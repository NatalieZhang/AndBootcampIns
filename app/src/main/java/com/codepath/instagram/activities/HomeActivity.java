package com.codepath.instagram.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.helpers.SimpleVerticalSpacerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;

import org.json.JSONObject;

import java.util.List;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private List<InstagramPost> instagramPosts;
    private RecyclerView postsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        instagramPosts = fetchPosts(getApplicationContext(), "popular.json");

        RecyclerView postsRecyclerView = (RecyclerView) findViewById(R.id.rvInsPost);
        InstagramPostsAdapter insAdapter = new InstagramPostsAdapter(instagramPosts, this);
        postsRecyclerView.setAdapter(insAdapter);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SimpleVerticalSpacerItemDecoration itemDecoration = new SimpleVerticalSpacerItemDecoration(24);
        postsRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private List<InstagramPost> fetchPosts(Context context, String fileName) {
        JSONObject postsJsonResponse = null;

        try {
            postsJsonResponse = Utils.loadJsonFromAsset(context, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Utils.decodePostsFromJsonResponse(postsJsonResponse);
    }
}
