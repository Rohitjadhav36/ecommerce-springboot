package com.ecom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {
    @RequestMapping("/signin")
    public String login()
    {
        return "login";
    }

    @RequestMapping("/register")
    public String register()
    {
        return "register";
    }
}

