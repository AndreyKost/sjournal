package sjournal.model.service;

import java.time.LocalDateTime;

public class ArticleServiceModel extends BaseServiceModel{
    private String name;
    private String textContent;
    private LocalDateTime addedOn;
    private UserServiceModel author;
    private TopicServiceModel topic;

    public ArticleServiceModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public UserServiceModel getAuthor() {
        return author;
    }

    public void setAuthor(UserServiceModel author) {
        this.author = author;
    }

    public TopicServiceModel getTopic() {
        return topic;
    }

    public void setTopic(TopicServiceModel topic) {
        this.topic = topic;
    }
}
