package sjournal.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sjournal.model.entity.Article;
import sjournal.model.entity.Topic;
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
                .map(article -> article.getTopic().getName())
                .collect(Collectors.toList());
    }

    @Override
    public ArticleServiceModel findByName(String name) {
        return this.modelMapper.map(articleRepository.findByTopicName(name),ArticleServiceModel.class);
    }

    @Override
    public List<ArticleServiceModel> findWholeArticles() {
        

        return this.articleRepository.findAll()
                .stream()
                .map(article -> this.modelMapper.map(article,ArticleServiceModel.class))
                .collect(Collectors.toList());
    }


}
