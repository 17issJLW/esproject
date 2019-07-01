package com.example.esdemo.dao;

import com.example.esdemo.entity.Article;
import com.example.esdemo.entity.Doc;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;


public interface DocRepository extends ElasticsearchRepository<Doc,Long> {
    List<Doc> queryByContent(String content);


    Doc findById(long id);
}
