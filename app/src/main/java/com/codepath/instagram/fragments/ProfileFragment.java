package com.codepath.instagram.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yuanzhang on 10/30/15.
 */
public class ProfileFragment extends Fragment {
    private static final String SELF_USER_ID = "145779633";

    private Context context;
    private SimpleDraweeView sdvImageInProfilePage;
    private TextView tvFollowersCount;
    private TextView tvFollowingCount;
    private TextView tvPostCount;
    private TextView tvPosts;
    private TextView tvFollowers;
    private TextView tvFollowing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        sdvImageInProfilePage = (SimpleDraweeView) view.findViewById(R.id.sdvImageInProfilePage);
        tvPostCount = (TextView) view.findViewById(R.id.tvPostCount);
        tvFollowersCount = (TextView) view.findViewById(R.id.tvFollowersCount);
        tvFollowingCount = (TextView) view.findViewById(R.id.tvFollowingCount);
        tvPosts = (TextView) view.findViewById(R.id.tvPostsInProfile);
        tvFollowers = (TextView) view.findViewById(R.id.tvFollowersInProfile);
        tvFollowing = (TextView) view.findViewById(R.id.tvFollowingInProfile);

        MainApplication.getRestClient().getSelfUser(getSelfHandler());


        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        PhotoGridFragment photoGridFragment = PhotoGridFragment.newInstance("self", "");
        ft.replace(R.id.flProfilePostContainer, photoGridFragment);
        ft.commit();
    }

    private JsonHttpResponseHandler getSelfHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    JSONObject counts = jsonObject.getJSONObject("counts");
                    tvPostCount.setText(counts.getString("media"));
                    tvFollowersCount.setText(counts.getString("followed_by"));
                    tvFollowingCount.setText(counts.getString("follows"));
                    sdvImageInProfilePage.setImageURI(Uri.parse(jsonObject.getString("profile_picture")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }

}
