package me.sabarirangan.apps.findpeoples.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sabari on 20-04-2017.
 */

public class Project extends RealmObject{
    @PrimaryKey
    private int id;
    private String title;
    private String description;
    private String created_at;
    private UserProfile user;
    private RealmList<Tags> tags;

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public RealmList<Tags> getTags() {
        return tags;
    }

    public void setTags(RealmList<Tags> tags) {
        this.tags = tags;
    }
}
