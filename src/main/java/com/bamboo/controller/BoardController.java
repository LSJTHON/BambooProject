package com.bamboo.controller;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.BoardFormDto;
import com.bamboo.service.BoardServiceHwang;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardServiceHwang boardService;

    //  게시글 등록 페이지
    @GetMapping(value = "/user/board/new")
    public String boardForm(Model model) {
        Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = kakaAuthentication.getPrincipal();
        Boolean loginType2 = (principal instanceof OAuth2User);
        model.addAttribute("loginType", loginType2);
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "board/boardForm";
    }

    //  게시글 등록 요청
    @PostMapping(value = "/user/board/new")
    public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model,
                           @RequestParam("boardFile") List<MultipartFile> boardFiles) {
        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }
        Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = kakaAuthentication.getPrincipal();

        try {
            //  일반 로그인일 경우
                if (principal instanceof OAuth2User) {
                    OAuth2User oauth2User = (OAuth2User) principal;
                    Map<String, Object> attributes = oauth2User.getAttributes();
                    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                    String email = (String) kakaoAccount.get("email");
                    boardService.saveBoard(email, boardFormDto, boardFiles);
                }else{  //  카카오 로그인일 경우
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    Authentication authentication = securityContext.getAuthentication();
                    String email = authentication.getName();
                    boardService.saveBoard(email, boardFormDto, boardFiles);
                }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 등록 중 에러가 발생하였습니다.");
            return "board/boardForm";
        }

        return "redirect:/";
    }

    //  게시글 수정 페이지
    @GetMapping(value = "/user/board/{boardId}")
    public String boardForm(@PathVariable("boardId") Long boardId, Model model) {
        try {
            Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = kakaAuthentication.getPrincipal();
            Boolean loginType2 = (principal instanceof OAuth2User);
            model.addAttribute("loginType", loginType2);

            BoardFormDto boardFormDto = boardService.getBoardForm(boardId); // 수정 페이지에 필요한 데이터를 가져오기 위해 서비스 호출
            model.addAttribute("boardFormDto", boardFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "없는 글입니다");
            model.addAttribute("boardFormDto", new BoardFormDto());
            return "board/boardForm";
        }

        return "board/boardForm";
    }

    //  게시글 수정 요청
    @PostMapping(value = "/user/board/update/{boardId}")
    public String boardUpdate(@PathVariable("boardId") Long boardId,
                              @Valid BoardFormDto boardFormDto,
                              BindingResult bindingResult,
                              Model model,
                              @RequestParam("boardFile") List<MultipartFile> boardFileList) {
        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }
        try {
            boardService.updateBoard(boardFormDto, boardFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "글 수정 중 에러: " + e.getMessage());
            return "board/boardForm";
        }
        return "redirect:/";
    }
    

}
