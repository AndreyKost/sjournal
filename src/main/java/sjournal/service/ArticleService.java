package sjournal.service;

import sjournal.model.service.ArticleServiceModel;

import java.util.List;
import java.util.Set;


public interface ArticleService {
    void add(ArticleServiceModel articleServiceModel);

    List<String> findAllArticles();

    ArticleServiceModel findByName(String name);

    List<ArticleServiceModel> findWholeArticles();

    ArticleServiceModel findArticleById(String articleId);

    ArticleServiceModel editArticle(String id,ArticleServiceModel articleServiceModel);

    void deleteArticle(String id);

    ArticleServiceModel findArticleByName(String name);

    List<ArticleServiceModel> findArticleByAuthor(String author);
}
