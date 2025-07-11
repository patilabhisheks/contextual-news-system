package com.example.demo.controller;

import com.example.demo.model.NewsArticle;
import com.example.demo.repository.NewsArticleRepository;
import com.example.demo.service.NewsService;
import com.example.demo.service.OpenAiServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsArticleController  {

    private final NewsArticleRepository repo;

    private final NewsService svc;
    private final OpenAiServiceAdapter llm;

//    @GetMapping("/score")
//    public List<NewsArticle> byScore(
//            @RequestParam(defaultValue = "0.7") double min) {
//        return repo.findAll()
//                .stream()
//                .filter(a -> a.getRelevanceScore() >= min)
//                .limit(5)
//                .toList();
//    }
    @GetMapping("/category")
    public List<NewsArticle> byCategory(@RequestParam String name){
        return svc.byCategory(name);
    }

    @GetMapping("/source")
    public List<NewsArticle> bySource(@RequestParam String name){
        return svc.bySource(name);
    }

    @GetMapping("/score")
    public List<NewsArticle> byScore(@RequestParam(defaultValue="0.7") double min){
        return svc.byScore(min);
    }

    @GetMapping("/search")
    public List<NewsArticle> search(@RequestParam String q){
        return svc.search(q);
    }

    @GetMapping("/nearby")
    public List<NewsArticle> nearby(@RequestParam double lat,
                                    @RequestParam double lon,
                                    @RequestParam(defaultValue="10") double radiusKm){
        return svc.nearby(lat,lon,radiusKm);
    }

    @GetMapping("/auto")
    public List<NewsArticle> auto(@RequestParam String q,
                                  @RequestParam(required=false) Double lat,
                                  @RequestParam(required=false) Double lon){
        var pq = llm.parse(q);
        return switch (pq.intent()) {
            case "category" -> svc.byCategory(pq.term());
            case "source"   -> svc.bySource  (pq.term());
            case "score"    -> svc.byScore(0.7);
            case "nearby"   -> svc.nearby(lat==null?0:lat, lon==null?0:lon, 10);
            default         -> svc.search(pq.term());
        };
    }
}
