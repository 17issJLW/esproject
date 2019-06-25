package com.example.esdemo.controller;

import com.example.esdemo.lib.exception.NotFound;
import com.example.esdemo.service.DocSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DocSearchController {

    @Autowired
    DocSearchService docSearchService;

    @GetMapping("/doc")
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
        Object docType = docSearchService.docTypeAgregation();
        Object time = docSearchService.timeAgregation();
        Map<String,Object> res = new HashMap<>();
        res.put("docType",docType);
        res.put("time",time);
        return res;
    }

}
