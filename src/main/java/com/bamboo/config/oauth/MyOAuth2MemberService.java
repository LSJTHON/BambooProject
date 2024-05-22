package com.bamboo.config.oauth;


import com.bamboo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MyOAuth2MemberService extends DefaultOAuth2UserService {
    private final MemberService memberService;
    public static String loginType = null;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));

        }catch (Exception e) {
            e.printStackTrace();
        }
        loginType = "kakao";

        Map<String,String> responseMap = (Map<String,String>) oAuth2User.getAttributes().get("kakao_account");

        String userEmail =responseMap.get("email");

        Map<String,String> properties = (Map<String,String>) oAuth2User.getAttributes().get("properties");
        String userNickname = properties.get("nickname");

        memberService.kakaoSave(userEmail,userNickname);


        // 사용자 속성 로드
        Map<String, Object> attributes = oAuth2User.getAttributes();

        //관리자 권한을 받을 계정의 id값(키값) : 3484473887 (dltjdwhd258@nate.com 의 키값)

        String userId = String.valueOf(attributes.get("id"));

        // 기본 권한 설정
        Set<GrantedAuthority> authorities;

        if("3484473887".equals(userId)){
            authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else{
            authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // 사용자 권한 설정
        return new DefaultOAuth2User(authorities, attributes, "id");
    }
}
