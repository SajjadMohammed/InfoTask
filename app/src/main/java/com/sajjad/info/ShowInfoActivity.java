package com.sajjad.info;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sajjad.info.Adapter.PersonInfo;

import java.util.List;

public class ShowInfoActivity extends AppCompatActivity {

    Toolbar mainToolbar;
    SQLiteHelper sqLiteHelper;
    PersonInfo personInfo;
    //
    ImageView personPicture;
    TextView personName, personSaygin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        //
        setUpViews();
    }

    private void setUpViews() {
        // views
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        personPicture = findViewById(R.id.personImage);
        personName = findViewById(R.id.personName);
        personSaygin = findViewById(R.id.personSaying);
        // get all person information
        sqLiteHelper = new SQLiteHelper(this);
        personInfo = sqLiteHelper.getPersonAt(getIntent().getIntExtra("personId", 0));
        //
        personName.setText(personInfo.getPersonName());
        personSaygin.setText(personInfo.getPersonSaying());
        byte[] bytes = personInfo.getPersonPicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        personPicture.setImageBitmap(bitmap);
    }
}