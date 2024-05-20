package com.bamboo.controller;

import com.bamboo.dto.MemberFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {
    @GetMapping("/login")
    public String login(){

        return "oauthLogin";
    }

    @GetMapping("/signup")
    public String signup(Model model){

        model.addAttribute("MemberFormDto", new MemberFormDto());

        return "signup";
    }
}
