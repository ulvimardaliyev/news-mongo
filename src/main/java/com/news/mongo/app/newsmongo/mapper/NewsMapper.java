package com.news.mongo.app.newsmongo.mapper;

import com.news.mongo.app.newsmongo.dto.news.*;
import com.news.mongo.app.newsmongo.entity.news.NewsEntity;
import com.news.mongo.app.newsmongo.entity.news.NextNewsEntity;
import com.news.mongo.app.newsmongo.entity.news.PreviousNewsEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class NewsMapper {

    public NewsEntity convert(NewsCreateDto createDto) {
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setDescription(createDto.getDescription());
        newsEntity.setTitle(createDto.getTitle());
        newsEntity.setViewCount(0L);
        newsEntity.setLocalDateTime(LocalDateTime.now(ZoneOffset.UTC));
        return newsEntity;
    }

    public NewsEntity convert(String pictureUrl, NewsCreateDto createDto) {
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setDescription(createDto.getDescription());
        newsEntity.setTitle(createDto.getTitle());
        newsEntity.setViewCount(0L);
        newsEntity.setLocalDateTime(LocalDateTime.now(ZoneOffset.UTC));
        newsEntity.setPictureUrl(pictureUrl);
        return newsEntity;
    }

    public NewsResponseDto convert(NewsEntity newsEntity) {
        NewsResponseDto newsResponseDto = new NewsResponseDto();
        newsResponseDto.setId(newsEntity.getId());
        newsResponseDto.setDescription(newsEntity.getDescription());
        newsResponseDto.setTitle(newsEntity.getTitle());
        newsResponseDto.setViewCount(newsEntity.getViewCount());
        newsResponseDto.setNext(convert(newsEntity.getNext()));
        newsResponseDto.setPrevious(convert(newsEntity.getPrevious()));
        newsResponseDto.setPictureUrl(newsEntity.getPictureUrl());
        newsResponseDto.setDate(newsEntity.getLocalDateTime().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
        return newsResponseDto;
    }

    public List<NewsResponseDto> convert(List<NewsEntity> allNews) {
        List<NewsResponseDto> allNewsResponse = new ArrayList<>();
        allNews.forEach(
                newsEntity -> allNewsResponse.add(convert(newsEntity))
        );
        return allNewsResponse;
    }

    public NextNewsDto convert(NextNewsEntity nextNewsEntity) {
        NextNewsDto newsDto = new NextNewsDto();

        if (nextNewsEntity == null || nextNewsEntity.getId() == null || nextNewsEntity.getTitle() == null) {
            return newsDto;
        }
        newsDto.setTitle(nextNewsEntity.getTitle());
        newsDto.setId(nextNewsEntity.getId());
        return newsDto;
    }

    public PreviousNewsDto convert(PreviousNewsEntity previousNewsEntity) {
        PreviousNewsDto previousNewsDto = new PreviousNewsDto();
        if (previousNewsEntity == null || previousNewsEntity.getId() == null || previousNewsEntity.getTitle() == null) {
            return previousNewsDto;
        }
        previousNewsDto.setId(previousNewsEntity.getId());
        previousNewsDto.setTitle(previousNewsEntity.getTitle());
        return previousNewsDto;
    }

    public PreviousNewsEntity convert(PreviousNewsDto previousNewsDto) {
        PreviousNewsEntity previousNewsEntity = new PreviousNewsEntity();
        previousNewsEntity.setId(previousNewsDto.getId());
        previousNewsEntity.setTitle(previousNewsDto.getTitle());
        return previousNewsEntity;
    }

    public NextNewsEntity convert(NewsResponseDto newsResponseDto) {
        NextNewsEntity newsEntity = new NextNewsEntity();
        newsEntity.setId(newsResponseDto.getId());
        newsEntity.setTitle(newsResponseDto.getTitle());
        return newsEntity;
    }

    public Map<String, Object> convert(NewsUpdateDto targetEntity) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", targetEntity.getTitle());
        map.put("description", targetEntity.getDescription());
        map.values().removeIf(Objects::isNull);
        map.put("updatedTime", LocalDateTime.now(ZoneOffset.UTC));
        return map;
    }
}
