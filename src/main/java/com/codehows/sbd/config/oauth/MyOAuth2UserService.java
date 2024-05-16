package com.codehows.sbd.config.oauth;


import com.codehows.sbd.domain.User;
import com.codehows.sbd.repository.UserRepository;
import com.codehows.sbd.service.UserService;
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
public class MyOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        System.out.println("키햐오오오오");
        System.out.println(userRequest.getAccessToken()+"들어오나");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));

        }catch (Exception e) {
            e.printStackTrace();
        }


        String userId ="kakao_"+ oAuth2User.getAttributes().get("id");

        Map<String,String> responseMap = (Map<String,String>) oAuth2User.getAttributes().get("kakao_account");

        String userEmail =responseMap.get("email");

        Map<String,String> properties = (Map<String,String>) oAuth2User.getAttributes().get("properties");
        String userNickname = properties.get("nickname");

        System.out.println("유저 이메일 : "+userEmail);
        System.out.println("유저 닉네임 : "+userNickname);

        userService.kakaoSave(userEmail,userNickname);

        return oAuth2User;
    }
}
