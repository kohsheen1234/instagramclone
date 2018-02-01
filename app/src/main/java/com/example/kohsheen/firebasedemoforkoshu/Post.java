package com.example.kohsheen.firebasedemoforkoshu;

/**
 * Created by kohsheen on 7/1/18.
 */

public class Post {
    public String title;
    public String description;
    public String url;
    public String username;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Post(String title, String description,String username, String url) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.url = url;
    }

    public Post() {
    }

}
