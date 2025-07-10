package com.example.demo.utils;

import com.example.demo.model.NewsArticle;
import com.example.demo.repository.NewsArticleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Profile("load")
@RequiredArgsConstructor
public class NewsJsonLoader implements CommandLineRunner {

    private final NewsArticleRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();
    private final GeometryFactory gf   = new GeometryFactory();

    @Override
    public void run(String... args) throws Exception {
        if (repo.count() > 0) {
            System.out.println("DB already seeded – skip loader.");
            return;
        }

        File json = new File(
                "src/main/resources/data/news_data.json");
        List<Raw> rawList = mapper.readValue(json, new TypeReference<>(){});

        rawList.stream().map(this::toEntity).forEach(repo::save);
        System.out.println("Inserted " + rawList.size() + " articles.");
    }


    private NewsArticle toEntity(Raw r) {
        NewsArticle n = new NewsArticle();
        n.setId(UUID.fromString(r.id));
        n.setTitle(r.title);
        n.setDescription(r.description);
        n.setUrl(r.url);
        n.setPublicationDate(
                OffsetDateTime.parse(r.publication_date));
        n.setSourceName(r.source_name);
        n.setCategory((r.category != null
                && !r.category.isEmpty()) ? r.category.get(0) : null);
        n.setRelevanceScore(r.relevance_score);

        Point p = gf.createPoint(new Coordinate(r.longitude, r.latitude));
        p.setSRID(4326);
        n.setLocation(p);
        return n;
    }

    private record Raw(
            String id,
            String title,
            String description,
            String url,
            String publication_date,
            String source_name,
            List<String> category,
            Double relevance_score,
            Double latitude,
            Double longitude
    ) {}

}
