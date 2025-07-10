package com.example.demo.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "news_article")
@Getter @Setter
public class NewsArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String url;

    @Column(name = "publication_date")
    private OffsetDateTime publicationDate;

    @Column(name = "source_name")
    private String sourceName;

    private String category;

    @Column(name = "relevance_score")
    private Double relevanceScore;

    /** PostGIS point (longitude, latitude) */
    @Column(columnDefinition = "geography(Point,4326)")
    private Point location;

    @Column(name = "llm_summary", columnDefinition = "TEXT")
    private String llmSummary;
}
