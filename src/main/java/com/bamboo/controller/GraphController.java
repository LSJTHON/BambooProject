package com.bamboo.controller;

import com.bamboo.entity.Board;
import com.bamboo.entity.Member;
import com.bamboo.entity.Reply;
import com.bamboo.entity.ReReply;
import com.bamboo.entity.Visit;
import com.bamboo.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class GraphController {

    private final VisitService visitService;

    @GetMapping("/graph")
    public String showGraph(@RequestParam(required = false, defaultValue = "week") String period, Model model) {
        Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = kakaAuthentication.getPrincipal();
        Boolean loginType2 = (principal instanceof OAuth2User);
        model.addAttribute("loginType", loginType2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Visit> visits;
        List<Member> members;
        List<Board> boards;
        List<Reply> replies;
        List<ReReply> reReplies;

        switch (period) {
            case "month":
                visits = visitService.getLastMonthVisits();
                members = visitService.getLastMonthMembers();
                boards = visitService.getLastMonthBoards();
                replies = visitService.getLastMonthReplies();
                reReplies = visitService.getLastMonthReReplies();
                break;
            case "year":
                visits = visitService.getLastYearVisits();
                members = visitService.getLastYearMembers();
                boards = visitService.getLastYearBoards();
                replies = visitService.getLastYearReplies();
                reReplies = visitService.getLastYearReReplies();
                break;
            case "week":
            default:
                visits = visitService.getLastWeekVisits();
                members = visitService.getLastWeekMembers();
                boards = visitService.getLastWeekBoards();
                replies = visitService.getLastWeekReplies();
                reReplies = visitService.getLastWeekReReplies();
                break;
        }

        List<String> visitData = visits.stream()
                .map(visit -> String.format("{\"date\": \"%s\", \"count\": %d}", visit.getDate().format(formatter), visit.getVisitNum()))
                .collect(Collectors.toList());

        List<String> memberData = visitService.getMemberDataForGraph(members);
        List<String> postData = visitService.getPostDataForGraph(boards);
        List<String> replyData = visitService.getReplyDataForGraph(replies, reReplies);

        model.addAttribute("visitData", visitData.toString());
        model.addAttribute("memberData", memberData.toString());
        model.addAttribute("postData", postData.toString());
        model.addAttribute("replyData", replyData.toString());
        model.addAttribute("period", period);

        return "admin/graph";
    }
}
