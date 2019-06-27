package com.example.esdemo.controller;


import com.example.esdemo.dao.DocRepository;
import com.example.esdemo.entity.Doc;
import com.example.esdemo.service.DocSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
public class DocAddController {

    @Autowired
    private DocSearchService docSearchService;
    @Autowired
    private DocRepository docRepository;


    @PostMapping("/adddoc")
    public Object addNewDoc(Doc doc){
//        System.out.println(doc.toString());
        doc.setId(new Date().getTime());
        docRepository.save(doc);
        return doc;
    }
}
