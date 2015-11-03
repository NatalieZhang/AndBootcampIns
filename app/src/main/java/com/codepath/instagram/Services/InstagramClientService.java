package com.codepath.instagram.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.networking.InstagramClient;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yuanzhang on 11/3/15.
 */
public class InstagramClientService extends IntentService {
    public static final String ACTION = "com.codepath.instagram.Services";

    private static final String TAG = "InstagramClientService";
    private InstagramClientDatabase instagramClientDatabase;

    public static Intent newIntent(Context context) {
        return new Intent(context, InstagramClientService.class);
    }

    // Creates an IntentService.  Invoked by your subclass's constructor.
    public InstagramClientService() {
        // Used to name the worker thread, important only for debugging.
        super("InstagramClientService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Context context = this;
        instagramClientDatabase = InstagramClientDatabase.getInstance(context);
        final Intent i = new Intent(ACTION);
        final InstagramPosts serializedInsPosts = new InstagramPosts();

        if (Utils.isNetworkAvailable(context)) {
            // refresh the cache when get a successful network response back
            SyncHttpClient syncClient = new SyncHttpClient();
            InstagramClient instagramClient = MainApplication.getRestClient();
            RequestParams params = new RequestParams("access_token", instagramClient.checkAccessToken().getToken());

            syncClient.get(context, InstagramClient.REST_URL + "/users/self/feed", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    List<InstagramPost> instagramPosts = Utils.decodePostsFromJsonResponse(response);
                    instagramClientDatabase.emptyAllTables();
                    instagramClientDatabase.addInstagramPosts(instagramPosts);

                    serializedInsPosts.insPosts = instagramPosts;
                    i.putExtra("serializedInsPosts", serializedInsPosts);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    Log.e(TAG, "failed to load posts to db.");
                }
            });
        } else {
            Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT).show();

            // load cached posts when there is no network connection
            List<InstagramPost> newPosts = instagramClientDatabase.getAllInstagramPosts();
            serializedInsPosts.insPosts = newPosts;
            i.putExtra("serializedInsPosts", serializedInsPosts);
            LocalBroadcastManager.getInstance(context).sendBroadcast(i);
        }
    }
}
