package com.bamboo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@Controller
public class BlogViewController {


    @GetMapping("/")
    public String getPage(){

        return "articleList";
    }

    @GetMapping("/1")
    public String getPage2(){

        return "testHtml";
    }

    @GetMapping("/3")
    public String getPage3(){

        return "testHtml";
    }

}
