package io.gresse.hugo.androidamorcage.service;

import com.squareup.otto.Bus;

import io.gresse.hugo.androidamorcage.NoteApi;
import io.realm.Realm;

/**
 * Create and store services.
 * <p/>
 * Created by Hugo on 13/07/2015.
 */
public class ServiceProvider {

    protected NoteService mNoteService;

    public void register(Realm realm, NoteApi noteApi, Bus bus) {

        mNoteService = new NoteService(realm, bus, noteApi);

        bus.register(mNoteService);
    }

}
