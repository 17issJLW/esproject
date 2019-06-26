package com.example.esdemo.controller;

import com.example.esdemo.lib.exception.NotFound;
import com.example.esdemo.lib.validation.AdvancedSearchValidation;
import com.example.esdemo.service.DocSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
        Object docType = docSearchService.agregationSearch("docType",false);
        Object time = docSearchService.agregationSearchTime("time",false,"yyyy");
        Object caseType = docSearchService.agregationSearch("caseType",false);
        Object reason = docSearchService.agregationSearch("reason",false);
        Map<String,Object> res = new HashMap<>();
        res.put("docType",docType);
        res.put("time",time);
        res.put("caseType",caseType);
        res.put("reason",reason);
        return res;
    }

    /**
     * 高级搜索，精确搜索各字段
     * @param advancedSearchValidation
     * @param bindingResult
     * @param pageable
     * @return
     */
    @PostMapping("/doc")
    public Object advancedSearch(@RequestBody @Valid AdvancedSearchValidation advancedSearchValidation,
                                 BindingResult bindingResult,
                                 @PageableDefault(page = 0, size = 20) Pageable pageable){

        if(bindingResult.hasErrors()){
            for(ObjectError error : bindingResult.getAllErrors()){
                Map<String,Object> res = new HashMap<>();
                res.put("message", error.getDefaultMessage());
                return new ResponseEntity<Map<String,Object>>(res, HttpStatus.BAD_REQUEST);
            }
        }

        Object res = docSearchService.AdvantureSearch(advancedSearchValidation,pageable);
        return res;

    }

    @GetMapping("doc/recommend")
    public Object docRecommend(@RequestParam(value = "caseType", required = false, defaultValue = "") String caseType,
                               @RequestParam(value = "reason", required = false, defaultValue = "") String reason,
                               @RequestParam(value = "docType", required = false, defaultValue = "") String docType,
                               @RequestParam(value = "id") long id){

        Object res = docSearchService.recomendByType(caseType,reason,docType,id);

        return res;


    }

}
