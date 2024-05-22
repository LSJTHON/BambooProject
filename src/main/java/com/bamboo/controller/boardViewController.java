package com.bamboo.controller;


import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.entity.FileConfig;
import com.bamboo.service.MultipartConfigService;
import com.bamboo.service.fileAllowedService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class boardViewController {


    @Autowired
    private MultipartConfigService multipartConfigService;

    private final fileAllowedService fileAllowedService;

    //파일 제한자에 보여질 정보들을 return 해주는 html 에 model 로 쏴줌
    @GetMapping("/testAllowed")
    public String getPorts(Model model){

        FileConfig maxFile = fileAllowedService.findById(1L);

        multipartConfigService.updateMaxUploadSize(maxFile.getMaxFileSize(), 300L);

        FileConfig fileConfig = fileAllowedService.findById(1L);
        String extensions = fileAllowedService.getExtensionsByFileConfigId(1L);

        model.addAttribute("extensions", extensions);
        model.addAttribute("allowed",fileConfig);
        model.addAttribute("loginType",MyOAuth2MemberService.loginType);

        return "fileAllowed";
    }

}
