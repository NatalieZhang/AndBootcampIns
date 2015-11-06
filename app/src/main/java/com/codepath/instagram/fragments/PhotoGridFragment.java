package com.codepath.instagram.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.PhotoGridAdapter;
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
 * Created by yuanzhang on 11/1/15.
 */
public class PhotoGridFragment extends Fragment {
    private List<InstagramPost> instagramPosts;
    private PhotoGridAdapter photoGridAdapter;
    private Context context;

    public static PhotoGridFragment newInstance(String id, String tag) {
        PhotoGridFragment photoGridFragment = new PhotoGridFragment();
        Bundle args = new Bundle();
        args.putString("userId", id);
        args.putString("searchTag", tag);
        photoGridFragment.setArguments(args);

        return photoGridFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = getArguments().getString("userId", "");
        String tag = getArguments().getString("searchTag", "");

        if (id != "") {
            if (id == "self") {
                MainApplication.getRestClient().getSelfPhotos(getHandler());
            } else {
                MainApplication.getRestClient().getUserPhotos(getHandler(), id);
            }
        } else if (tag != ""){
            MainApplication.getRestClient().getTagPhotos(getHandler(), tag);
        }
    }

    private JsonHttpResponseHandler getHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                instagramPosts.clear();
                instagramPosts.addAll(Utils.decodePostsFromJsonResponse(response));
                photoGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("haha", "haha : failed to get user photos");
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_grid, container, false);

        context = getActivity();
        instagramPosts = new ArrayList<>();
        photoGridAdapter = new PhotoGridAdapter(instagramPosts, context);


        RecyclerView photoGridRecyclerView = (RecyclerView) view.findViewById(R.id.rvPhotoGrid);
        photoGridRecyclerView.setAdapter(photoGridAdapter);
        photoGridRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        SimpleVerticalSpacerItemDecoration itemDecoration = new SimpleVerticalSpacerItemDecoration(24);
        photoGridRecyclerView.addItemDecoration(itemDecoration);

        return view;
    }
}
