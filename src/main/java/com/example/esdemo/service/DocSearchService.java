package com.example.esdemo.service;

import com.example.esdemo.dao.DocRepository;
import com.example.esdemo.entity.Doc;
import com.example.esdemo.lib.exception.NotFound;
import com.example.esdemo.lib.validation.AdvancedSearchValidation;
import com.example.esdemo.util.MyResultMapper;
import com.google.gson.Gson;
import org.apache.lucene.index.Terms;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.ScriptField;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.validation.Valid;
import java.sql.SQLOutput;
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




    public Object simpleSearch(String keyword, Pageable pageable){
        /**
         * 简单全文搜索
         */

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryStringQuery(keyword))
                .withPageable(pageable)
                .withHighlightBuilder(highlightBuilder)
                .withHighlightFields(highlightContent,highlightTitle)
                .build();

        return esTemplate.queryForPage(searchQuery, Doc.class, myResultMapper);
    }

    public Object openSingleDoc(long id){
        /**
         * 打开单一文档
         */
        Doc doc = docRepository.findById(id);
        if(doc == null)
            throw new NotFound();
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
        return map ;

    }

    public Object agregationSearchTime(String field, boolean order,String format){
        /**
         * 搜索结果聚合查询
         */

        AbstractAggregationBuilder abstractAggregationBuilder = AggregationBuilders.terms(field)
                .field(field).format(format).size(10).order(BucketOrder.count(order));

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


    public Object AdvantureSearch(AdvancedSearchValidation data,Pageable pageable){

        System.out.println(data);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        SortBuilder sortBuilder = null;

        if(!data.getSortBy().isEmpty()){
            SortOrder order = SortOrder.DESC;
            if(data.isOrder()) order = SortOrder.ASC;
            sortBuilder = SortBuilders.fieldSort(data.getSortBy())
                    .order(order);
        }

        if(!data.getCourt().isEmpty()){
            queryBuilder.filter(matchQuery("court",data.getCourt()));
        }
        if(!data.getDocType().isEmpty()){
            queryBuilder.filter(matchQuery("docType",data.getDocType()));
        }
        if(!data.getReason().isEmpty()){
            queryBuilder.filter(matchQuery("reason",data.getReason()));
        }
        if(!data.getStage().isEmpty()){
            queryBuilder.filter(matchQuery("stage",data.getStage()));
        }
        if((data.getFromYear() != 0) &&  (data.getToYear() != 0) ){
            queryBuilder.filter(rangeQuery("time").format("yyyy").from(data.getFromYear()).to(data.getToYear()));
        }
        if(!data.getKeyword().isEmpty()){
            queryBuilder.filter(multiMatchQuery(data.getKeyword(),"caseName","content"));
        }

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
//                .withQuery(QueryBuilders.queryStringQuery(data.getKeyword()))
                .withPageable(pageable)
                .withHighlightBuilder(highlightBuilder)
                .withHighlightFields(highlightContent,highlightTitle)
                .build();

        return esTemplate.queryForPage(query,Doc.class);
    }
}
