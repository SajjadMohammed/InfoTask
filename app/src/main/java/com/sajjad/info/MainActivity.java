package com.sajjad.info;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Toolbar mainToolbar;
    Button signIn, signUp;
    SaveModel saveModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveModel = new SaveModel(this);
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        //
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        checkLogInStatus();
    }

    public void signIn(View view) {
        if (saveModel.getLogInStatus()) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        } else {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, view, "signIn");
            Intent signInIntent = new Intent(this, SignInActivity.class);
            startActivity(signInIntent, activityOptions.toBundle());
        }
    }

    public void signUp(View view) {
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, view, "signUp");
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent, activityOptions.toBundle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (saveModel.getLogInStatus()) {
            finish();
        }
        else {
            if (saveModel.getUserName().isEmpty()) {
                signIn.setVisibility(View.GONE);
            } else {
                signIn.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.GONE);
            }
        }
    }

    private void checkLogInStatus() {
        if (saveModel.getLogInStatus()) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        } else {
            if (saveModel.getUserName().isEmpty()) {
                signIn.setVisibility(View.GONE);
            } else {
                signIn.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.GONE);
            }
        }
    }
}