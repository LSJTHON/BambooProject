package com.bamboo.controller;



import com.bamboo.dto.MemberDeleteDto;
import com.bamboo.dto.MemberFormDto;
import com.bamboo.entity.Member;
import com.bamboo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Controller
public class MemberApiController {
    private final MemberService memberService;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    @PostMapping("/user")
    public String signup(@Valid @ModelAttribute("MemberFormDto")MemberFormDto request,BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            return "member/signup";
        }
        // 이메일 형식이 올바른지 확인
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            bindingResult.rejectValue("email", "emailInCorrect", "유효한 이메일 주소를 입력해주세요.");
            return "member/signup";
        }
        if (request.getPassword().length() < 4 || request.getPassword().length() > 16) {
            bindingResult.rejectValue("name", "nameLengthIncorrect",
                    "비밀번호는 8자 이상 16자 이하이어야 합니다.");
            return "member/signup";
        }
        if(!request.getPassword().equals(request.getConfirmPassword())) {
            bindingResult.rejectValue("password", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "member/signup";
        }
        if (request.getName().length() < 3 || request.getName().length() > 8) {
            bindingResult.rejectValue("name", "nameLengthIncorrect",
                    "이름은 3자 이상 8자 이하이어야 합니다.");
            return "member/signup";
        }
        memberService.save(request); //회원가입 메소드 호출

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
