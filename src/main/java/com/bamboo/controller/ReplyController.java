package com.bamboo.controller;

import com.bamboo.dto.ReReplyDto;
import com.bamboo.dto.ReplyDto;
import com.bamboo.dto.ReplyFormDto;
import com.bamboo.service.ReReplyService;
import com.bamboo.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/comments")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    private final ReReplyService reReplyService;

    //  댓글, 대댓글 컴포넌트 불러오기
    @GetMapping(value = "/test/{boardId}")
    public String test(Model model, @PathVariable("boardId") Long boardId){
        List<ReplyDto> replyDtos = replyService.getReplyList(0L);
        List<ReReplyDto> reReplyDtos = reReplyService.getReReplyList(0L);
        model.addAttribute("replys", replyDtos);
        model.addAttribute("rereplys", reReplyDtos);
        model.addAttribute("boardId",boardId);
        model.addAttribute("replyFormDto", new ReplyFormDto());
        return "reply/test";
    }

    //  댓글 등록하기
    @PostMapping(value = "/new")
    public String replyNew(@Valid ReplyFormDto replyFormDto, @RequestParam("boardId") Long boardId, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "reply/test";
        }

        try{
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            String email = authentication.getName();

            System.out.println("댓글 등록 시작");
//            로그인 정보 가져와서 댓글 등록
//            replyService.saveReply(email, boardId, replyFormDto);
            replyService.saveReply("test@test.com", boardId, replyFormDto);
        }catch (Exception e) {
            System.out.println("에러 메시지:"+e.getMessage());
            model.addAttribute("errorMessage", "댓글 등록 중 에러가 발생하였습니다.");
            return "reply/test";
        }

        return "redirect:/comments/test/" + boardId;
    }

    // 댓글 삭제하기
    @PostMapping(value = "/delete")
    public String replyDelete(@RequestParam("replyId") Long replyId, @RequestParam("boardId") Long boardId, RedirectAttributes redirectAttributes){
        try {
            System.out.println("댓글 삭제 시작");
            replyService.cancelReply(replyId);
            // 삭제 성공 시
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 삭제되었습니다.");
            return "redirect:/comments/test/" + boardId;
        } catch (Exception e) {
            // 삭제 실패 시
            System.out.println("삭제 에러 메시지:"+e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "reply/test"+ boardId;
        }
    }


    //  대댓글 등록하기
    @PostMapping(value = "/new/rereply")
    public String reReplyNew(@Valid ReplyFormDto replyFormDto, @RequestParam("boardId") Long boardId, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "reply/test";
        }

        try{
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            String email = authentication.getName();

            System.out.println("대댓글 등록 시작");
//            로그인 정보 가져와서 대댓글 등록
//            reReplyService.saveReReply(email, boardId, replyFormDto);
            reReplyService.saveReReply("test@test.com", boardId, replyFormDto);
        }catch (Exception e) {
            System.out.println("댓글 삭제 에러 메시지:"+e.getMessage());
            model.addAttribute("errorMessage", "대댓글 등록 중 에러가 발생하였습니다.");
            return "reply/test";
        }

        return "redirect:/comments/test/" + boardId;
    }

    //  대댓글 삭제하기
    @PostMapping(value = "/delete/rereply")
    public String reReplyDelete(@RequestParam("rereplyId") Long reReplyId, @RequestParam("boardId") Long boardId, RedirectAttributes redirectAttributes){
        try {
            System.out.println("댓글 삭제 시작");
            reReplyService.cancelReReply(reReplyId);
            // 삭제 성공 시
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 삭제되었습니다.");
            return "redirect:/comments/test/" + boardId;
        } catch (Exception e) {
            // 삭제 실패 시
            System.out.println("대댓글 삭제 에러 메시지:"+e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "대댓글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "reply/test";
        }
    }
}
