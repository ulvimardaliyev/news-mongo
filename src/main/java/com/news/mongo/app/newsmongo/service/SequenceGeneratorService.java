package com.news.mongo.app.newsmongo.service;

public interface SequenceGeneratorService {
    long generate(String seqName);
}