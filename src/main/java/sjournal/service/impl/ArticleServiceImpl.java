package sjournal.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sjournal.error.ArticleNotFoundException;
import sjournal.error.Constants;
import sjournal.model.entity.Article;
import sjournal.model.entity.Topic;
import sjournal.model.entity.User;
import sjournal.model.service.ArticleServiceModel;
import sjournal.repository.ArticleRepository;
import sjournal.service.ArticleService;


import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void add(ArticleServiceModel articleServiceModel) {
        Article article=this.modelMapper.map(articleServiceModel,Article.class);
        this.articleRepository.saveAndFlush(article);
    }


    @Override
    public List<String> findAllArticles() {
        return this.articleRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Article::getAddedOn).reversed())
                .map(Article::getName)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleServiceModel findByName(String name) {
        return this.modelMapper.map(articleRepository.findByName(name),ArticleServiceModel.class);
    }

    @Override
    public List<ArticleServiceModel> findWholeArticles() {
        

        return this.articleRepository.findAll()
                .stream()
                .map(article -> this.modelMapper.map(article,ArticleServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ArticleServiceModel findArticleById(String articleId) {
       return this.articleRepository.findById(articleId)
               .map(article -> this.modelMapper.map(article,ArticleServiceModel.class))
               .orElseThrow(()-> new ArticleNotFoundException(Constants.ARTICLE_ID_NOT_FOUND));
    }

    @Override
    public ArticleServiceModel editArticle(String id, ArticleServiceModel articleServiceModel) {
        Article article=this.articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(Constants.ARTICLE_ID_NOT_FOUND));

        //article.setTopic(this.modelMapper.map(articleServiceModel.getTopic(),Topic.class));
        String newTextContent = articleServiceModel.getTextContent();
        article.setTextContent(newTextContent);
        //article.setAuthor(this.modelMapper.map(articleServiceModel.getAuthor(), User.class));
        //article.setAddedOn(articleServiceModel.getAddedOn());

        this.articleRepository.saveAndFlush(article);

        return this.modelMapper.map(article, ArticleServiceModel.class);
    }

    @Override
    public void deleteArticle(String id) {

        Article article=this.articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException(Constants.ARTICLE_ID_NOT_FOUND));

        this.articleRepository.delete(article);
    }

    @Override
    public ArticleServiceModel findArticleByName(String name) {
        return this.articleRepository.findArticleByName(name)
                .map(a -> this.modelMapper.map(a, ArticleServiceModel.class))
                .orElseThrow(() -> new ArticleNotFoundException(Constants.ARTICLE_NOT_FOUND));
    }

    @Override
    public List<ArticleServiceModel> findArticleByAuthor(String author) {
        return this.articleRepository.findAll()
                .stream()
                .map(q -> this.modelMapper.map(q, ArticleServiceModel.class))
                .filter(q -> q.getAuthor().getUsername().equals(author))
                .collect(Collectors.toList());
    }


}
