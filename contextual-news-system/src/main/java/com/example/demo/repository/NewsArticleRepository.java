package com.example.demo.repository;

import com.example.demo.model.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle,Long> {
    List<NewsArticle> findTop5ByCategoryIgnoreCaseOrderByPublicationDateDesc(String category);

    List<NewsArticle> findTop5BySourceNameIgnoreCaseOrderByPublicationDateDesc(String sourceName);

    List<NewsArticle> findTop5ByRelevanceScoreGreaterThanEqualOrderByRelevanceScoreDesc(Double min);

    /** naive ILIKE search (good enough for demo) */
    @Query("""
           SELECT a FROM NewsArticle a
           WHERE lower(a.title) LIKE lower(concat('%', :q, '%'))
              OR lower(a.description) LIKE lower(concat('%', :q, '%'))
           ORDER BY (a.relevanceScore) DESC, a.publicationDate DESC
           """)
    List<NewsArticle> searchTop5(String q);
}
