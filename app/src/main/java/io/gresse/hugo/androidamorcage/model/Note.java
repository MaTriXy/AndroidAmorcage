package io.gresse.hugo.androidamorcage.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * A simple note
 *
 * Created by Hugo Gresse on 02/10/15.
 */
public class Note extends RealmObject {

    @PrimaryKey
    private int id;

    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
