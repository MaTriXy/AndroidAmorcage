package io.gresse.hugo.androidamorcage;

import io.gresse.hugo.androidamorcage.model.Note;
import io.realm.RealmList;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * A simple note interface representing the note API
 *
 * Created by Hugo Gresse on 02/10/15.
 */
public interface NoteApi {

    @GET("/notes")
    void notes(@Query("start") String start,
               @Query("limit") String limit,
               Callback<RealmList<Note>> callback);

}
