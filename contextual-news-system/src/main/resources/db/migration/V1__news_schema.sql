CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE news_article (
  id               UUID PRIMARY KEY,
  title            TEXT,
  description      TEXT,
  url              TEXT,
  publication_date TIMESTAMPTZ,
  source_name      TEXT,
  category         TEXT,
  relevance_score  NUMERIC(3,2),
  location         GEOGRAPHY(Point,4326),
  llm_summary      TEXT
);

ALTER TABLE news_article
  ADD COLUMN tsv tsvector
  GENERATED ALWAYS AS
    (to_tsvector('english', coalesce(title,'') || ' ' || coalesce(description,'')))
  STORED;

CREATE INDEX idx_news_tsv ON news_article USING GIN (tsv);
CREATE INDEX idx_news_loc ON news_article USING GIST (location);
