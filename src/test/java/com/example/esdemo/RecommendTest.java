package com.example.esdemo;

import com.example.esdemo.dao.DocRepository;
import com.example.esdemo.entity.Doc;
import com.example.esdemo.service.DocRecommendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommendTest {

    @Autowired
    private DocRecommendService docRecommendService;

    @Autowired
    private DocRepository docRepository;

    @Test
    public void testRecommend(){
//        docRecommendService.recommendByUser("4aa476a72347ba44c9bd20c974d0f181");
    }

    @Test
    public void testMap(){
        Map<String,Long> map = new HashMap<>();
        map.put("a",1l);
        map.put("b",2l);
        map.put("c",9l);
        map.put("b",6l);
//        Collections.max()
    }

    @Test
    public void testDao(){
        Sort sort = new Sort(Sort.Direction.DESC, "time");
        Pageable pageable = new PageRequest(0,5,sort);
        Page<Doc> list = docRepository.findAll(pageable);
        System.out.println(list);
    }
}
