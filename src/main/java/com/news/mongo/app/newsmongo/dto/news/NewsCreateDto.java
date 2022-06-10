package com.news.mongo.app.newsmongo.dto.news;

import lombok.Data;

@Data
public class NewsCreateDto  {
    private String title;
    private String description;
}
