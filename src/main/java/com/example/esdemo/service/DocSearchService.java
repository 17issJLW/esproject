package com.example.esdemo.service;

import com.example.esdemo.dao.DocRepository;
import com.example.esdemo.dao.RedisUtils;
import com.example.esdemo.entity.Doc;
import com.example.esdemo.lib.exception.NotFound;
import com.example.esdemo.lib.validation.AdvancedSearchValidation;
import com.example.esdemo.util.MyResultMapper;
import com.google.gson.Gson;
import org.apache.lucene.index.Terms;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.pipeline.bucketselector.BucketSelectorPipelineAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.ScriptField;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class DocSearchService {


    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private DocRepository docRepository;

    @Autowired
    private MyResultMapper myResultMapper;

    @Autowired
    private RedisUtils redisUtils;

    private String highlightPreTags = "<span class='highlight' style='color:red'>";
    private String highlightPostTags = "</span>";

    HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content")
            .preTags(highlightPreTags)
            .postTags(highlightPostTags);

    HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("caseName")
            .preTags(highlightPreTags)
            .postTags(highlightPostTags);

    HighlightBuilder highlightBuilder = new HighlightBuilder().field("content",5,30)
            .preTags(highlightPreTags)
            .postTags(highlightPostTags);




    public Object simpleSearch(List<String> keyword, Pageable pageable){
        /**
         * 简单全文搜索
         */
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        for(String key:keyword){
            queryBuilder.filter(queryStringQuery(key));
        }
//        Script script = new Script("doc['reason'].value.length()");
//        ScriptField scriptField = new ScriptField("size",script);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .withHighlightBuilder(highlightBuilder)
                .withHighlightFields(highlightContent,highlightTitle)
                .build();

        return esTemplate.queryForPage(searchQuery, Doc.class, myResultMapper);
    }

    public Object openSingleDoc(long id,String fingerprint){
        /**
         * 打开单一文档
         */
        System.out.println("start");
        Doc doc = docRepository.findById(id);
        if(doc == null)
            throw new NotFound();
//redis统计点击量，并定期同步到es中
        System.out.println(doc.getId());

        long docId = doc.getId();
        String key = String.format(RedisUtils.docClickCount, docId);
        int clickCount = (int)redisUtils.incr(key,1);
        doc.setClickCount(clickCount);

        System.out.println(String.format("id:%d,%d",doc.getId(),clickCount));

//        记录浏览器指纹和访问文档id
        if(!fingerprint.isEmpty()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateStr = sdf.format(new Date());
            redisUtils.set(String.format(RedisUtils.history,fingerprint,dateStr),doc.getId());
        }

        if(clickCount % 10 == 0){
            docRepository.save(doc);
        }
        return doc;

    }

    public Object agregationSearch(String field, boolean order){
        /**
         * 搜索结果聚合查询
         */

        AbstractAggregationBuilder abstractAggregationBuilder = AggregationBuilders.terms(field)
                .field(field).size(10).order(BucketOrder.count(order));

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(abstractAggregationBuilder)
                .build();


        Aggregations aggregations = esTemplate.query(query, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
//        为了绕开这个aggregationMap序列化为json出错的问题
//        下面操作将map转为String ，再转回map。。。。
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        Gson gson = new Gson();
        Map<String,Object> map = new HashMap<>();
        map = gson.fromJson(aggregationMap.get(field).toString(),map.getClass());
        return map;

    }

    public Object agregationSearchTime(String field, boolean order, String format){
        /**
         * 时间聚合查询
         */
        DateHistogramAggregationBuilder abstractAggregationBuilder = AggregationBuilders.dateHistogram(field)
                .field(field).dateHistogramInterval(DateHistogramInterval.YEAR).format(format).order(BucketOrder.key(order));

        Map<String, String> bucketsPathsMap = new HashMap<>(8);
        bucketsPathsMap.put("count", "_count");
        Script script = new Script("params.count > 0");

        BucketSelectorPipelineAggregationBuilder bs =
                PipelineAggregatorBuilders.bucketSelector("having", bucketsPathsMap, script);

        abstractAggregationBuilder.subAggregation(bs);
//        AbstractAggregationBuilder abstractAggregationBuilder = AggregationBuilders.terms(field)
//                .field(field).format(format).size(10).order(BucketOrder.key(false));

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(abstractAggregationBuilder)
                .build();


        Aggregations aggregations = esTemplate.query(query, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
//        为了绕开这个aggregationMap序列化为json出错的问题
//        下面操作将map转为String ，再转回map。。。。
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        Gson gson = new Gson();
        Map<String,Object> map = new HashMap<>();
        map = gson.fromJson(aggregationMap.get(field).toString(),map.getClass());
        return map;

    }

    /**
     * 根据复杂搜索构造BoolQueryBuilder
     * @param data
     * @return
     */
    public BoolQueryBuilder buildBoolQueryBuilder(AdvancedSearchValidation data){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        if(!data.getCourt().isBlank()){
            queryBuilder.must(matchQuery("court",data.getCourt()));
        }
        if(!data.getDocType().isBlank()){
            queryBuilder.must(termQuery("docType",data.getDocType()));
        }
        if(!data.getReason().isBlank()){
            queryBuilder.must(matchQuery("reason",data.getReason()));
        }
        if(!data.getStage().isBlank()){
            queryBuilder.must(termQuery("stage",data.getStage()));
        }
        if((data.getFromYear() != 0) &&  (data.getToYear() != 0) ){
            queryBuilder.must(rangeQuery("time").format("yyyy").from(data.getFromYear()).to(data.getToYear()));
        }
        if(!data.getKeyword().isEmpty()){
            for(String key:data.getKeyword()){
                if(!key.isBlank()){
//                    queryBuilder.must(queryStringQuery(key).boost(2f));
                    queryBuilder.must(multiMatchQuery(key,"content","caseName")
                            .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                            .tieBreaker(0.1f));
                }
            }
        }
        if(data.getLaw() != null && !data.getLaw().isBlank()){
            queryBuilder.must(fuzzyQuery("lawList",data.getLaw()));
        }
        if(data.getLocation() != null && !data.getLocation().isBlank()){
            queryBuilder.must(termQuery("location",data.getLocation()));
        }

        return queryBuilder;
    }

    /**
     * 高级搜索
     * @param data
     * @param pageable
     * @return
     */
    public Object AdvantureSearch(AdvancedSearchValidation data,Pageable pageable){

        BoolQueryBuilder queryBuilder = buildBoolQueryBuilder(data);

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        if(!data.getSortBy().isEmpty()){
            SortOrder order = SortOrder.DESC;
            if(data.isOrder()) order = SortOrder.ASC;
            SortBuilder sortBuilder = SortBuilders.fieldSort(data.getSortBy())
                    .order(order);
            nativeSearchQueryBuilder.withSort(sortBuilder);
        }


        SearchQuery query = nativeSearchQueryBuilder
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .withHighlightBuilder(highlightBuilder)
                .withHighlightFields(highlightContent,highlightTitle)
                .build();


        return esTemplate.queryForPage(query,Doc.class,myResultMapper);
    }

    /**
     * 根据文书推荐
     * @param caseType
     * @param reason
     * @param docType
     * @param id
     * @return
     */
    public Object recomendByType(String caseType,String reason,String docType,long id){

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        if(!caseType.isEmpty())
            queryBuilder.filter(matchQuery("caseType",caseType));

        if(!reason.isEmpty())
            queryBuilder.filter(matchQuery("reason",reason));

        if(!docType.isEmpty())
            queryBuilder.filter(matchQuery("docType",docType));

        queryBuilder.mustNot(matchQuery("id",id));

        SearchQuery query = nativeSearchQueryBuilder
                .withQuery(queryBuilder)
                .withPageable(new PageRequest(0,5))
                .build();

        List<Doc> listDoc = (List<Doc>)esTemplate.queryForList(query,Doc.class);
        listDoc.forEach(doc -> {
            Object o = redisUtils.get(String.format(RedisUtils.docClickCount,doc.getId()));
            int clickCount = 0;
            if(o != null) clickCount = (int)redisUtils.get(String.format(RedisUtils.docClickCount,doc.getId()));
            doc.setClickCount(clickCount);
        });

        return listDoc;

    }

    public Object recomendByLike(long id){

        Doc doc = docRepository.findById(id);
        MoreLikeThisQueryBuilder moreLikeThisQueryBuilder = QueryBuilders
                .moreLikeThisQuery(new String[] {doc.getReason()});
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(moreLikeThisQueryBuilder)
                .build();

        return esTemplate.queryForList(query,Doc.class);


    }


    /**
     * 搜索结果聚合
     * @param data
     * @param mapList
     * @return
     */
    public Object searchResultAnalyse(AdvancedSearchValidation data,
                                      List<Map<String,String>> mapList){
        List<AbstractAggregationBuilder> aabList = new ArrayList<>();
        BoolQueryBuilder queryBuilder = buildBoolQueryBuilder(data);
        mapList.forEach(map -> {
            System.out.println(map.get("key"));
            AbstractAggregationBuilder abstractAggregationBuilder = AggregationBuilders.terms(map.get("key"))
                    .field(map.get("key")).size(8).order(BucketOrder.count(map.get("order").equals("true")));
            aabList.add(abstractAggregationBuilder);
        });


        DateHistogramAggregationBuilder abstractAggregationBuilder = AggregationBuilders.dateHistogram("year")
                .field("time").dateHistogramInterval(DateHistogramInterval.YEAR).format("yyyy").order(BucketOrder.key(false));

        Map<String, String> bucketsPathsMap = new HashMap<>(8);
        bucketsPathsMap.put("count", "_count");
        Script script = new Script("params.count > 0");

        BucketSelectorPipelineAggregationBuilder bs =
                PipelineAggregatorBuilders.bucketSelector("having", bucketsPathsMap, script);

        abstractAggregationBuilder.subAggregation(bs);


        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(queryBuilder);
        aabList.forEach(aab->{
            nativeSearchQueryBuilder.addAggregation(aab);
        });
        nativeSearchQueryBuilder.addAggregation(abstractAggregationBuilder);

        SearchQuery query = nativeSearchQueryBuilder.build();
        Aggregations aggregations = esTemplate.query(query, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        List<Object> resList = new ArrayList<>();
        Gson gson = new Gson();
        mapList.forEach(map -> {
            StringTerms stringTerms = (StringTerms) aggregationMap.get(map.get("key"));
            System.out.println(stringTerms);
//            List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
            Object ob = gson.fromJson(stringTerms.toString(),Object.class);
            resList.add(ob);

        });

        Object ob = gson.fromJson(aggregationMap.get("year").toString(), Object.class);
        resList.add(ob);
        return resList;

    }
}
