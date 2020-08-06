package sjournal.service;

import sjournal.model.service.ArticleServiceModel;

import java.util.List;
import java.util.Set;


public interface ArticleService {
    void add(ArticleServiceModel articleServiceModel);

    //ArticleServiceModel findOneToCheck();// tuka e dobre da e view model vmesto service model da go opravq

    //ArticleServiceModel findById(String homeworkId);

    List<String> findAllArticles();

    ArticleServiceModel findByName(String name);

    List<ArticleServiceModel> findWholeArticles();
}
