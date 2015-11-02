package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.HomeFragmentStatePagerAdapter;
import com.codepath.instagram.fragments.PostsFragment;

public class HomeActivity extends AppCompatActivity {
    private HomeFragmentStatePagerAdapter homeFragmentStatePagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = (ViewPager) findViewById(R.id.vpNonSwipeViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tlTabs);

        homeFragmentStatePagerAdapter = new HomeFragmentStatePagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(homeFragmentStatePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.vpNonSwipeViewPager, new PostsFragment());
        fragmentTransaction.commit();
    }
}
