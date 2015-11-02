package com.codepath.instagram.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhang on 10/30/15.
 */
public class PostsFragment extends Fragment {
    private static final String TAG = "PostsFragment";

    private List<InstagramPost> instagramPosts;
    private InstagramPostsAdapter insAdapter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        context = getActivity();
        instagramPosts = new ArrayList<>();
        insAdapter = new InstagramPostsAdapter(instagramPosts, context);

        if (Utils.isNetworkAvailable(context)) {
            MainApplication.getRestClient().getUserFeed(getJsonHandler());
        } else {
            Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }

        RecyclerView postsRecyclerView = (RecyclerView) view.findViewById(R.id.rvInsPost);
        postsRecyclerView.setAdapter(insAdapter);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        SimpleVerticalSpacerItemDecoration itemDecoration = new SimpleVerticalSpacerItemDecoration(24);
        postsRecyclerView.addItemDecoration(itemDecoration);

        return view;
    }

    private JsonHttpResponseHandler getJsonHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                instagramPosts.addAll(Utils.decodePostsFromJsonResponse(response));
                insAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e(TAG, "failed to get instagram posts data.");
            }
        };
    }
}
