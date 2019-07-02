package com.example.esdemo.dao;

import com.example.esdemo.entity.Article;
import com.example.esdemo.entity.Doc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface DocRepository extends ElasticsearchRepository<Doc,Long> {
    List<Doc> queryByIdIn(Collection<Long> idList);
    Page<Doc> findByReasonAndDocTypeAndIdNotInOrderByClickCountDesc(String reason, String docType, List<Long> idList, Pageable pageable);
    Doc findById(long id);
    List<Doc> findByOrderByTimeDesc(Pageable pageable);
}
