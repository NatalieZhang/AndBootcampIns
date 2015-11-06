package com.codepath.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.PhotoGridFragment;

/**
 * Created by yuanzhang on 11/1/15.
 */
public class PhotoGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);

        Intent i = getIntent();
        String id = i.getStringExtra("userId");
        String tag = i.getStringExtra("searchTag");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PhotoGridFragment photoGridFragment = PhotoGridFragment.newInstance(id, tag);
        ft.replace(R.id.flPhotoGirdContainer, photoGridFragment);
        ft.commit();
    }


}
