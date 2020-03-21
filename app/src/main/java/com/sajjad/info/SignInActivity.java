package com.sajjad.info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignInActivity extends AppCompatActivity {

    Toolbar mainToolbar;
    TextInputEditText userName, userPassword;
    Button signIn;
    SaveModel saveModel;
    CheckBox staySignedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setUpViews();
    }

    private void setUpViews() {
        saveModel = new SaveModel(this);
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        //
        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);
        signIn = findViewById(R.id.signIn);
        staySignedIn = findViewById(R.id.staySignedIn);
        signIn.setOnClickListener(signInListener);
    }

    View.OnClickListener signInListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (saveModel.getUserName().equals(userName.getText().toString())
                    && saveModel.getUserPassword().equals(userPassword.getText().toString())) {

                // check signed in status
                if (staySignedIn.isChecked()) {
                    saveModel.saveLogInStatus();
                    Intent intent = new Intent(SignInActivity.this, InfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SignInActivity.this, InfoActivity.class);
                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(SignInActivity.this, "UserName or UserPassword isn't correct, try again", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}