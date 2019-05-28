package com.huji.pnina;

import androidx.annotation.Keep;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Keep
public class Post {
    private Date rawDate;
    private List<Integer> categories;
    private int likes;
    private String contentPost;
    private String creationDate;
    private String id;

    public Post() {
    }

    public Post(List<Integer> categories, String contentPost) {
        this.categories = categories;
        this.contentPost = contentPost;
        this.likes = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy, HH:mm");
        Date date = new Date();
        this.creationDate = dateFormat.format(date);
        this.rawDate = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getRawDate() {
        return rawDate.getTime();
    }

    public void setRawDate(long rawDate) {
        this.rawDate = new Date(rawDate);
    }


    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setContentPost(String contentPost) {
        this.contentPost = contentPost;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public int getLikes() {
        return likes;
    }

    public void addLike() {
        likes = likes + 1;
    }

    public String getContentPost() {
        return contentPost;
    }

}
