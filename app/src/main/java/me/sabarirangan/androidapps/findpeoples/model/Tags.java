package me.sabarirangan.androidapps.findpeoples.model;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import retrofit2.http.Path;

/**
 * Created by sabari on 20-04-2017.
 */


public class Tags extends RealmObject {
    @PrimaryKey
    private String name;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
