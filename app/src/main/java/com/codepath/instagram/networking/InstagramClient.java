package com.codepath.instagram.networking;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by yuanzhang on 10/28/15.
 */
public class InstagramClient {

    public static void getPopularFeed(JsonHttpResponseHandler responseHandler, String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, responseHandler);
    }
}
