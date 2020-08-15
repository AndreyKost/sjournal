package sjournal.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import sjournal.repository.ArticleRepository;
import sjournal.service.ArticleService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ArticleServiceTests {

    @Autowired
    private ArticleService articleService;

    @MockBean
    private ArticleRepository mockArticleRepository;



    @Test(expected = Exception.class)
    public void articleService_findArticleByNameWithInvalidValue_ThrowError() {
        articleService.findArticleByName(null);
        verify(mockArticleRepository)
                .save(any());

    }


    @org.junit.Test(expected = Exception.class)
    public void articleService_deleteArticleWithInvalidValue_ThrowError() {
        articleService.deleteArticle(null);
        verify(mockArticleRepository)
                .save(any());
    }


    @org.junit.Test(expected = Exception.class)
    public void articleService_findArticleByIdWithInvalidValue_ThrowError() {
        articleService.findArticleById(null);
        verify(mockArticleRepository)
                .save(any());
    }



    @org.junit.Test(expected = Exception.class)
    public void articleService_editArticleWithInvalidValue_ThrowError() {
        articleService.editArticle(null,null);
        verify(mockArticleRepository)
                .save(any());
    }



    @Test(expected = Exception.class)
    public void articleService_findArticleByAuthorWithInvalidValue_ThrowError() {
        articleService.findArticleByAuthor(null);
        verify(mockArticleRepository)
                .save(any());
    }

    @Test(expected = Exception.class)
    public void articleService_findAllArticlesWithInvalidValue_ThrowError() {
        articleService.findWholeArticles();
        verify(mockArticleRepository)
                .save(any());
    }


}
