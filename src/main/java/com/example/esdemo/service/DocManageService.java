package com.example.esdemo.service;

import com.example.esdemo.dao.DocRepository;
import com.example.esdemo.entity.Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Date;

@Service
public class DocManageService {

    @Autowired
    private DocRepository docRepository;

    public boolean addNewDoc(Doc doc){

        doc.setId(new Date().getTime());
        docRepository.save(doc);
        return true;
    }

    public Doc findDoc(long id){

        Doc doc = docRepository.findById(id);
        if(doc == null){
            return null;
        }
        return doc;
    }

    public Object findAllDoc(Pageable pageable){
        return docRepository.findAll(pageable);
    }

    public Object updateDoc(long id, Doc doc){
        doc.setId(id);
        docRepository.save(doc);
        return doc;
    }

    public boolean deleteDoc(long id){
        Doc doc = docRepository.findById(id);
        if(doc == null) return false;
        docRepository.delete(doc);
        return true;
    }
}
