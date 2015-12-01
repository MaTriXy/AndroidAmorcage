package io.gresse.hugo.androidamorcage.event.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.gresse.hugo.androidamorcage.event.base.NetworkEvent;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Error returner by the api. If there is a erro key value in a json, parse it and save it.
 * <p/>
 * Created by Hugo Gresse on 06/07/15.
 */
public class ApiErrorEvent {

    public RetrofitError mRetrofitError;
    public NetworkEvent mOriginalEvent;
    public String mError;

    public ApiErrorEvent(RetrofitError error, NetworkEvent event) {
        mRetrofitError = error;
        mOriginalEvent = event;

        try {
            JsonElement jelement = new JsonParser().parse(
                    new String(((TypedByteArray) error.getResponse().getBody()).getBytes())
            );
            JsonObject jobject = jelement.getAsJsonObject();
            mError = jobject.get("error").getAsString();
        } catch (NullPointerException ignored) {
        }

    }

    @Override
    public String toString() {
        return "ApiErrorEvent mRetrofitError=" + mRetrofitError;
    }
}
