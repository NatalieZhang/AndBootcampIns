package com.codepath.instagram.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.SearchUserResultsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhang on 10/30/15.
 */
public class SearchUsersResultFragment extends Fragment {
    private static final String TAG = "UsersResultFragment";

    private SearchUserResultsAdapter searchUserResultsAdapter;
    private Context context;
    private List<InstagramUser> searchedUsers;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //fragment wants to add items to the action bar
//        setHasOptionsMenu(true);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_search, container, false);
        context = getActivity();
        searchedUsers = new ArrayList<>();
        searchUserResultsAdapter = new SearchUserResultsAdapter(searchedUsers, context);

        RecyclerView usersRecyclerView = (RecyclerView) view.findViewById(R.id.rvInsSearch);
        usersRecyclerView.setAdapter(searchUserResultsAdapter);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.mnu_search);
        int id = searchItem.getItemId();
        Log.e(TAG, "haha : search users: menu id " + id) ;
        if (id == R.id.mnu_search) {
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (Utils.isNetworkAvailable(context)) {
                        MainApplication.getRestClient().getSearchedUsers(getJsonHandler(), query);
                    } else {
                        Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT).show();
                    }

                    // Reset SearchView
                    searchView.clearFocus();
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                    searchItem.collapseActionView();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
    }

    private JsonHttpResponseHandler getJsonHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                searchedUsers.addAll(Utils.decodeUsersFromJsonResponse(response));
                searchUserResultsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.e(TAG, "failed to get instagram users.");
            }
        };
    }

    public void onUsersLoaded(ArrayList<InstagramUser> users) {
        searchedUsers.clear();
        searchedUsers.addAll(users);
        searchUserResultsAdapter.notifyDataSetChanged();
    }
}
