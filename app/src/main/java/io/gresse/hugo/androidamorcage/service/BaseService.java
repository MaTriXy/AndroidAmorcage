package io.gresse.hugo.androidamorcage.service;

import com.squareup.otto.Bus;

import io.gresse.hugo.androidamorcage.NoteApi;
import io.realm.Realm;

/**
 * Base service with a bus an a realm instance and the api instance
 * <p/>
 * Created by Hugo on 12/07/2015.
 */
public abstract class BaseService {

    protected Realm mRealm;
    protected Bus mBus;
    protected NoteApi mNoteApi;

    public BaseService(Realm realm, Bus bus, NoteApi noteApi) {
        mRealm = realm;
        mBus = bus;
        mNoteApi = noteApi;
    }
}
