package com.bamboo.controller;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.dto.BoardSearchDto;
import com.bamboo.entity.Board;
import com.bamboo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardService boardService;

    //  글 목록 조회(메인페이지)
    @GetMapping(value = {"/", "/{page}"})
    public String boards(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page,
                         @RequestParam(value = "sort", required = false) String sort,
                         Model model) {
        Pageable pageable;
        Page<Board> boards;

        if (sort != null && !sort.isEmpty()) {
            pageable = PageRequest.of(page.orElse(0), 10, Sort.by(sort));
            boards = boardService.getSortedBoardPage(boardSearchDto, pageable, sort);
        } else {
            pageable = PageRequest.of(page.orElse(0), 10);
            boards = boardService.getBoardPage(boardSearchDto, pageable);
        }

        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 10);
        model.addAttribute("loginType", MyOAuth2MemberService.loginType);

        return "board/boards";
    }

}
