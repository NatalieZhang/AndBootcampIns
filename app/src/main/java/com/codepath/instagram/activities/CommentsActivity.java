package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramCommentsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {
    private static final String TAG = "CommentsActivity";

    private List<InstagramComment> instagramComments;
    private InstagramCommentsAdapter insCommentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        instagramComments = new ArrayList<>();
        insCommentsAdapter = new InstagramCommentsAdapter(instagramComments, CommentsActivity.this);

        MainApplication.getRestClient().getComments(getIntent().getStringExtra("mediaId"), getJsonHandler());
        RecyclerView commentsRecyclerView = (RecyclerView) findViewById(R.id.rvComments);
        commentsRecyclerView.setAdapter(insCommentsAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comments, menu);
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
                instagramComments.addAll(Utils.decodeCommentsFromJsonResponse(response));
                insCommentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e(TAG, "failed to get instagram comments data");
            }
        };
    }
}
