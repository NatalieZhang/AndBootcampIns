package com.codepath.instagram.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.Services.InstagramClientService;
import com.codepath.instagram.abstracts.EndlessScrollListener;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
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
    private SwipeRefreshLayout swipeContainer;
    private String currentUrl;

    private InstagramClientDatabase instagramClientDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        // start service
        Intent i = InstagramClientService.newIntent(context);
        context.startService(i);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        instagramPosts = new ArrayList<>();
        insAdapter = new InstagramPostsAdapter(instagramPosts, context);

        instagramClientDatabase = InstagramClientDatabase.getInstance(getContext());

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPosts();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchPosts();
        final RecyclerView postsRecyclerView = (RecyclerView) view.findViewById(R.id.rvInsPost);
        postsRecyclerView.setAdapter(insAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        postsRecyclerView.setLayoutManager(layoutManager);
        SimpleVerticalSpacerItemDecoration itemDecoration = new SimpleVerticalSpacerItemDecoration(24);
        postsRecyclerView.addItemDecoration(itemDecoration);


        postsRecyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                MainApplication.getRestClient().getMoreFeeds(getMoreHandler(), currentUrl);
            }
        });

        return view;
    }

    private JsonHttpResponseHandler getMoreHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                instagramPosts.addAll(Utils.decodePostsFromJsonResponse(response));
                insAdapter.notifyDataSetChanged();

                try {
                    JSONObject jsonObject = response.getJSONObject("pagination");
                    currentUrl = jsonObject.getString("next_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                instagramClientDatabase.addInstagramPosts(instagramPosts);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(InstagramClientService.ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            InstagramPosts serializedInsPosts = (InstagramPosts) intent.getSerializableExtra("serializedInsPosts");

            instagramPosts.clear();
            instagramPosts.addAll(serializedInsPosts.insPosts);
            insAdapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        }
    };

    private void fetchPosts() {
        if (Utils.isNetworkAvailable(context)) {
            MainApplication.getRestClient().getUserFeed(getJsonHandler());
            // get next url

        } else {
            Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT).show();

            // load cached posts when there is no network connection
            List<InstagramPost> newPosts = instagramClientDatabase.getAllInstagramPosts();
            instagramPosts.clear();
            instagramPosts.addAll(newPosts);
            insAdapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        }
    }

    private JsonHttpResponseHandler getJsonHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                instagramPosts.clear();
                instagramPosts.addAll(Utils.decodePostsFromJsonResponse(response));
                insAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

                try {
                    JSONObject jsonObject = response.getJSONObject("pagination");
                    currentUrl = jsonObject.getString("next_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // refresh the cache when get a successful network response back
                instagramClientDatabase.emptyAllTables();
                instagramClientDatabase.addInstagramPosts(instagramPosts);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e(TAG, "failed to get instagram posts data.");
            }
        };
    }
}
