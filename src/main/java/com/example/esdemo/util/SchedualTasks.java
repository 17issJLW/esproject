package com.example.esdemo.util;

import com.example.esdemo.dao.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SchedualTasks {
    @Autowired
    RedisUtils redisUtils;

    @Scheduled(cron = "0/10 * * * * *")
    public void timer() {
        redisUtils.get("heartbeat");
//        System.out.println("beat");
    }
}
