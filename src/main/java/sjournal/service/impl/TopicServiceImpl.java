package sjournal.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sjournal.model.entity.Topic;
import sjournal.model.service.TopicServiceModel;
import sjournal.repository.TopicRepository;
import sjournal.service.TopicService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository, ModelMapper modelMapper) {
        this.topicRepository = topicRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addTop(TopicServiceModel topicServiceModel) {
        this.topicRepository.saveAndFlush(modelMapper.map(topicServiceModel, Topic.class));
    }

    @Override
    public List<String> findAllTopicNames() {

        return this.topicRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Topic::getAddedOn).reversed())
                .map(Topic::getName)
                .collect(Collectors.toList());
    }

    @Override
    public TopicServiceModel findByName(String topic ) {
         Topic topic1=this.topicRepository.findByName(topic).orElse(null);

         return this.modelMapper.map(topic1,TopicServiceModel.class);
    }
}
