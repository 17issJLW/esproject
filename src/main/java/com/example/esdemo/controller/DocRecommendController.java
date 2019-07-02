package com.example.esdemo.controller;

import com.example.esdemo.entity.Doc;
import com.example.esdemo.service.DocRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
public class DocRecommendController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DocRecommendService docRecommendService;

    @GetMapping("/doc/history")
    public List<Doc> history(){
        String fingerprint = request.getHeader("Token");
        if(fingerprint == null) return null;
        List<Doc> docList = docRecommendService.getUserHistory(fingerprint);
        return docList;
    }

    @GetMapping("/doc/recommend")
    public Page<Doc> recommendByUser(@PageableDefault(size = 10,page = 0) Pageable pageable){
        String fingerprint = request.getHeader("Token");
        Page<Doc> page = docRecommendService.recommendByUser(fingerprint,pageable);
        return page;
    }
}
