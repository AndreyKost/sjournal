package sjournal.service;

import sjournal.model.service.TopicServiceModel;

import java.util.List;

public interface TopicService {
    void addTop(TopicServiceModel exerciseServiceModel);

    List<String> findAllTopicNames();

    TopicServiceModel findByName(String exercise);
}
