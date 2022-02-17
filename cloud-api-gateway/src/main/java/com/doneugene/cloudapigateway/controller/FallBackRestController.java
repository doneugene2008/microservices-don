package com.doneugene.cloudapigateway.controller;


import com.doneugene.cloudapigateway.service.ResilienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/resilience")
public class FallBackRestController {
    /*@Autowired
    private ResilienceService resilienceService;

    @GetMapping("/cb")
    public String circuitBreaker() {
        return resilienceService.circuitBreaker();
    }
    @GetMapping("/bulkhead")
    public String bulkhead() {
        return resilienceService.bulkHead();
    }
    @GetMapping("/tl")
    public CompletableFuture<String> timeLimiter() {
        return resilienceService.timeLimiter();
    }
    @GetMapping("/rl")
    public String rateLimiter() {
        return resilienceService.rateLimiter();
    }
    @GetMapping("/retry")
    public String retry() {
        return resilienceService.retry();
    }*/

}