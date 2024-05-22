package com.bamboo.controller;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.MemberFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {
    @GetMapping("/login")
    public String login(){

        return "member/oauthLogin";
    }

    @GetMapping("/signup")
    public String signup(MemberFormDto request, Model model){

        model.addAttribute("MemberFormDto", new MemberFormDto());

        return "member/signup";
    }

    @GetMapping("/modifyMember")
    public String modifyMember(Model model){

        model.addAttribute("MemberFormDto", new MemberFormDto());
        model.addAttribute("loginType", MyOAuth2MemberService.loginType);

        return "member/modifyMember";
    }
}
