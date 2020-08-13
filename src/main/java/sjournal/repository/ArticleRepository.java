package sjournal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sjournal.model.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query("select a from Article as a where a.topic.name=:name")
    Article findByTopicName(String name);

    Article findByName(String name);

}
