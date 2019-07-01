package com.example.esdemo.controller;


import com.example.esdemo.dao.DocRepository;
import com.example.esdemo.entity.Doc;
import com.example.esdemo.lib.validation.AdvancedSearchValidation;
import com.example.esdemo.service.DocManageService;
import com.example.esdemo.service.DocSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DocManageController {

    @Autowired
    private DocManageService docManageService;

    @GetMapping("/doc/manage/{id}")
    public Object findOneDoc(@PathVariable(name = "id") long id){
        Doc doc = docManageService.findDoc(id);
        if(doc == null) return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        return doc;
    }

    @GetMapping("/doc/manage")
    public Object findAllDoc(@PageableDefault(page = 0,size = 10) Pageable pageable){
        return docManageService.findAllDoc(pageable);
    }

    @PostMapping("/doc/manage")
    public Object addNewDoc(@RequestBody @Valid Doc doc,
                            BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            for(ObjectError error : bindingResult.getAllErrors()){
                Map<String,Object> res = new HashMap<>();
                res.put("message", error.getDefaultMessage());
                return new ResponseEntity<Map<String,Object>>(res,HttpStatus.BAD_REQUEST);
            }
        }
        docManageService.addNewDoc(doc);
        return doc;
    }

    @PutMapping("/doc/manage/{id}")
    public Object updateDoc(@PathVariable(name = "id") long id,
                            @RequestBody @Valid Doc doc,
                            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
        }
        return docManageService.updateDoc(id,doc);

    }

    @DeleteMapping("/doc/manage/{id}")
    public Object deleteDoc(@PathVariable long id){
        return docManageService.deleteDoc(id);
    }



}
