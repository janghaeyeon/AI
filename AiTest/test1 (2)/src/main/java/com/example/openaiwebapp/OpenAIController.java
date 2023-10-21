package com.example.openaiwebapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OpenAIController {
    //private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/gpt-4.0-turbo/completions";
    //private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/davinci/completions";
    //private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/assistant/completions";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";
    private static final String OPENAI_API_KEY = "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";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/ask")
    public String ask(@RequestParam String question, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> payload = new HashMap<>();
        payload.put("prompt", question);
        payload.put("max_tokens", 1500);
        payload.put("temperature", 0.0);
        payload.put("model", "text-davinci-003");
        payload.put("top_p",1.0);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
        headers.set("Content-Type", "application/json");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        String answer = null;
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_API_URL, entity, Map.class);


            HttpHeaders responseHeaders = response.getHeaders();
            String limit = responseHeaders.getFirst("X-RateLimit-Limit");
            String remaining = responseHeaders.getFirst("X-RateLimit-Remaining");
            String reset = responseHeaders.getFirst("X-RateLimit-Reset");

            System.out.println("Rate Limit: " + limit);
            System.out.println("Remaining Requests: " + remaining);
            System.out.println("Reset Time: " + reset);

            Map responseBody = response.getBody();
            answer = (String) ((Map) ((List) responseBody.get("choices")).get(0)).get("text");
            if (answer != null) {
                model.addAttribute("answer", answer.trim());
                model.addAttribute("question", question.trim());
            }
            else {
                model.addAttribute("answer", "error1");
            }
        } catch (Exception e) {
            System.err.println("Error occurred while calling OpenAI API: " + e.getMessage());
            //model.addAttribute("error", "An error occurred while processing your request. Please try again later.");
            model.addAttribute("answer", "ERROR:" +  e.getMessage());
        }
        System.out.println("question: " + question);
        System.out.println("answer: " + answer);
        return "index";
    }
}