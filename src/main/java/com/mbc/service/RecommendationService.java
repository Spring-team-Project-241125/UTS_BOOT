package com.mbc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecommendationService {
    private final RestTemplate restTemplate;
    private final String FASTAPI_URL = "http://127.0.0.1:8000/recommend/";

    public RecommendationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getRecommendations(int memberId) {
        String url = FASTAPI_URL + memberId;
        return restTemplate.getForObject(url, String.class);
    }
}


