package sjournal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sjournal.model.entity.Topic;


import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, String> {
    Optional<Topic> findByName(String name);
}
