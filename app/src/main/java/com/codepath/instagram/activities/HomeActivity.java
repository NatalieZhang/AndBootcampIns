package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.networking.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private List<InstagramPost> instagramPosts;
    private InstagramPostsAdapter insAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        instagramPosts = new ArrayList<>();
        insAdapter = new InstagramPostsAdapter(instagramPosts, HomeActivity.this);

        if (Utils.isNetworkAvailable(this)) {
            String url = "https://api.instagram.com/v1/media/popular?client_id=f8272bdb0a5549bfbbd44e5026144bba";
            InstagramClient.getPopularFeed(getJsonHandler(), url);
        } else {
            Toast.makeText(this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }

        RecyclerView postsRecyclerView = (RecyclerView) findViewById(R.id.rvInsPost);
        postsRecyclerView.setAdapter(insAdapter);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SimpleVerticalSpacerItemDecoration itemDecoration = new SimpleVerticalSpacerItemDecoration(24);
        postsRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
