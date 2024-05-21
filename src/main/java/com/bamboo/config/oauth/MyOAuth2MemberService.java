package com.bamboo.config.oauth;


import com.bamboo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

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

        return oAuth2User;
    }
}
