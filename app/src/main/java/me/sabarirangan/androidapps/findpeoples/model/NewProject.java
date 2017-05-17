package me.sabarirangan.androidapps.findpeoples.model;

import java.util.List;

/**
 * Created by sabari on 22-04-2017.
 */

public class NewProject {
    private String title;
    private String description;
    private List<String> tags = null;
    private Boolean anonymous;

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }




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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
