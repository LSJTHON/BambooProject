package com.bamboo.config;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final MyOAuth2MemberService myOAuth2MemberService;

    // 토큰 방식으로 인증을 하기 때문에 기존에 사용하던 플러그인, 세션 비활성화
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MemberService memberService)
            throws Exception {
        //특정 요구사항에 따라 세션 유지
        http
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        // 토큰 재발급 URL은 인증 없이 접근 가능하도록 설정. 나머지 API URL은 인증 필요.
        http.authorizeRequests(authorize -> authorize
                        .requestMatchers("/login","/signup", "/css/**","/user",
                                "/js/**","/img/**","/")
                        .permitAll() // 해당 html 페이지들은 로그인 하지 않아도 접속 가능
                        .requestMatchers("/articleList","/api/**","/testAllowed")
                        .authenticated()  //권한 필요

                        // 나머지 API URL은 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/")
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/articles/callback"))
                        // Authorization 요청과 관련된 상태 저장
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(myOAuth2MemberService))
                )
//                .logout((logout) -> logout
//                        .logoutSuccessUrl("/"))
                .logout((logout) -> logout
                        .logoutSuccessHandler(customLogoutSuccessHandler()))

                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

//    private LogoutSuccessHandler customLogoutSuccessHandler() {
//        MyOAuth2MemberService.userEmail=null;
//        return null;
//    }

    private LogoutSuccessHandler customLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            MyOAuth2MemberService.userEmail=null;
            // 로그아웃 후 리다이렉트
            response.sendRedirect("/");
        };
    }


    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin1234@naver.com")
//                .password(bCryptPasswordEncoder().encode("admin1234"))
//                .roles("ADMIN");
//    }
}

