package com.example.demo.repository;

import com.example.demo.model.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle,Long> {
}
