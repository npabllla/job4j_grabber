package ru.job4j.grabber;

import javafx.geometry.Pos;

public class Post {
    private String id;
    private String name;
    private String text;
    private String link;
    private String created;

    public Post() {

    }

    public Post(String id, String name, String text, String link, String created) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.link = link;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Post{"
                + "id='"
                + id + '\''
                + ", name='"
                + name + '\''
                + ", text='"
                + text + '\''
                + ", link='"
                + link + '\''
                + ", created='"
                + created + '\''
                + '}';
    }
}