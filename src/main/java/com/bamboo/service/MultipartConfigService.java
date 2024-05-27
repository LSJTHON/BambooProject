package com.bamboo.service;

import jakarta.servlet.MultipartConfigElement;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.util.unit.DataSize;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Service
public class MultipartConfigService {

    @Autowired
    private MultipartConfigElement multipartConfigElement;

    public void updateMaxUploadSize(long maxFileSize, long maxRequestSize) {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(maxFileSize));
        factory.setMaxRequestSize(DataSize.ofMegabytes(maxRequestSize));
        this.multipartConfigElement = factory.createMultipartConfig();
    }

    public MultipartConfigElement getMultipartConfigElement() {
        return this.multipartConfigElement;
    }

}