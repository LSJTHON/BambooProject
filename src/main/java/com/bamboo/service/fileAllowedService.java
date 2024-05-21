package com.bamboo.service;

import com.bamboo.dto.updateFileAllowedRequest;
import com.bamboo.entity.AllowedFileExtensions;
import com.bamboo.entity.FileConfig;
import com.bamboo.repository.AllowedFileExtensionsRepository;
import com.bamboo.repository.fileAllowedRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class fileAllowedService {

    private final AllowedFileExtensionsRepository fileExtensionsRepository;
    private final fileAllowedRepository fileAllowedRepository;

    public FileConfig findById(long id){
        return fileAllowedRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("findById 할 수 없습니다."));
    }

    public String getExtensionsByFileConfigId(long fileConfigId) {
        return fileExtensionsRepository.findExtensionsByFileConfigId(fileConfigId);
    }
    @Transactional
    public FileConfig update(updateFileAllowedRequest request){
        //파일 확장자를 전부 삭제하고 다시 save 위함

        fileExtensionsRepository.deleteExtensions();


        FileConfig fileConfig = fileAllowedRepository.findById(1L)
                .orElseThrow(()->new IllegalArgumentException("파일의 개수와 크기를 업데이트 할 수 없습니다."));

        //Http에서 받아오고 Dto에 저장된 값을 DB에 업데이트 시켜줌
        fileConfig.update(request.getMaxFileCount(),request.getMaxFileSize());


        List<String> requestExtensions2 = request.getExtensions();

        for(int i=0; i< requestExtensions2.size();i++){

            String extension = requestExtensions2.get(i); // 확장자 문자열 가져오기
            // "[]"를 제거하여 사용
            extension = extension.replaceAll("\\[|\\]", "");
            System.out.println(extension);
            request.setExtensions(Collections.singletonList(extension));

            //fileExtensionsRepository.save(request.toEntity());
            fileExtensionsRepository.save(
                    AllowedFileExtensions.builder()
                            .extension(extension)
                            .build());
        }

        fileExtensionsRepository.updateConfigId();
        return fileConfig;
    }
}
