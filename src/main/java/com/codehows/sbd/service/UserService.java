package com.codehows.sbd.service;


import com.codehows.sbd.domain.User;
import com.codehows.sbd.dto.AddUserRequest;
import com.codehows.sbd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto){
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                //패스워드 암호화
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }


    public User kakaoSave(String email, String nickname){
        User user = userRepository.findByEmail(email)
                .orElse(User.builder()
                        .email(email)
                        .nickname(nickname)
                        .build());
        return userRepository.save(user);
    }


}
