package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostExampleController {

    @GetMapping("/")
    public String index() {
        return "index";  // HTML 파일 이름 (확장자 제외)
    }

    @PostMapping("/postData")
    public String postData(@RequestParam String name, @RequestParam String email, Model model) {
        // 여기서 name과 email 값을 처리
        System.out.println("이름: " + name);
        System.out.println("이메일: " + email);

        model.addAttribute("message", "데이터가 성공적으로 전송되었습니다.");
        return "result";  // 결과를 표시할 HTML 파일 이름 (확장자 제외)
    }
}
