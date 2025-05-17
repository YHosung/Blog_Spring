package com.test.project.controller;

import com.test.project.service.VisitorCounter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VisitorController {

    private final VisitorCounter visitorCounter;

    public VisitorController(VisitorCounter visitorCounter) {
        this.visitorCounter = visitorCounter;
    }

    @GetMapping("/visitor") // 또는 "/main"
    public String main(Model model) {
        visitorCounter.increment(); // 방문자 수 증가

        model.addAttribute("todayVisitors", visitorCounter.getTodayCount());
        model.addAttribute("totalVisitors", visitorCounter.getTotalCount());

        return "main"; // main.html 템플릿 이름
    }
}
