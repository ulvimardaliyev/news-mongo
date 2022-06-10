package com.news.mongo.app.newsmongo.dto.news;

import lombok.Data;

@Data
public class NewsResponseDto  {
    private Long id;
    private String title;
    private String description;
    private Long date;
    private Long viewCount;
    private NextNewsDto next;
    private PreviousNewsDto previous;
    private String pictureUrl;
}
