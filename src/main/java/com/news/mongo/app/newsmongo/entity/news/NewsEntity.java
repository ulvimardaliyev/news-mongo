package com.news.mongo.app.newsmongo.entity.news;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "news")
public class NewsEntity {
    @Transient
    public static final String SEQUENCE_NAME = "news_sequence";
    @Id
    private Long id;
    private String title;
    private String description;
    private LocalDateTime localDateTime;
    private Long viewCount;
    private NextNewsEntity next;
    private PreviousNewsEntity previous;
    private String pictureUrl;
    private LocalDateTime updatedTime;
}
