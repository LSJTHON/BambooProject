package com.bamboo.service;

import com.bamboo.entity.BoardFile;
import com.bamboo.repository.BoardFileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardFileService {

    @Value("${portfolioImgLocation}")
    private String boardFileLocation;

    private final BoardFileRepository boardFileRepository;

    private final FileService fileService;

    public void saveBoardFile(BoardFile boardFile, MultipartFile boardFileData) throws Exception {
        String oriFileName = boardFileData.getOriginalFilename();
        String fileName = "";
        String fileUrl = "";

        if (!StringUtils.isEmpty(oriFileName)) {
            fileName = fileService.uploadFile(boardFileLocation, oriFileName, boardFileData.getBytes());
            fileUrl = "/images/item/" + fileName;
        }

        boardFile.updateBoardFile(oriFileName, fileName, fileUrl);
        boardFileRepository.save(boardFile);
    }

    public void updateBoardFile(Long boardFileId, MultipartFile boardFileData) throws Exception{
        if(!boardFileData.isEmpty()){
            System.out.println("boardFile의 id: "+boardFileId);
            BoardFile boardFile = boardFileRepository.findById(boardFileId)
                    .orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(boardFile.getFileName())){
                fileService.deleteFile(boardFileLocation+"/"+boardFile.getFileName());
            }

            String oriFileName = boardFileData.getOriginalFilename();
            String fileName = fileService.uploadFile(boardFileLocation, oriFileName, boardFileData.getBytes());
            String fileUrl = "/images/item/" + fileName;
            boardFile.updateBoardFile(oriFileName, fileName, fileUrl);
        }else {
            System.out.println("boardFileData가 null입니다.");
        }
    }
}
