package sjournal.model.service;

public class ReviewServiceModel extends BaseServiceModel {
    private int score;
    private UserServiceModel author;
    private ArticleServiceModel article;

    public ReviewServiceModel() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UserServiceModel getAuthor() {
        return author;
    }

    public void setAuthor(UserServiceModel author) {
        this.author = author;
    }

    public ArticleServiceModel getArticle() {
        return article;
    }

    public void setArticle(ArticleServiceModel article) {
        this.article = article;
    }
}
