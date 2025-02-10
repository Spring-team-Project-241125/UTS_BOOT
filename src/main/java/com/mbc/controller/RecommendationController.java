package com.mbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbc.security.PrincipalDetails;
import com.mbc.service.RecommendationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller  // RestController 대신 Controller 사용
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // 추천 데이터를 모델에 추가하고, 추천 결과를 HTML 페이지로 반환
    @GetMapping("/recommend/{memberId}")
    public String recommend(@PathVariable int memberId, Model model) {
        String recommendationsJson = recommendationService.getRecommendations(memberId);

        System.out.println("recommendationsJson: " + recommendationsJson);

        try {
            // JSON 문자열을 Java 객체(List)로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<Map<String, Object>>> parsedJson = objectMapper.readValue(recommendationsJson, Map.class);
            List<Map<String, Object>> recommendationsList = parsedJson.get("recommendations");

            // 변환된 데이터를 모델에 추가
            model.addAttribute("recommendations", recommendationsList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // "recommendations.html" 템플릿을 반환
        return "AI/recommendations";
    }

}