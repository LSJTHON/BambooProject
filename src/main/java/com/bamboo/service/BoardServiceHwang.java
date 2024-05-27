package com.bamboo.service;

import com.bamboo.dto.*;
import com.bamboo.entity.*;
import com.bamboo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceHwang {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardMemberMapRepository boardMemberMapRepository;
    private final BoardHashtagMapRepository boardHashtagMapRepository;
    private final HashTagRepository hashtagRepository;
    private final BoardFileService boardFileService;
    @Transactional(readOnly = true)
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardRepository.getBoardPage(boardSearchDto, pageable);
    }
    @Transactional(readOnly = true)
    public Page<Board> getSortedBoardPage(BoardSearchDto boardSearchDto, Pageable pageable, String sort) {
        return boardRepository.getSortedBoardPage(boardSearchDto, pageable, sort);
    }
    @Transactional(readOnly = true)
    public Page<Board> getDeletedBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardRepository.getDeletedBoardPage(boardSearchDto, pageable);
    }
    public Long saveBoard(String email, BoardFormDto boardFormDto, List<MultipartFile> boardFiles) throws Exception {
        Board board = boardFormDto.createBoard();
        board.setDeleted(false);
        Member member = memberRepository.findById(email)
                .orElseThrow(EntityNotFoundException::new);
        board.setMember(member);
        boardRepository.save(board);
        // 파일 저장
        if (boardFiles != null && !boardFiles.isEmpty()) {
            for (MultipartFile multipartFile : boardFiles) {
                System.out.println("파일 이름: "+multipartFile.getOriginalFilename());
                if (!multipartFile.isEmpty()) {
                    BoardFile boardFile = new BoardFile();
                    boardFile.setBoard(board);
                    boardFileService.saveBoardFile(boardFile, multipartFile); // BoardFileService를 사용하여 파일 저장
                }
            }
        }
        // 해시태그 저장 및 매핑
        if (boardFormDto.getHashtag() != null && !boardFormDto.getHashtag().isEmpty()) {
            String[] hashtags = boardFormDto.getHashtag().split(" ");
            for (String tagName : hashtags) {
                String trimmedTagName = tagName.trim();
                if (!trimmedTagName.isEmpty()) {
                    Hashtag hashtag = hashtagRepository.findByName(trimmedTagName).orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setName(trimmedTagName);
                        return newHashtag;
                    });
                    hashtagRepository.save(hashtag);
                    BoardHashtagMap boardHashtagMap = new BoardHashtagMap();
                    boardHashtagMap.setBoard(board);
                    boardHashtagMap.setHashtag(hashtag);
                    boardHashtagMapRepository.save(boardHashtagMap);
                }
            }
        }
        return board.getId();
    }
    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardFileList) throws Exception {
        Board board = boardRepository.findById(boardFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);
        List<Long> boardFileIds = boardFormDto.getBoardFileIds();
        // 파일 ID 리스트와 파일 리스트 크기 일치 확인
        if (boardFileIds != null && boardFileIds.size() == boardFileList.size()) {
            for (int i = 0; i < boardFileList.size(); i++) {
                boardFileService.updateBoardFile(boardFileIds.get(i), boardFileList.get(i));
            }
        } else {
            // 기존 파일 삭제
            boardFileRepository.deleteByBoard(board);

            // 새 파일 추가
            if (boardFileList != null && !boardFileList.isEmpty()) {
                for (MultipartFile multipartFile : boardFileList) {
                    if (!multipartFile.isEmpty()) {
                        BoardFile boardFile = new BoardFile();
                        boardFile.setBoard(board);
                        boardFileService.saveBoardFile(boardFile, multipartFile);
                    }
                }
            }
        }
        // 기존 해시태그 매핑 삭제
        boardHashtagMapRepository.deleteByBoard(board);
        // 새로운 해시태그 저장 및 매핑
        if (boardFormDto.getHashtag() != null && !boardFormDto.getHashtag().isEmpty()) {
            String[] hashtags = boardFormDto.getHashtag().split(" ");
            for (String tagName : hashtags) {
                System.out.println("해시태그 이름은?: "+tagName);
                String trimmedTagName = tagName.trim();
                if (!trimmedTagName.isEmpty()) {
                    Hashtag hashtag = hashtagRepository.findByName(trimmedTagName).orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setName(trimmedTagName);
                        return newHashtag;
                    });
                    hashtagRepository.save(hashtag);
                    BoardHashtagMap boardHashtagMap = new BoardHashtagMap();
                    boardHashtagMap.setBoard(board);
                    boardHashtagMap.setHashtag(hashtag);
                    boardHashtagMapRepository.save(boardHashtagMap);
                }
            }
        }
        return board.getId();
    }
    @Transactional(readOnly = true)
    public BoardFormDto getBoardForm(Long boardId) {
        List<BoardFile> boardFileList = boardFileRepository.findByBoardId(boardId);
        List<BoardFileDto> boardFileDtoList = new ArrayList<>();
        for (BoardFile boardFile : boardFileList) {
            BoardFileDto boardFileDto = BoardFileDto.of(boardFile);
            boardFileDtoList.add(boardFileDto);
        }
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        BoardFormDto boardFormDto = BoardFormDto.of(board);
        boardFormDto.setBoardFileDtoList(boardFileDtoList);
        // 해시태그 조회 및 설정
        List<BoardHashtagMap> boardHashtagMaps = boardHashtagMapRepository.findByBoard(board);
        List<HashtagDto> hashtagDtoList = new ArrayList<>();
        List<String> hashtags = new ArrayList<>();
        for (BoardHashtagMap boardHashtagMap : boardHashtagMaps) {
            HashtagDto hashtagDto = new HashtagDto();
            hashtagDto.setTagName(boardHashtagMap.getHashtag().getName());
            hashtagDtoList.add(hashtagDto);
            hashtags.add(boardHashtagMap.getHashtag().getName());
        }
        boardFormDto.setHashtagDtoList(hashtagDtoList);
        boardFormDto.setHashtag(String.join(" ", hashtags)); // 해시태그 문자열 설정
        // 디버그 로그 추가
        System.out.println("Hashtags: " + String.join(", ", hashtags));
        return boardFormDto;
    }
    @Transactional(readOnly = true)
    public BoardDto getBoardDtl(Long boardId){
        List<BoardFile> boardFileList = boardFileRepository.findAllByBoardIdOrderByIdAsc(boardId);
        List<BoardFileDto> boardFileDtoList = new ArrayList<>();
        for (BoardFile boardFile : boardFileList){
            BoardFileDto boardFileDto = BoardFileDto.of(boardFile);
            boardFileDtoList.add(boardFileDto);
        }
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        BoardDto boardDto = BoardDto.of(board);
        boardDto.setBoardFileDtoList(boardFileDtoList);
        Member member = board.getMember();
        boardDto.setMember(member);
        return boardDto;
    }

    public List<String> getHashtags(Long boardId){
        List<Hashtag> hashtagList = boardHashtagMapRepository.findHashtagsByBoardId(boardId);
        List<String> hashtagNames = new ArrayList<>();
        for (Hashtag hashtag: hashtagList){
            String hashtagName = hashtag.getName();
            hashtagNames.add(hashtagName);
        }
        return hashtagNames;
    }

    public void setHit(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        board.setHit(board.getHit()+1);
        boardRepository.save(board);
    }

    public boolean setGood(Long boardId, String email) {
        // 이미 좋아요 눌렀는지 확인
        boolean exists = boardMemberMapRepository.existsByBoardIdAndMemberEmail(boardId, email);
        if (exists) {
            // 레코드가 존재할 때의 로직
            return false;
        } else {
            // 레코드가 존재하지 않을 때의 로직
            // 매핑 테이블 생성
            BoardMemberMap like = new BoardMemberMap();
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            Member member = memberRepository.findById(email)
                    .orElseThrow(EntityNotFoundException::new);
            like.setBoard(board);
            like.setMember(member);
            boardMemberMapRepository.save(like);

            // 좋아요 수 증가
            board.setGood(board.getGood() + 1);
            boardRepository.save(board);
            System.out.println("좋아요 수: "+board.getGood());

            return true;
        }
    }
    public void cancelBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        board.setDeleted(true);
        boardRepository.save(board);
    }
    public void restoreBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        board.setDeleted(false);
        board.setRestored(true);
        System.out.println("삭제여부: "+board.isDeleted());
        System.out.println("복원여부: "+board.isRestored());
        boardRepository.save(board);
    }

}