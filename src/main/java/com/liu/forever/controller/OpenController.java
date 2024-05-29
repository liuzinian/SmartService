package com.liu.forever.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
@Controller

@RequestMapping("")
public class OpenController {


    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "World");
        return "ms";
    }
}
