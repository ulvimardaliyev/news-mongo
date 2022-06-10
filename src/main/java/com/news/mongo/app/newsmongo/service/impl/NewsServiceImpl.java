package com.news.mongo.app.newsmongo.service.impl;

import com.news.mongo.app.newsmongo.dto.header.HeaderRequest;
import com.news.mongo.app.newsmongo.dto.news.NewsCreateDto;
import com.news.mongo.app.newsmongo.dto.news.NewsResponseDto;
import com.news.mongo.app.newsmongo.dto.news.NewsUpdateDto;
import com.news.mongo.app.newsmongo.entity.news.NewsEntity;
import com.news.mongo.app.newsmongo.entity.news.NextNewsEntity;
import com.news.mongo.app.newsmongo.entity.news.PreviousNewsEntity;
import com.news.mongo.app.newsmongo.exception.NewsNotFoundException;
import com.news.mongo.app.newsmongo.mapper.NewsMapper;
import com.news.mongo.app.newsmongo.repository.NewsRepository;
import com.news.mongo.app.newsmongo.service.NewsService;
import com.news.mongo.app.newsmongo.validation.NewsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.news.mongo.app.newsmongo.utils.Constant.NEWS_PIC_URL;


@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    private final NewsValidator newsValidator;

    @Override
    public List<NewsResponseDto> getAllNews() {
        return newsMapper.convert(newsRepository.getAllNews());
    }

    @Override
    public NewsResponseDto getNewsById(Long id) {
        NewsEntity newsEntity = newsRepository.getNewsById(id);
        if (newsEntity == null)
            throw new NewsNotFoundException(
                    ""
            );
        return newsMapper.convert(newsEntity);
    }

    @Override
    public NewsResponseDto create(MultipartFile picture, NewsCreateDto newsCreateDto) {
        newsValidator.validate(picture, newsCreateDto);
        String uploadedPic;
        try {
            uploadedPic = Arrays.toString(picture.getBytes());
        } catch (IOException e) {
            throw new NewsNotFoundException(
                    ""
            );
        }
        NewsEntity newsEntity = newsMapper.convert(NEWS_PIC_URL, newsCreateDto);
        NewsEntity inserted = newsRepository.create(newsEntity);
        return newsMapper.convert(inserted);
    }

    @Override
    public List<NewsResponseDto> getNewsByOffsetAndLimit(Integer offset, Integer limit) {
        return newsMapper.convert(newsRepository.getByOffsetAndLimit(offset, limit));
    }

    @Override
    public NewsResponseDto deleteNewsById(Long id) {
        NewsEntity newsEntity = newsRepository.getNewsById(id);
        PreviousNewsEntity previousNewsEntity = newsEntity.getPrevious();
        Long previousNewsId = null;
        if (previousNewsEntity != null) {
            previousNewsId = previousNewsEntity.getId();
        }
        NextNewsEntity nextNewsEntity = newsEntity.getNext();
        Long nextNewsId = null;
        if (nextNewsEntity != null) {
            nextNewsId = nextNewsEntity.getId();
        }
        /**
         *if next and previous news is not null,
         *then set current news next to previous news and current news previous to next news
         */
        if (nextNewsId != null && previousNewsId != null) {
            newsRepository.setCurrentsNextToCurrentsPrevious(previousNewsId, nextNewsEntity);
            newsRepository.setCurrentsPreviousToCurrentsNext(nextNewsId, previousNewsEntity);
        }
        /**if current news is last news then set current news previous to null */
        if (nextNewsId == null && previousNewsId != null) {
            newsRepository.setCurrentsNextToCurrentsPrevious(previousNewsId, null);
        }
        /**if current news is first news then set current news next to null */
        if (previousNewsId == null && nextNewsId != null) {
            newsRepository.setCurrentsPreviousToCurrentsNext(nextNewsId, null);
        }
        /**delete current news*/
        return newsMapper.convert(newsRepository.deleteById(id));
    }

    @Override
    public NewsResponseDto updateNews(Long id, MultipartFile picture, NewsUpdateDto newsUpdateDto) {
        if (!newsRepository.existById(id)) {
            throw new NewsNotFoundException(
                    ""
            );
        }
        Map<String, Object> update = newsMapper.convert(newsUpdateDto);
        String uploadedPic;
        if (picture != null && !picture.isEmpty()) {
            try {
                uploadedPic = Arrays.toString(picture.getBytes());
                update.put("pictureUrl", NEWS_PIC_URL);
            } catch (IOException e) {
                log.info("Exception message in log {}", e.getMessage());
            }
        }
        return newsMapper.convert(newsRepository.update(id, update));
    }
}
