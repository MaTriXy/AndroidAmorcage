package io.gresse.hugo.androidamorcage.service;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import io.gresse.hugo.androidamorcage.NoteApi;
import io.gresse.hugo.androidamorcage.event.network.ApiErrorEvent;
import io.gresse.hugo.androidamorcage.event.network.NetworkRequestFinishedEvent;
import io.gresse.hugo.androidamorcage.event.network.NetworkRequestStartEvent;
import io.gresse.hugo.androidamorcage.event.note.LoadNotesEvent;
import io.gresse.hugo.androidamorcage.event.note.NotesReceivedEvent;
import io.gresse.hugo.androidamorcage.model.Note;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Manage notes : get, post, delete and other process.
 * <p/>
 * Created by Hugo Gresse on 02/10/15.
 */
public class NoteService extends BaseService {

    public static final String TAG = NoteService.class.getSimpleName();

    public NoteService(Realm realm, Bus bus, NoteApi noteApi) {
        super(realm, bus, noteApi);
    }

    @Subscribe
    public void loadNotes(final LoadNotesEvent event) {
        Log.d(TAG, "loadNotes");
        mBus.post(new NetworkRequestStartEvent());
        mNoteApi.notes(
                String.valueOf(event.mStart),
                String.valueOf(event.mCount),
                new Callback<RealmList<Note>>() {
                    @Override
                    public void success(RealmList<Note> notes, Response response) {
                        mBus.post(new NetworkRequestFinishedEvent());

                        // Do your result process here

                        // Save data in Realm db
                        mRealm.beginTransaction();
                        mRealm.copyToRealmOrUpdate(notes);
                        mRealm.commitTransaction();

                        // Post a new successful load event
                        mBus.post(new NotesReceivedEvent(notes));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (event.shouldRetry(error)) {
                            mBus.post(event);
                            return;
                        }

                        mBus.post(new NetworkRequestFinishedEvent());

                        mBus.post(new ApiErrorEvent(error, event));
                    }
                }
        );
    }


    /**
     * Return the list of note
     *
     * @param realm realm instance
     * @return list of note
     */
    public static RealmList<Note> getNotes(Realm realm) {

        RealmResults<Note> notesResult = realm.where(Note.class).findAll();

        RealmList<Note> noteList = new RealmList<>();
        noteList.addAll(notesResult.subList(0, notesResult.size()));

        return noteList;
    }
}
