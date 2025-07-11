package com.example.demo.service;

import com.example.demo.model.NewsArticle;
import com.example.demo.repository.NewsArticleRepository;
import com.example.demo.utils.GeoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsArticleRepository repo;
    private final MyLlmService llm;

    public List<NewsArticle> byCategory(String cat){
        return enrich(repo.findTop5ByCategoryIgnoreCaseOrderByPublicationDateDesc(cat));
    }

    public List<NewsArticle> bySource(String src){
        return enrich(repo.findTop5BySourceNameIgnoreCaseOrderByPublicationDateDesc(src));
    }

    public List<NewsArticle> byScore(double min){
        return enrich(repo.findTop5ByRelevanceScoreGreaterThanEqualOrderByRelevanceScoreDesc(min));
    }

    public List<NewsArticle> search(String q){
        return enrich(repo.searchTop5(q));
    }

    public List<NewsArticle> nearby(double lat, double lon, double radiusKm) {
        return enrich(
                repo.findAll().stream()
                        .filter(a -> GeoUtil.distanceKm(lat, lon,
                                a.getLocation().getY(), a.getLocation().getX()) <= radiusKm)
                        .sorted(Comparator.comparingDouble(
                                a -> GeoUtil.distanceKm(lat, lon,
                                        a.getLocation().getY(), a.getLocation().getX())))
                        .limit(5)
                        .toList()
        );
    }

    private List<NewsArticle> enrich(List<NewsArticle> list) {
        list.forEach(a -> {
            if (a.getLlmSummary() == null || a.getLlmSummary().isEmpty()) {
                String summary = llm.summarise(a.getTitle(), a.getDescription());
                a.setLlmSummary(summary);
                repo.save(a);  // save updated summary
            }
        });
        return list;
    }

}
