package com.codepath.instagram.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.instagram.fragments.SearchTagsResultFragment;
import com.codepath.instagram.fragments.SearchUsersResultFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

/**
 * Created by yuanzhang on 11/1/15.
 */
public class SearchFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {
    private String tabTitles[] = new String[] {"USERS", "TAGS"};
    private static int NUM_ITEMS = 2;

    public SearchFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchUsersResultFragment();
            case 1:
                return new SearchTagsResultFragment();
            default:
                return new SearchUsersResultFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
