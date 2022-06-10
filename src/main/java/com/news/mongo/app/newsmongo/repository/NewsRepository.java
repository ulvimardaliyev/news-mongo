package com.news.mongo.app.newsmongo.repository;



import com.news.mongo.app.newsmongo.entity.news.NewsEntity;
import com.news.mongo.app.newsmongo.entity.news.NextNewsEntity;
import com.news.mongo.app.newsmongo.entity.news.PreviousNewsEntity;

import java.util.List;
import java.util.Map;

public interface NewsRepository {
    List<NewsEntity> getAllNews();
    NewsEntity getNewsById(Long id);
    boolean existById(Long id);
    NewsEntity create(NewsEntity newsEntity);
    List<NewsEntity> getByOffsetAndLimit(Integer offset, Integer limit);
    NewsEntity deleteById(Long id);
    void setCurrentsNextToCurrentsPrevious(Long previousId, NextNewsEntity nextNewsEntity);
    void setCurrentsPreviousToCurrentsNext(Long nextId, PreviousNewsEntity previousNewsEntity);
    NewsEntity update(Long id, Map<String, Object> fields);
}
