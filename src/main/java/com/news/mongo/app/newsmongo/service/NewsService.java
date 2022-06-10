package com.news.mongo.app.newsmongo.service;


import com.news.mongo.app.newsmongo.dto.header.HeaderRequest;
import com.news.mongo.app.newsmongo.dto.news.NewsCreateDto;
import com.news.mongo.app.newsmongo.dto.news.NewsResponseDto;
import com.news.mongo.app.newsmongo.dto.news.NewsUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NewsService {
    List<NewsResponseDto> getAllNews();
    NewsResponseDto getNewsById(Long id);
    NewsResponseDto create(MultipartFile picture, NewsCreateDto newsCreateDto);
    List<NewsResponseDto> getNewsByOffsetAndLimit(Integer offset,Integer limit);
    NewsResponseDto deleteNewsById(Long id);
    NewsResponseDto updateNews(Long id, MultipartFile picture, NewsUpdateDto newsUpdateDto);
}
