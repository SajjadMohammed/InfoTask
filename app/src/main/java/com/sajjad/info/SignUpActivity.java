package com.sajjad.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    Toolbar mainToolbar;
    TextInputEditText userName, userPassword;
    Button signUp;
    SaveModel saveModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpViews();
    }

    private void setUpViews() {
        saveModel = new SaveModel(this);
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);
        signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(signUpListener);
    }

    View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String uName = userName.getText().toString();
            String password = userPassword.getText().toString();
            if (uName.isEmpty() || password.isEmpty()) {
                Snackbar.make(view, "Please, enter user name and password", Snackbar.LENGTH_LONG)
                        .setAction("OK", null)
                        .show();
            } else {
                saveModel.saveUserName(uName);
                saveModel.saveUserPassword(password);
                Intent intent = new Intent(SignUpActivity.this, InfoActivity.class);
                startActivity(intent);
                finish();
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