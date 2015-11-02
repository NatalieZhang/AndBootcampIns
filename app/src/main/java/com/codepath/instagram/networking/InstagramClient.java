package com.codepath.instagram.networking;

import android.content.Context;

import com.codepath.instagram.helpers.Constants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.scribe.builder.api.Api;

/**
 * Created by yuanzhang on 10/28/15.
 */
public class InstagramClient extends OAuthBaseClient {
    public static final String  REST_URL = "https://api.instagram.com/v1";
    public static final Class<? extends Api> REST_API_CLASS = InstagramApi.class;
    public static final String REST_CONSUMER_KEY = "7f5321002cc04089b778e463cd87953f";
    public static final String REST_CONSUMER_SECRET = "a9980e6933814fd3848dba9f6b370b63";
    public static final String  REDIRECT_URI = Constants.REDIRECT_URI;
    public static final String  SCOPE = Constants.SCOPE;


    public InstagramClient(Context c) {
        super(c, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET, REDIRECT_URI, SCOPE);

    }

    // get all comments in a post
    public void getComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "/media/" + mediaId + "/comments", responseHandler);
    }

    // get popular feeds
    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "/media/popular", responseHandler);
    }

    // get user's own feeds
    public void getUserFeed(JsonHttpResponseHandler responseHandler) {
        client.get(REST_URL + "/users/self/feed", responseHandler);
    }

    public void getSearchedUsers(JsonHttpResponseHandler responseHandler, String searchTerm) {
        client.get(REST_URL + "/users/search?q=" + searchTerm, responseHandler);
    }

    public void getSearchedTags(JsonHttpResponseHandler responseHandler, String searchTerm) {
        client.get(REST_URL + "/tags/search?q=" + searchTerm, responseHandler);
    }
}
