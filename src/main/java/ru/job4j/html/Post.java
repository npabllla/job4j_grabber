package ru.job4j.html;

import java.util.Objects;

public class Post {
    private String link;
    private String description;
    private String date;

    public Post() {

    }
    public Post(String link, String description, String date) {
        this.link = link;
        this.description = description;
        this.date = date;
    }

    public Post(String link, String description) {
        this.link = link;
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Post{" + "link='"
                + link + '\''
                + ", description='"
                + description + '\''
                + ", date='"
                + date + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(link, post.link) && Objects.equals(description, post.description) && Objects.equals(date, post.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, description, date);
    }
}
