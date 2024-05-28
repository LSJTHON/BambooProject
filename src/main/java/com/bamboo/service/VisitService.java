package com.bamboo.service;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.entity.*;
import com.bamboo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final ReReplyRepository reReplyRepository;

    private Set<String> visitedEmails = new HashSet<>();

    public void countVisit() {
        String email = getCurrentUserEmail();
        LocalDate today = LocalDate.now();

        if (visitedEmails.contains(email + today.toString())) {
            return;
        } else {
            visitedEmails.add(email + today.toString());
        }

        Optional<Visit> visitOptional = visitRepository.findById(today);
        Visit visit = visitOptional.orElseGet(() -> new Visit());
        visit.setDate(today);

        if (visit.getVisitNum() == null) {
            visit.setVisitNum(1L);
        } else {
            visit.setVisitNum(visit.getVisitNum() + 1);
        }

        visitRepository.save(visit);
    }

    private String getCurrentUserEmail() {
        String email;


        Authentication kakaAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = kakaAuthentication.getPrincipal();
        Boolean loginType2 = (principal instanceof OAuth2User);

        if (!(principal instanceof OAuth2User)) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            email = authentication.getName();
        } else {
            OAuth2User oauth2User = (OAuth2User) principal;
            Map<String, Object> attributes = oauth2User.getAttributes();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");
        }
        return email;
    }

    public List<Visit> getLastWeekVisits() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        return visitRepository.findByDateBetween(startDate, endDate);
    }

    public List<Visit> getLastMonthVisits() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        return visitRepository.findByDateBetween(startDate, endDate);
    }

    public List<Visit> getLastYearVisits() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);
        return visitRepository.findByDateBetween(startDate, endDate);
    }

    public List<Member> getLastWeekMembers() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(7);
        return memberRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<Member> getLastMonthMembers() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);
        return memberRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<Member> getLastYearMembers() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusYears(1);
        return memberRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<String> getMemberDataForGraph(List<Member> members) {
        return members.stream()
                .map(member -> String.format("{\"date\": \"%s\", \"count\": 1}", member.getRegTime().toLocalDate()))
                .collect(Collectors.toList());
    }

    public List<Board> getLastWeekBoards() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(7);
        return boardRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<Board> getLastMonthBoards() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);
        return boardRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<Board> getLastYearBoards() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusYears(1);
        return boardRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<Reply> getLastWeekReplies() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(7);
        return replyRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<Reply> getLastMonthReplies() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);
        return replyRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<Reply> getLastYearReplies() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusYears(1);
        return replyRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<ReReply> getLastWeekReReplies() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(7);
        return reReplyRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<ReReply> getLastMonthReReplies() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);
        return reReplyRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<ReReply> getLastYearReReplies() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusYears(1);
        return reReplyRepository.findByRegTimeBetween(startDate, endDate);
    }

    public List<String> getPostDataForGraph(List<Board> boards) {
        return boards.stream()
                .map(board -> String.format("{\"date\": \"%s\", \"count\": 1}", board.getRegTime().toLocalDate()))
                .collect(Collectors.toList());
    }

    public List<String> getReplyDataForGraph(List<Reply> replies, List<ReReply> reReplies) {
        List<String> replyData = replies.stream()
                .map(reply -> String.format("{\"date\": \"%s\", \"count\": 1}", reply.getRegTime().toLocalDate()))
                .collect(Collectors.toList());

        List<String> reReplyData = reReplies.stream()
                .map(reReply -> String.format("{\"date\": \"%s\", \"count\": 1}", reReply.getRegTime().toLocalDate()))
                .collect(Collectors.toList());

        replyData.addAll(reReplyData);
        return replyData;
    }
}
