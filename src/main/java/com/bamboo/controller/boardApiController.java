package com.bamboo.controller;


import com.bamboo.dto.MemberDeleteDto;
import com.bamboo.dto.updateFileAllowedRequest;
import com.bamboo.entity.FileConfig;
import com.bamboo.entity.Member;
import com.bamboo.service.fileAllowedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class boardApiController {


    private final fileAllowedService fileAllowedService;

    @PutMapping("/modifyFileAllowed")
    public ResponseEntity<FileConfig> updateFileAllowed(@RequestBody updateFileAllowedRequest request) {

        FileConfig updatedFileConfig = fileAllowedService.update(request);

        return ResponseEntity.ok()
                .body(updatedFileConfig);
    }

}
