package com.news.mongo.app.newsmongo.dto.header;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeaderRequest {
    private String lang;
    private String userId;
    private String requestId;
    private String username;
    private Integer pageIndex;
    private Integer pageSize;
    private Long startTime;
    private Long endTime;
}