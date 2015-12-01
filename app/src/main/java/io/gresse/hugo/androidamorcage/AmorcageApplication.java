package io.gresse.hugo.androidamorcage;

import android.app.Application;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import io.gresse.hugo.androidamorcage.event.BusProvider;
import io.gresse.hugo.androidamorcage.service.ServiceProvider;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.exceptions.RealmMigrationNeededException;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Main application that register app services
 *
 * Created by Hugo Gresse on 23/09/15.
 */
public class AmorcageApplication extends Application {

    public static final String API_URL = "http://private-bae52-hugogresse.apiary-mock.com";

    private NoteApi mNoteApi;
    private Realm mRealm;
    private Bus mBus = BusProvider.getInstance();
    private ServiceProvider mServiceProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        mNoteApi = buildApi();
        mRealm = buildDatabase();

        mBus.register(this); //listen for "global" events

        mServiceProvider = new ServiceProvider();
        mServiceProvider.register(mRealm, mNoteApi, mBus);

        Fresco.initialize(this);
    }

    /**
     * Build Retrofit Note api. Set custom settings for parsing :
     * - custom date parsing format
     * - custom exclusion strategies for managing {@link RealmObject}
     *
     * @return the noteApi
     */
    private NoteApi buildApi() {
        // create custom GSON for skipping Realm fields
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        return new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setConverter(new GsonConverter(gson))
                .build()
                .create(NoteApi.class);
    }

    /**
     * Create or retrieve the Realm instance. If the model has change, a {@link RealmMigrationNeededException} will be
     * thrown, catch, the database will be erased and a new Realm instance will be returned. This prevent issue in
     * developping phase and should be remvoed for production app.
     *
     * @return the Realm instance
     */
    @NonNull
    public Realm buildDatabase() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();

        try {
            return Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e) {
            try {
                Realm.deleteRealm(realmConfiguration);
                Toast.makeText(
                        this,
                        "Delete database due to missing migrations",
                        Toast.LENGTH_SHORT)
                        .show();
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex) {
                throw ex;
                //No Realm file to remove.
            }
        }
    }

}
