package com.bamboo.repository;

import com.bamboo.dto.BoardSearchDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.QBoard;
import com.bamboo.entity.QBoardHashtagMap;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QBoard board = QBoard.board;
    private final QBoardHashtagMap boardHashtagMap = QBoardHashtagMap.boardHashtagMap;

    // 검색 조건 생성
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        BooleanExpression isNotDeleted = board.isDeleted.isFalse();

        if (searchQuery.startsWith("#")) {
            String result = searchQuery.replace(" ", "");
            String[] hashtagNames = result.split("#");
            BooleanExpression expression = null;
            for (String hashtagName : hashtagNames) {
                if (!hashtagName.isEmpty()) {
                    BooleanExpression hashtagExpression = boardHashtagMap.hashtag.name.eq(hashtagName.trim());
                    expression = (expression == null) ? hashtagExpression : expression.or(hashtagExpression);
                }
            }
            return expression != null ? expression.and(isNotDeleted) : isNotDeleted;
        } else if (StringUtils.equals("title", searchBy)) {
            return board.title.like("%" + searchQuery + "%").and(isNotDeleted);
        } else if (StringUtils.equals("content", searchBy)) {
            return board.content.like("%" + searchQuery + "%").and(isNotDeleted);
        } else if (StringUtils.equals("name", searchBy)) {
            return board.member.name.like("%" + searchQuery + "%").and(isNotDeleted);
        }
        return isNotDeleted;
    }

    @Override
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        BooleanExpression searchExpression = searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery());

        // 조건에 따라 검색한 글 목록 쿼리
        JPAQuery<Board> contentQuery = queryFactory
                .selectFrom(board)
                .leftJoin(boardHashtagMap).on(board.id.eq(boardHashtagMap.board.id))
                .where(searchExpression)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 전체 count 수 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(board.id.count())
                .from(board)
                .leftJoin(boardHashtagMap).on(board.id.eq(boardHashtagMap.board.id))
                .where(searchExpression);

        List<Board> content = contentQuery.fetch();
        long total = countQuery.fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<Board> getSortedBoardPage(BoardSearchDto boardSearchDto, Pageable pageable, String sortBy) {
        // 쿼리 표현식 정의
        BooleanExpression searchExpression = searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery());
        OrderSpecifier<?> orderSpecifier;

        // 정렬 속성을 결정하고 그에 따라 OrderSpecifier 생성
        switch (sortBy) {
            case "title":
                orderSpecifier = board.title.asc();
                break;
            case "member.name":
                orderSpecifier = board.member.name.asc();
                break;
            case "regTime":
                orderSpecifier = board.regTime.asc();
                break;
            default:
                // 유효한 속성이 제공되지 않은 경우 기본 정렬 방법
                orderSpecifier = board.id.desc();
                break;
        }

        // 조건에 따라 글 목록 쿼리 생성
        JPAQuery<Board> query = queryFactory
                .selectFrom(board)
                .leftJoin(boardHashtagMap).on(board.id.eq(boardHashtagMap.board.id))
                .where(searchExpression)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 쿼리문 및 총 개수 가져오기
        List<Board> content = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Board> getDeletedBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return null;
    }
}
