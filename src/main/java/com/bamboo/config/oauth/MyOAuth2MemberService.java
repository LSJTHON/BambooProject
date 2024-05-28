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

import java.util.*;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        //loginType = "kakao";

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        Map<String, String> responseMap = (Map<String, String>) attributes.get("kakao_account");

        Map<String, String> properties = (Map<String, String>) attributes.get("properties");
        String userNickname = properties.get("nickname");


        // 관리자의 키값
        String userId = String.valueOf(attributes.get("id"));
        // 기본 권한 설정
        Set<GrantedAuthority> authorities;
        if ("3484473887".equals(userId)) {
            memberService.kakaoSave(responseMap.get("email"), userNickname,"ADMIN");
        } else if ("3493271205".equals(userId)) {
            memberService.kakaoSave(responseMap.get("email"), userNickname,"ADMIN");
        }else {
            memberService.kakaoSave(responseMap.get("email"), userNickname,"USER");
        }

        if (memberService.isUserDeleted(responseMap.get("email"))) {
            // 모든 권한 제거
            //loginType = null;
            System.out.println("이용 불가능한 사용자입니다...3473275983257982357932758325789237598235798237러ㅏ인롸ㅓㄴㅇ로ㅑㅓ로ㅑㄷㅈ롣ㅈ로ㅑ젿로ㅓㅐㅑ젿ㄹ");
            authorities = new HashSet<>(oAuth2User.getAuthorities());
            authorities.removeAll(authorities); // 모든 권한 제거
            throw new OAuth2AuthenticationException("현재 로그인 시도한 계정은 영구 정지 계정입니다.");
        }else{
            System.out.println("이용 가능한 사용자입니다...3473275983257982357932758325789237598235798237러ㅏ인롸ㅓㄴㅇ로ㅑㅓ로ㅑㄷㅈ롣ㅈ로ㅑ젿로ㅓㅐㅑ젿ㄹ");
            if ("3484473887".equals(userId)) {
                authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }else if("3493271205".equals(userId)){
            authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }else {
                authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
            }
        }


        // 사용자 권한 설정
        attributes.put("email", responseMap.get("email")); // 이메일 추가

        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}

