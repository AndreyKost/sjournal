package sjournal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sjournal.model.entity.Review;

import java.util.Optional;


@Repository
public interface ReviewRepository extends JpaRepository<Review,String> {

    @Query("select r from Review as r where r.article.id=:articleId")
    Optional<Review> findByArticleId(String articleId);


}
