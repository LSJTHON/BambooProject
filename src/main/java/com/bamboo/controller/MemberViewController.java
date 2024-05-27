package com.bamboo.controller;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.MemberFormDto;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberViewController {
    @GetMapping("/login")
    public String login(Model model){


        model.addAttribute("MemberFormDto", new MemberFormDto());

        return "member/oauthLogin";
    }



    @GetMapping("/signup")
    public String signup(Model model){

        model.addAttribute("MemberFormDto", new MemberFormDto());

        return "member/signup";
    }

    @GetMapping("/modifyMember")
    public String modifyMember(Model model){

        Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = kakaAuthentication.getPrincipal();
        Boolean loginType2 = (principal instanceof OAuth2User);

        model.addAttribute("MemberFormDto", new MemberFormDto());
        model.addAttribute("loginType", loginType2);

        return "member/modifyMember";
    }
}
