package sjournal.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sjournal.model.entity.Review;
import sjournal.model.service.ReviewServiceModel;
import sjournal.repository.ReviewRepository;
import sjournal.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void add(ReviewServiceModel reviewServiceModel) {
        this.reviewRepository.saveAndFlush(this.modelMapper.map(reviewServiceModel, Review.class));
    }
}
