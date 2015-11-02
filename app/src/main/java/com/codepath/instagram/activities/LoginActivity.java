package com.codepath.instagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codepath.instagram.R;
import com.codepath.instagram.networking.InstagramClient;
import com.codepath.oauth.OAuthLoginActivity;

public class LoginActivity extends OAuthLoginActivity<InstagramClient> {

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             loginToRest(v);
            }
        });
    }

    @Override
    public void onLoginSuccess() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginFailure(Exception e) {

    }

    // Method to be called to begin the authentication process
    // assuming user is not authenticated.
    // Typically used as an event listener for a button for the user to press.
    public void loginToRest(View view) {
        getClient().connect();
    }
}
