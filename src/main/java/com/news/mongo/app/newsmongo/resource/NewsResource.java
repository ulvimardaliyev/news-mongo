package com.news.mongo.app.newsmongo.resource;

import com.news.mongo.app.newsmongo.dto.news.NewsCreateDto;
import com.news.mongo.app.newsmongo.dto.news.NewsResponseDto;
import com.news.mongo.app.newsmongo.dto.news.NewsUpdateDto;
import com.news.mongo.app.newsmongo.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/news")
public class NewsResource {
    private final NewsService newsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsResponseDto> create(MultipartFile picture, NewsCreateDto createDto) {
        return ResponseEntity.ok(newsService.create(picture, createDto));
    }

    @GetMapping("/{newsId}")
    public ResponseEntity<NewsResponseDto> getNews(@PathVariable Long newsId) {
        return ResponseEntity.ok(newsService.getNewsById(newsId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<NewsResponseDto>> getAllNews() {
        return ResponseEntity.ok((newsService.getAllNews()));
    }

    @GetMapping
    public ResponseEntity<List<NewsResponseDto>> get(@RequestParam(defaultValue = "0") Integer offset,
                                                     @RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(
                newsService.getNewsByOffsetAndLimit(offset, limit)

        );
    }

    @DeleteMapping("/{newsId}")
    public ResponseEntity<NewsResponseDto> delete(@PathVariable Long newsId) {
        return ResponseEntity.ok(newsService.deleteNewsById(newsId));
    }

    @PatchMapping("/{newsId}")
    public ResponseEntity<NewsResponseDto> delete(@PathVariable Long newsId,
                                                  MultipartFile picture,
                                                  @RequestBody NewsUpdateDto newsUpdateDto) {
        return ResponseEntity.ok((newsService.updateNews(newsId, picture, newsUpdateDto)));
    }
}
