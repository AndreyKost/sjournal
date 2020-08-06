package sjournal.model.binding;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class ReviewAddBindingModel {
    private int score;
    private String articleId;

    public ReviewAddBindingModel() {
    }

    @Min(value = 2,message = "Score must be between 2 and 6 inclusive")
    @Max(value = 6,message = "Score must be between 2 and 6 inclusive")
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
