package com.example.demo.service;

import com.example.demo.model.NewsArticle;
import com.example.demo.repository.NewsArticleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@Profile("load")
@RequiredArgsConstructor
public class NewsLoader implements CommandLineRunner {

    private final NewsArticleRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        if (repo.count() > 0) return;

        var file = new File("src/main/resources/data/news_data.json");
        List<NewsArticle> items = mapper.readValue(file,
                new TypeReference<List<NewsArticle>>() {});
        repo.saveAll(items);
        System.out.println("Loaded "+items.size()+" articles.");
    }
}
