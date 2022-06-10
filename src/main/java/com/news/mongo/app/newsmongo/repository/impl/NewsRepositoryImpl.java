package com.news.mongo.app.newsmongo.repository.impl;

import com.news.mongo.app.newsmongo.entity.news.NewsEntity;
import com.news.mongo.app.newsmongo.entity.news.NextNewsEntity;
import com.news.mongo.app.newsmongo.entity.news.PreviousNewsEntity;
import com.news.mongo.app.newsmongo.exception.NewsNotFoundException;
import com.news.mongo.app.newsmongo.repository.NewsRepository;
import com.news.mongo.app.newsmongo.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.news.mongo.app.newsmongo.entity.news.NewsEntity.SEQUENCE_NAME;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepository {
    private final MongoTemplate mongoTemplate;
    private final SequenceGeneratorService sequenceGeneratorService;


    @Override
    public List<NewsEntity> getAllNews() {
        return mongoTemplate.findAll(NewsEntity.class);
    }

    @Override
    public NewsEntity getNewsById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.inc("viewCount");
        NewsEntity newsEntity = mongoTemplate.findAndModify(query, update, NewsEntity.class);
        if (newsEntity == null) {
            throw new NewsNotFoundException(
                    ""
            );
        }
        return newsEntity;
    }

    @Override
    public NewsEntity create(NewsEntity newsModel) {
        //first insert
        NewsEntity lastNews = mongoTemplate.findOne(new Query().with(Sort.by("id").descending()).limit(1), NewsEntity.class);
        NewsEntity newInserted = null;
        if (lastNews == null) {
            newsModel.setNext(null);
            newsModel.setPrevious(null);
            newsModel.setId(sequenceGeneratorService.generate(SEQUENCE_NAME));
            newInserted = mongoTemplate.save(newsModel);
            return newInserted;
        }
        //after first insert
        Long previousNewsId = lastNews.getId();
        PreviousNewsEntity previousNewsEntity = new PreviousNewsEntity();
        previousNewsEntity.setTitle(lastNews.getTitle());
        previousNewsEntity.setId(lastNews.getId());

        newsModel.setPrevious(previousNewsEntity);
        newsModel.setId(sequenceGeneratorService.generate(SEQUENCE_NAME));
        newInserted = mongoTemplate.save(newsModel); //save new news with previous

        Query query = new Query();
        query.addCriteria(where("id").is(previousNewsId));
        Update update = new Update();
        NextNewsEntity newsEntity = new NextNewsEntity();
        newsEntity.setTitle(newInserted.getTitle());
        newsEntity.setId(newInserted.getId());
        update.set("next", newsEntity);
        mongoTemplate.updateFirst(query, update, NewsEntity.class); //update previous news with next
        return newInserted;
    }

    @Override
    public List<NewsEntity> getByOffsetAndLimit(Integer offset, Integer limit) {
        Query query = new Query().skip(offset).limit(limit);
        return mongoTemplate.find(query, NewsEntity.class);
    }

    @Override
    public void setCurrentsNextToCurrentsPrevious(Long previousId, NextNewsEntity nextNewsEntity) {
        Update update = new Update();
        update.set("next", nextNewsEntity);
        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(previousId)), update, NewsEntity.class);
    }

    @Override
    public void setCurrentsPreviousToCurrentsNext(Long nextId, PreviousNewsEntity previousNewsEntity) {
        Update update = new Update();
        update.set("previous", previousNewsEntity);
        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(nextId)), update, NewsEntity.class);
    }

    @Override
    public NewsEntity deleteById(Long id) {
        Query query = new Query();
        query.addCriteria(where("id").is(id));
        return mongoTemplate.findAndRemove(query, NewsEntity.class);
    }

    @Override
    public NewsEntity update(Long id, Map<String, Object> fields) {
        Query query = new Query();
        query.addCriteria(where("id").is(id));
        Update update = new Update();
        fields.forEach(update::set);
        return mongoTemplate.findAndModify(query, update, NewsEntity.class);
    }

    @Override
    public boolean existById(Long id) {
        return Optional.ofNullable(
                        mongoTemplate.findOne(
                                new Query(where("id").is(id)), NewsEntity.class)
                )
                .isPresent();
    }
}
