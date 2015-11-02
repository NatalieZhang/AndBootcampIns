package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.SearchFragmentStatePagerAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;
import com.codepath.instagram.models.InstagramUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yuanzhang on 11/1/15.
 */
public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private SearchFragmentStatePagerAdapter searchFragmentStatePagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fragment wants to add items to the action bar
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        viewPager = (ViewPager) view.findViewById(R.id.vpSearchContainer);
        tabLayout = (TabLayout) view.findViewById(R.id.tlSearchTabs);

        searchFragmentStatePagerAdapter = new SearchFragmentStatePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(searchFragmentStatePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.vpSearchContainer, new SearchUsersResultFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.mnu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MainApplication.getRestClient().getSearchedUsers(getUsersJsonHandler(), query);
                MainApplication.getRestClient().getSearchedTags(getTagsJsonHandler(), query);

                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private JsonHttpResponseHandler getUsersJsonHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                SearchUsersResultFragment searchUsersResultFragment = (SearchUsersResultFragment)
                        searchFragmentStatePagerAdapter.getRegisteredFragment(0);

                if (searchUsersResultFragment != null) {
                    searchUsersResultFragment.onUsersLoaded((ArrayList<InstagramUser>)
                            Utils.decodeUsersFromJsonResponse(response));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e(TAG, "failed to get instagram users.");
            }
        };
    }

    private JsonHttpResponseHandler getTagsJsonHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                SearchTagsResultFragment searchTagsResultFragment = (SearchTagsResultFragment)
                        searchFragmentStatePagerAdapter.getRegisteredFragment(1);

                if (searchTagsResultFragment != null) {
                    searchTagsResultFragment.onTagsLoaded((ArrayList<InstagramSearchTag>)
                            Utils.decodeSearchTagsFromJsonResponse(response));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e(TAG, "failed to get instagram users.");
            }
        };
    }
}
