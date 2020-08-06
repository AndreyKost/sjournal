package sjournal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sjournal.model.entity.Review;


@Repository
public interface ReviewRepository extends JpaRepository<Review,String> {

}