package me.sabarirangan.androidapps.findpeoples.model;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sabari on 20-04-2017.
 */

public class Project extends RealmObject{
    @PrimaryKey
    private Integer id;
    private String title;
    private String description;
    private String created_at;
    private Boolean anonymous;

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }


    @NonNull
    private Integer status;

    public Integer getStatus() {
        return (status==null)?10:status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
        String[] spdate=created_at.split("T");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(spdate[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd,yyyy");
        return fmtOut.format(date);
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
