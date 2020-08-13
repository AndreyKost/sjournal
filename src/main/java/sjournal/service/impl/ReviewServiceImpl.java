package sjournal.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sjournal.model.entity.Review;
import sjournal.model.service.ArticleServiceModel;
import sjournal.model.service.ReviewServiceModel;
import sjournal.repository.ReviewRepository;
import sjournal.service.ReviewService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public ReviewServiceModel findByArticleId(String articleId) {
        Review review = reviewRepository.findByArticleId(articleId).orElse(null);
        return modelMapper.map(review,ReviewServiceModel.class);
    }

    @Override
    public boolean isArticleExist(String articleId) {
        return reviewRepository.findByArticleId(articleId).isPresent();
    }


    @Override
    public void deleteReview(ReviewServiceModel reviewServiceModel) {
        Review reviewToDelete = this.modelMapper.map(reviewServiceModel, Review.class);
        this.reviewRepository.delete(reviewToDelete);
    }

    @Override
    public List<ReviewServiceModel> findWholeReviews() {
        return this.reviewRepository.findAll()
                .stream()
                .map(review -> this.modelMapper.map(review, ReviewServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public double getAvgScore() {
        return  this.reviewRepository.findAll()
                .stream()
                .mapToDouble(Review::getScore)
                .average()
                .orElse(0D);
    }



}
