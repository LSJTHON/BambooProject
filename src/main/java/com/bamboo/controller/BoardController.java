package com.bamboo.controller;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.BoardDto;
import com.bamboo.dto.ReReplyDto;
import com.bamboo.dto.ReplyDto;
import com.bamboo.dto.ReplyFormDto;
import com.bamboo.service.BoardService;
import com.bamboo.service.ReReplyService;
import com.bamboo.service.ReplyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private final ReReplyService reReplyService;

    //  게시글 상세 조회
    @GetMapping(value = "/boards/{boardId}")
    public String boardDtl(@PathVariable("boardId") Long boardId, Model model){
        try{
            boardService.setHit(boardId);
            BoardDto boardDto = boardService.getBoardDtl(boardId);
            model.addAttribute("boardDto", boardDto);

            List<String> hashtags = boardService.getHashtags(boardId);
            model.addAttribute("hashtags", hashtags);

            List<ReplyDto> replyDtos = replyService.getReplyList(boardId);
            List<ReReplyDto> reReplyDtos = reReplyService.getReReplyList(boardId);
            model.addAttribute("replys", replyDtos);
            model.addAttribute("rereplys", reReplyDtos);
            model.addAttribute("boardId",boardId);
            model.addAttribute("replyFormDto", new ReplyFormDto());
            model.addAttribute("loginType", MyOAuth2MemberService.loginType);
        }catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 게시글 입니다.");
            model.addAttribute("boardDto", new BoardDto());
            model.addAttribute("hashtags", new ArrayList<>());
            model.addAttribute("replys", new ArrayList<>());
            model.addAttribute("rereplys", new ArrayList<>());
            model.addAttribute("boardId", boardId);
            model.addAttribute("replyFormDto", new ReplyFormDto());
            model.addAttribute("loginType", MyOAuth2MemberService.loginType);
            return "board/boardDtl";
        }
        return "board/boardDtl";
    }

    //  게시글 작성 페이지
    @GetMapping(value = "/boards/create")
    public String newBoard(Model model){
        model.addAttribute("boardDto", new BoardDto());
        model.addAttribute("loginType", MyOAuth2MemberService.loginType);
        return "board/boardForm";
    }

    //  게시글 등록 post 요청
    @PostMapping(value = "/boards/create")
    public String boardNew(BoardDto boardDto, BindingResult bindingResult,
                           Model model, @RequestParam("boardFiles") List<MultipartFile> boardFileList){
        if(bindingResult.hasErrors()){
            return "board/boardForm";
        }
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            String email = authentication.getName();
            boardService.saveBoard(email, boardDto, boardFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "게시글 등록 중 에러가 발생하였습니다.");
            return "board/boardForm";
        }

        return "redirect:/";
    }

    //  게시글 삭제 post 요청
    @PostMapping(value = "/boards/delete")
    public String boardDelete(@RequestParam("boardId") Long boardId, RedirectAttributes redirectAttributes){
        try {
            System.out.println("게시글 삭제 시작");
            boardService.cancelBoard(boardId);
            // 삭제 성공 시
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 성공적으로 삭제되었습니다.");
            return "redirect:/";
        } catch (Exception e) {
            // 삭제 실패 시
            System.out.println("삭제 에러 메시지:"+e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "redirect:/boards/" + boardId;
        }
    }

    //  게시글 좋아요 get 요청
    @GetMapping(value = "/boards/good/{boardId}")
    public String boardGood(@PathVariable("boardId") Long boardId, RedirectAttributes redirectAttributes, Model model) {
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            String email = authentication.getName();
            System.out.println("좋아요 요청: " + boardId);
            Boolean isTrue = boardService.setGood(boardId, email);
            if (!isTrue){
                System.out.println("좋아요는 한 게시글당 한 번만 누를 수 있습니다.");
                redirectAttributes.addFlashAttribute("errorMessage", "좋아요는 한 게시글당 한 번만 누를 수 있습니다.");
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
            replyService.saveReply(email, boardId, replyFormDto);
        }catch (Exception e) {
            System.out.println("에러 메시지:"+e.getMessage());
            model.addAttribute("errorMessage", "댓글 등록 중 에러가 발생하였습니다.");
            return "reply/test";
        }

        return "redirect:/boards/" + boardId;
    }

    // 댓글 삭제 post 요청
    @PostMapping(value = "/boards/comments/delete")
    public String replyDelete(@RequestParam("replyId") Long replyId, @RequestParam("boardId") Long boardId, RedirectAttributes redirectAttributes){
        try {
            System.out.println("댓글 삭제 시작");
            replyService.cancelReply(replyId);
            // 삭제 성공 시
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 삭제되었습니다.");
            return "redirect:/boards/" + boardId;
        } catch (Exception e) {
            // 삭제 실패 시
            System.out.println("삭제 에러 메시지:"+e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "reply/test"+ boardId;
        }
    }

    //  대댓글 등록 post 요청
    @PostMapping(value = "/boards/comments/new/rereply")
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
            reReplyService.saveReReply(email, boardId, replyFormDto);
        }catch (Exception e) {
            System.out.println("댓글 삭제 에러 메시지:"+e.getMessage());
            model.addAttribute("errorMessage", "대댓글 등록 중 에러가 발생하였습니다.");
            return "reply/test";
        }

        return "redirect:/boards/" + boardId;
    }

    //  대댓글 삭제 post 요청
    @PostMapping(value = "/boards/comments/delete/rereply")
    public String reReplyDelete(@RequestParam("rereplyId") Long reReplyId, @RequestParam("boardId") Long boardId, RedirectAttributes redirectAttributes){
        try {
            System.out.println("댓글 삭제 시작");
            reReplyService.cancelReReply(reReplyId);
            // 삭제 성공 시
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 삭제되었습니다.");
            return "redirect:/boards/" + boardId;
        } catch (Exception e) {
            // 삭제 실패 시
            System.out.println("대댓글 삭제 에러 메시지:"+e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "대댓글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "reply/test";
        }
    }

}
