package com.bamboo.controller;



import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.MemberDeleteDto;
import com.bamboo.dto.MemberFormDto;
import com.bamboo.entity.Member;
import com.bamboo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/user")
    public String signup(@Valid MemberFormDto request, Model model, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            return "/signup";
        }

        try{
            memberService.save(request); //회원가입 메소드 호출
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/signup";
        }
        return "redirect:/login";  //회원 가입이 완료된 이후에 로그인 페이지로 이동
    }



    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @PutMapping("/deleteMember")
    public ResponseEntity<Member> deleteMember(@RequestBody MemberDeleteDto request){

        System.out.println(request.getEmail()+": 정지할 이메일 이름");
        System.out.println(request.isDeleted()+": 정지 여부?");

        Member updatedMember = memberService.updatedDelete(request.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(updatedMember);

    }

}
