package com.example.esdemo.controller;

import com.example.esdemo.service.DocSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DocSearchController {

    @Autowired
    DocSearchService docSearchService;

    @GetMapping("/search")
    public Object simpleSearch(@RequestParam(value = "keyword",defaultValue = "") String keyword,
                               @PageableDefault(page = 0, size = 20) Pageable pageable){
        Object res = docSearchService.simpleSearch(keyword, pageable);

        return res;

    }

    @GetMapping("/doc/{id}")
    public Object openDoc(@PathVariable(value = "id") long id){
        Object res = docSearchService.openSingleDoc(id);
        return res;
    }

    @GetMapping("/doctype")
    public Object getAllDocType(){
        Object res = docSearchService.subAgregation();
        return res;
    }

}
