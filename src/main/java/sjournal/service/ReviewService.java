package sjournal.service;

import sjournal.model.service.ReviewServiceModel;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    void add(ReviewServiceModel reviewServiceModel);

    ReviewServiceModel findByArticleId(String articleId);

    boolean isArticleExist(String articleId);

    void deleteReview(ReviewServiceModel reviewServiceModel);

    List<ReviewServiceModel> findWholeReviews();

    double getAvgScore();

    //HashMap<Integer,Integer> getScoreMap();
}
