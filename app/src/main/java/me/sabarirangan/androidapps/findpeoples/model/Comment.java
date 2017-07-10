package me.sabarirangan.androidapps.findpeoples.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sabari on 20-04-2017.
 */

public class Comment extends RealmObject{
    @PrimaryKey
    private Integer id;
    private String content;
    private String created_at;
    private Project project;
    private UserProfile user;
    private Integer notify=0;

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        String[] spdate=created_at.split("T");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(spdate[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd,yyyy");
            return fmtOut.format(date);
        } catch (ParseException e) {
            return "now";
        }


    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }
}
