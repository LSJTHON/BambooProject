package com.codehows.sbd.config;

import com.codehows.sbd.config.jwt.TokenProvider;
import com.codehows.sbd.config.oauth.MyOAuth2UserService;
//import com.codehows.sbd.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
//import com.codehows.sbd.config.oauth.OAuth2SuccessHandler;
import com.codehows.sbd.config.oauth.OAuth2UserCustomService;
import com.codehows.sbd.repository.RefreshTokenRepository;
import com.codehows.sbd.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import lombok.RequiredArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final MyOAuth2UserService myOAuth2UserService;

    // 스프링 시큐리티 기능 비활성화
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
//                .requestMatchers(new AntPathRequestMatcher("/img/**"))
//                .requestMatchers(new AntPathRequestMatcher("/css/**"))
//                .requestMatchers(new AntPathRequestMatcher("/js/**"));
//    }

    // 토큰 방식으로 인증을 하기 때문에 기존에 사용하던 플러그인, 세션 비활성화
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
            throws Exception {
        http.csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable());

        http.sessionManagement(
                sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 헤더를 확인할 커스텀 필터 추가
//        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 토큰 재발급 URL은 인증 없이 접근 가능하도록 설정. 나머지 API URL은 인증 필요.
        http.authorizeRequests(authorize -> authorize
                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/token")).permitAll() // 토큰 재발급 URL은 인증 없이 접근 가능
                        //.requestMatchers(new MvcRequestMatcher(introspector, "/articles")).authenticated() // /articles에 대한 접근은 인증된 사용자에게만 허용
                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/**")).authenticated() // 나머지 API URL은 인증 필요
                        .anyRequest().permitAll())
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/articles")
                );


        http
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        // Authorization 요청과 관련된 상태 저장
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("articles/callback"))
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(myOAuth2UserService)));


        http
                .logout((logout) -> logout.logoutSuccessUrl("/login"));


        return http.build();
    }




    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

