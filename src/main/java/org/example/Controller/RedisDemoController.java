package org.example.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class RedisDemoController {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisDemoController(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("/redis/ping")
    public String ping() {
        String pong = stringRedisTemplate.getRequiredConnectionFactory()
                .getConnection()
                .ping();
        return pong == null ? "NO_PONG" : pong;
    }

    @GetMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable String key, @PathVariable String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return "OK";
    }

    @GetMapping("/redis/get/{key}")
    public String get(@PathVariable String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        int count = 0;
        System.out.println(count++);
        return value == null ? "(nil)" : value;
    }
    
}


