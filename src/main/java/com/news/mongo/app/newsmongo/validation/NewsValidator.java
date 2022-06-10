package com.news.mongo.app.newsmongo.validation;

import com.news.mongo.app.newsmongo.dto.news.NewsCreateDto;
import com.news.mongo.app.newsmongo.exception.NewsNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class NewsValidator {

    public void validate(MultipartFile multipartFile, NewsCreateDto createDto) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new NewsNotFoundException(
                    ""
            );
        }
        if (createDto.getDescription() == null || createDto.getDescription().isEmpty()) {
            throw new NewsNotFoundException(
                   ""
            );
        }
        if (createDto.getTitle() == null || createDto.getTitle().isEmpty()) {
            throw new NewsNotFoundException(
                   ""
            );
        }
    }
}
