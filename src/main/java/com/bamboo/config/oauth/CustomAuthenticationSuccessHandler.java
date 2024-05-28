package com.bamboo.config.oauth;

import com.bamboo.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        authentication = securityContext.getAuthentication();
        String userEmail = authentication.getName();

        if (memberService.isUserDeleted(userEmail)) {
//            System.out.println("이용 불가능한 사용자입니다.(일반 로그인) ");
            // 정지된 사용자라면 로그아웃 처리
            request.getSession().invalidate();
            response.sendRedirect("/login?error=accountLocked&redirect=true");
        } else {
//            System.out.println("이용 가능한 사용자입니다. (일반 로그인) ");
            // 정지되지 않은 사용자라면 홈 페이지로 리다이렉트
            response.sendRedirect("/");
        }
    }
}
