package io.gresse.hugo.androidamorcage.event.base;

/**
 * Created by Hugo Gresse on 12/10/15.
 */

import android.util.Log;

import java.net.SocketTimeoutException;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Retry event. Manage generate event check for retrying
 * <p/>
 * Created by Hugo on 06/07/2015.
 */
public abstract class RetryEvent implements NetworkEvent {

    private static final String TAG = RetryEvent.class.getSimpleName();

    // Number of retry if request failed
    public static final int NUM_RETRIES = 2;

    private int mRetryCount;

    /**
     * Check if we should retry the network call. If yes, increase the amount of retry.
     *
     * @param error the retrofit error
     * @return true if we should retry
     */
    public boolean shouldRetry(RetrofitError error) {

        // If it's a HTTP Request Time-out (408) code
        Response response = error.getResponse();
        if (response != null && response.getStatus() == 408) {
            Log.v(TAG, "Request time-out, retry: " + this.toString());
            if (mRetryCount >= NUM_RETRIES) {
                // retry if possible
                mRetryCount++;
                return true;
            }
            return false;
        }

        if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
            if (error.getCause() instanceof SocketTimeoutException) {
                //connection timeout check
                if (mRetryCount >= NUM_RETRIES) {
                    // retry if possible
                    mRetryCount++;
                    return true;
                }

                Log.i(TAG, toString() + " has no more retries");
            } else {
                //no connection check (issue with the connection)
                Log.i(TAG, "No connection: " + toString());
            }
        }
        // else non network error check

        mRetryCount = 0;

        Log.w(TAG, "non network error : " + toString() + " error:" + error.toString());
        return false;
    }
}
