package me.sabarirangan.apps.findpeoples.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sabari on 20-04-2017.
 */

public class Comment extends RealmObject{
    @PrimaryKey
    private String id;
    private String content;
    private String created_at;
    private Review review;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
