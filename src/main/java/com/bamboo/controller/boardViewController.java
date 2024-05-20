package com.bamboo.controller;


import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.MemberFormDto;
import com.bamboo.entity.FileConfig;
import com.bamboo.entity.Member;
import com.bamboo.service.MemberService;
import com.bamboo.service.fileAllowedService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class boardViewController {

    private final MemberService memberService;
    private final fileAllowedService fileAllowedService;

    //메인 페이지에 보여질 정보들을 return 해주는 html 에 model 로 쏴줌
    @GetMapping("/testAllowed")
    public String getPorts(Model model, @AuthenticationPrincipal Member member){


        //필요에 따라 member.getName()도 가능함
        String memberEmail = (member == null) ? MyOAuth2MemberService.userEmail : member.getUsername();


        FileConfig fileConfig = fileAllowedService.findById(1L);
        String extensions = fileAllowedService.getExtensionsByFileConfigId(1L);

        model.addAttribute("extensions", extensions);
        model.addAttribute("allowed",fileConfig);
        model.addAttribute("memberEmail",memberEmail);
        return "fileAllowed";
    }


    @GetMapping("/")
    public String getPage(@AuthenticationPrincipal Member member, Model model){

        String memberEmail = (member == null) ? MyOAuth2MemberService.userEmail : member.getUsername();

        System.out.println("멤버 이메일은 : "+memberEmail);

        if(memberEmail != null){
            Member memberInfo =  memberService.findByEmail(memberEmail);
            System.out.println(
                    "이메일 정보 : "+memberInfo.getEmail()+
                            "\n이름 : " +memberInfo.getName()+
                            "\n권한 정보 : "+memberInfo.getRole()
            );
            model.addAttribute("memberEmail",memberInfo);
            return "mainPage";
        }else{
            model.addAttribute("memberEmail",memberEmail);
            return "mainPage";
        }
    }

    @GetMapping("/1")
    public String getPage2(@AuthenticationPrincipal Member member, Model model){

        String memberEmail = (member == null) ? MyOAuth2MemberService.userEmail : member.getUsername();

        model.addAttribute("memberEmail",memberEmail);

        return "testHtml";
    }
}
