package com.example.esdemo.service;

import com.example.esdemo.dao.DocRepository;
import com.example.esdemo.entity.Doc;
import com.example.esdemo.lib.exception.NotFound;
import com.example.esdemo.util.MyResultMapper;
import org.apache.lucene.index.Terms;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.ScriptField;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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

    public Object docTypeAgregation(){
        /**
         * 搜索结果聚合查询
         */

        AbstractAggregationBuilder docType = AggregationBuilders.terms("docType")
                .field("docType").size(10).order(BucketOrder.count(false));

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(docType)
                .build();


        Aggregations aggregations = esTemplate.query(query, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        Map<String, Aggregation> aggregationMap = aggregations.asMap();

        return aggregationMap.get("docType").toString();

    }

    public Object timeAgregation(){
        /**
         * 搜索结果聚合查询
         */

        AbstractAggregationBuilder time = AggregationBuilders.terms("time")
                .field("time").format("yyyy").size(10).order(BucketOrder.count(false));


        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(time)
                .build();


        Aggregations aggregations = esTemplate.query(query, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        Map<String, Aggregation> aggregationMap = aggregations.asMap();

        return aggregationMap.get("time").toString();

    }
}
