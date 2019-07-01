package com.example.esdemo.controller;

import com.example.esdemo.entity.Doc;
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
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DocSearchController {

    @Autowired
    DocSearchService docSearchService;

    @GetMapping("/doc")
    public Object simpleSearch(@RequestParam(value = "keyword",defaultValue = "") List<String> keyword,
                               @PageableDefault(page = 0, size = 10) Pageable pageable){
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
        Object stage = docSearchService.agregationSearch("stage",false);
        Object location = docSearchService.agregationSearch("location",false);
        Object lawNameList = docSearchService.agregationSearch("lawNameList",false);
        Map<String,Object> res = new HashMap<>();
        res.put("docType",docType);
        res.put("time",time);
        res.put("caseType",caseType);
        res.put("reason",reason);
        res.put("stage",stage);
        res.put("location",location);
        res.put("lawNameList",lawNameList);
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
                                 @PageableDefault(page = 0, size = 10) Pageable pageable){

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

    @PostMapping("/doc/result")
    public Object searchResultAnalse(@RequestBody @Valid AdvancedSearchValidation advancedSearchValidation,
                                     BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            for(ObjectError error : bindingResult.getAllErrors()){
                Map<String,Object> res = new HashMap<>();
                res.put("message", error.getDefaultMessage());
                return new ResponseEntity<Map<String,Object>>(res, HttpStatus.BAD_REQUEST);
            }
        }
        List<Map<String,String>> mapList = new ArrayList<>();
        Map<String,String> docType = new HashMap<>();
        docType.put("key","docType");
        docType.put("order","false");

        Map<String,String> caseType = new HashMap<>();
        caseType.put("key","caseType");
        caseType.put("order","false");

        Map<String,String> reason = new HashMap<>();
        reason.put("key","reason");
        reason.put("order","false");

        Map<String,String> stage = new HashMap<>();
        stage.put("key","stage");
        stage.put("order","false");

        Map<String,String> lawNameList = new HashMap<>();
        lawNameList.put("key","lawNameList");
        lawNameList.put("order","false");

        Map<String,String> location = new HashMap<>();
        location.put("key","location");
        location.put("order","false");

        mapList.add(docType);
        mapList.add(caseType);
        mapList.add(reason);
        mapList.add(stage);
        mapList.add(lawNameList);
        mapList.add(location);

        Object res = docSearchService.searchResultAnalyse(advancedSearchValidation,mapList);

//        Object time = docSearchService.agregationSearchTime("time",false,"yyyy");
        return res;

    }

    @GetMapping("/doc/recommend")
    public Object docRecommend(@RequestParam(value = "caseType", required = false, defaultValue = "") String caseType,
                               @RequestParam(value = "reason", required = false, defaultValue = "") String reason,
                               @RequestParam(value = "docType", required = false, defaultValue = "") String docType,
                               @RequestParam(value = "id") long id){

        Object res = docSearchService.recomendByType(caseType,reason,docType,id);
        return res;


    }

}
