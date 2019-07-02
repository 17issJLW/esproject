package com.example.esdemo.service;

import com.example.esdemo.dao.DocRepository;
import com.example.esdemo.dao.RedisUtils;
import com.example.esdemo.entity.Doc;
import org.apache.lucene.search.MultiCollectorManager;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.mapstruct.MappingInheritanceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DocRecommendService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private DocRepository docRepository;

    public List<Doc> getUserHistory(String fingerprint) {
        String prefix = String.format(RedisUtils.history, fingerprint, "*");
        Set<String> keys = redisUtils.getKeyByPrefix(prefix);
        if (!keys.isEmpty()) {
            Set<Long> idSet = new HashSet<>();
            keys.forEach(key -> {
                Long id = (Long) redisUtils.get(key);
                idSet.add(id);
            });
            List<Doc> docList = docRepository.queryByIdIn(idSet);
            return docList;
        }
        return null;
    }


    public String findMaxCountWord(Map<String,Long> map) {
        String word = "";
        Collection<Long> count = map.values();
        long maxCount = Collections.max(count);
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            if (maxCount == entry.getValue()) {
                word = entry.getKey();
                break;
            }
        }
        return word;

    }


    public Page<Doc> recommendByUser(String fingerprint,Pageable pageable) {

        List<Doc> docList = getUserHistory(fingerprint);

        if (docList != null) {
            List<String> reasonList = new ArrayList<>();
            List<String> docTypeList = new ArrayList<>();
            List<Long> idList = new ArrayList<>();
            docList.forEach(doc -> {
                reasonList.add(doc.getReason());
                docTypeList.add(doc.getDocType());
                idList.add(doc.getId());
            });

            Map<String, Long> reasonMap = docList.stream()
                    .collect(Collectors.groupingBy(Doc::getReason, Collectors.counting()));
            Map<String, Long> docTypeMap = docList.stream()
                    .collect(Collectors.groupingBy(Doc::getDocType, Collectors.counting()));

            String reason = findMaxCountWord(reasonMap);
            String docType = findMaxCountWord(docTypeMap);
            Page<Doc> result = docRepository.findByReasonAndDocTypeAndIdNotInOrderByClickCountDesc(reason, docType, idList, pageable);

            return result;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "time");
        pageable.getSortOr(sort);
        return docRepository.findAll(pageable);

    }
}
