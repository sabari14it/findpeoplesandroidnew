package me.sabarirangan.androidapps.findpeoples.model;

import io.realm.RealmObject;

/**
 * Created by sabari on 3/5/17.
 */

public class SearchHistory extends RealmObject {
    String history;

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
