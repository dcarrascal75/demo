package com.tutorialspoint.demo.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
 
@RestController
@RequestMapping("/ping")
public class TestController {
 
    @RequestMapping
    public String doAlive() {
        return "Alive!";
    }
 
    @RequestMapping("/rest")
    public String doRestAlive() {
        return new RestTemplate()
                .getForObject("https://localhost:443/ping", String.class);
 
    }
}
