package com.bamboo.controller;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.*;
import com.bamboo.entity.Board;
import com.bamboo.service.BoardServiceHwang;
import com.bamboo.service.ReReplyService;
import com.bamboo.service.ReplyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardControllerHwang {

    private final BoardServiceHwang boardService;
    private final ReplyService replyService;
    private final ReReplyService reReplyService;

    //  게시글 상세 조회
    @GetMapping(value = "/boards/{boardId}")
    public String boardDtl(@PathVariable("boardId") Long boardId, Model model) {
        Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = kakaAuthentication.getPrincipal();

        Boolean loginType = (principal instanceof OAuth2User);
        try {
            boardService.setHit(boardId);
            BoardDto boardDto = boardService.getBoardDtl(boardId);
            model.addAttribute("boardDto", boardDto);

            List<String> hashtags = boardService.getHashtags(boardId);
            model.addAttribute("hashtags", hashtags);

            List<ReplyDto> replyDtos = replyService.getReplyList(boardId);
            List<ReReplyDto> reReplyDtos = reReplyService.getReReplyList(boardId);
            model.addAttribute("replys", replyDtos);
            model.addAttribute("rereplys", reReplyDtos);
            model.addAttribute("boardId", boardId);
            model.addAttribute("replyFormDto", new ReplyFormDto());
            model.addAttribute("loginType", loginType);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 게시글 입니다.");
            model.addAttribute("boardDto", new BoardDto());
            model.addAttribute("hashtags", new ArrayList<>());
            model.addAttribute("replys", new ArrayList<>());
            model.addAttribute("rereplys", new ArrayList<>());
            model.addAttribute("boardId", boardId);
            model.addAttribute("replyFormDto", new ReplyFormDto());
            model.addAttribute("loginType", loginType);
            return "board/boardDtl";
        }
        return "board/boardDtl";
    }

    //  게시글 삭제 post 요청
    @PostMapping(value = "/boards/delete")
    public String boardDelete(@RequestParam("boardId") Long boardId, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("게시글 삭제 시작");
            boardService.cancelBoard(boardId);
            // 삭제 성공 시
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 성공적으로 삭제되었습니다.");
            return "redirect:/";
        } catch (Exception e) {
            // 삭제 실패 시
            System.out.println("삭제 에러 메시지:" + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "redirect:/boards/" + boardId;
        }
    }

    //  게시글 좋아요 get 요청
    @GetMapping(value = "/boards/good/{boardId}")
    public String boardGood(@PathVariable("boardId") Long boardId, RedirectAttributes redirectAttributes, Model model) {
        try {
            System.out.println("좋아요 시작");


            Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = kakaAuthentication.getPrincipal();



            if (!(principal instanceof OAuth2User)) {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = securityContext.getAuthentication();
                String email = authentication.getName();
                Boolean isTrue = boardService.setGood(boardId, email);
                if (!isTrue) {
                    System.out.println("좋아요는 한 게시글당 한 번만 누를 수 있습니다.");
                    redirectAttributes.addFlashAttribute("errorMessage", "좋아요는 한 게시글당 한 번만 누를 수 있습니다.");
                }
            } else {
                OAuth2User oauth2User = (OAuth2User) principal;
                Map<String, Object> attributes = oauth2User.getAttributes();
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                String email = (String) kakaoAccount.get("email");

                Boolean isTrue = boardService.setGood(boardId, email);
                if (!isTrue) {
                    System.out.println("좋아요는 한 게시글당 한 번만 누를 수 있습니다.");
                    redirectAttributes.addFlashAttribute("errorMessage", "좋아요는 한 게시글당 한 번만 누를 수 있습니다.");
                }
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "좋아요 에러 발생: 게시글을 찾을 수 없습니다.");
            model.addAttribute("boardDto", new BoardDto());
            return "board/boardDtl";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "좋아요 에러 발생");
            model.addAttribute("boardDto", new BoardDto());
            return "board/boardDtl";
        }
        return "redirect:/boards/" + boardId;
    }

    //  댓글 등록 post 요청
    @PostMapping(value = "/boards/comments/new")
    public String replyNew(@Valid ReplyFormDto replyFormDto, @RequestParam("boardId") Long boardId, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("댓글 등록 bindingResult.hasErrors() 오류 발생");
            return "redirect:/boards/" + boardId;
        }
        try {
            System.out.println("댓글 등록 시작");

            Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = kakaAuthentication.getPrincipal();


            if (!(principal instanceof OAuth2User)) {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = securityContext.getAuthentication();
                String email = authentication.getName();
                replyService.saveReply(email, boardId, replyFormDto);
            } else {
                OAuth2User oauth2User = (OAuth2User) principal;
                Map<String, Object> attributes = oauth2User.getAttributes();
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                String email = (String) kakaoAccount.get("email");
                replyService.saveReply(email, boardId, replyFormDto);
            }
        } catch (Exception e) {
            System.out.println("에러 메시지:" + e.getMessage());
            model.addAttribute("errorMessage", "댓글 등록 중 에러가 발생하였습니다.");
            return "redirect:/boards/" + boardId;
        }

        return "redirect:/boards/" + boardId;
    }

    // 댓글 삭제 post 요청
    @PostMapping(value = "/boards/comments/delete")
    public String replyDelete(@RequestParam("replyId") Long replyId, @RequestParam("boardId") Long boardId, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("댓글 삭제 시작");
            replyService.cancelReply(replyId);
            // 삭제 성공 시
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 삭제되었습니다.");
            return "redirect:/boards/" + boardId;
        } catch (Exception e) {
            // 삭제 실패 시
            System.out.println("삭제 에러 메시지:" + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "redirect:/boards/" + boardId;
        }
    }

    //  대댓글 등록 post 요청
    @PostMapping(value = "/boards/comments/new/rereply")
    public String reReplyNew(@Valid ReplyFormDto replyFormDto, @RequestParam("boardId") Long boardId, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("대댓글 등록 bindingResult.hasErrors() 오류 발생");
            return "redirect:/boards/" + boardId;
        }
        try {
            Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = kakaAuthentication.getPrincipal();


            System.out.println("대댓글 등록 시작");
            if (!(principal instanceof OAuth2User)) {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = securityContext.getAuthentication();
                String email = authentication.getName();
                reReplyService.saveReReply(email, boardId, replyFormDto);
            } else {
                OAuth2User oauth2User = (OAuth2User) principal;
                Map<String, Object> attributes = oauth2User.getAttributes();
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                String email = (String) kakaoAccount.get("email");
                reReplyService.saveReReply(email, boardId, replyFormDto);
            }
        } catch (Exception e) {
            System.out.println("댓글 삭제 에러 메시지:" + e.getMessage());
            model.addAttribute("errorMessage", "대댓글 등록 중 에러가 발생하였습니다.");
            return "redirect:/boards/" + boardId;
        }

        return "redirect:/boards/" + boardId;
    }

    //  대댓글 삭제 post 요청
    @PostMapping(value = "/boards/comments/delete/rereply")
    public String reReplyDelete(@RequestParam("rereplyId") Long reReplyId, @RequestParam("boardId") Long boardId, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("대댓글 삭제 시작");
            reReplyService.cancelReReply(reReplyId);
            // 삭제 성공 시
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 삭제되었습니다.");
            return "redirect:/boards/" + boardId;
        } catch (Exception e) {
            // 삭제 실패 시
            System.out.println("대댓글 삭제 에러 메시지:" + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "대댓글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "redirect:/boards/" + boardId;
        }
    }

    //  삭제 목록 관리 페이지
    @GetMapping(value = {"/deleted", "/deleted/{page}"})
    public String deletedBoardPage(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {


        Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = kakaAuthentication.getPrincipal();
        Boolean loginType2 = (principal instanceof OAuth2User);


        Pageable pageable;
        Page<Board> boards;

        pageable = PageRequest.of(page.orElse(0), 10);
        boards = boardService.getDeletedBoardPage(boardSearchDto, pageable);

        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 10);
        model.addAttribute("loginType", loginType2);

        return "board/deletedBoards";
    }

    //  복원 post 요청
    @PostMapping(value = "/deleted/restore")
    public String restoreBoard(@RequestParam("boardId") Long boardId, RedirectAttributes redirectAttributes){
        try {
            System.out.println("게시글 복원 시작");
            boardService.restoreBoard(boardId);
            // 복원 성공 시
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 성공적으로 복원되었습니다.");
            return "redirect:/deleted";
        } catch (Exception e) {
            // 삭제 실패 시
            System.out.println("게시글 복원 에러 메시지:" + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 복원 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "redirect:/deleted";
        }
    }
}
