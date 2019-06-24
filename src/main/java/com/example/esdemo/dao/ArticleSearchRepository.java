package com.example.esdemo.dao;

import com.example.esdemo.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleSearchRepository extends ElasticsearchRepository<Article,Long> {
    List<Article> findByTitle(String title);

    List<Article> queryArticlesByText(String text);

    Article findArticleById(Long id);
}
