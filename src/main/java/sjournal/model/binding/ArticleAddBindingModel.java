package sjournal.model.binding;

import org.hibernate.validator.constraints.Length;

public class ArticleAddBindingModel {
    private String name;
    private  String topic;
    private String textContent;

    public ArticleAddBindingModel() {
    }

    @Length(min = 3, message = "Article name length must be more than 2 characters")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 3, message = "Topic name length must be more than 2 characters")
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Length(min = 10, message = "Text content length must be more than 9 characters")
    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
