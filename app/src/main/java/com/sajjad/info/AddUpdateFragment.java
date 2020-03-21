package com.sajjad.info;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.sajjad.info.Adapter.PersonInfo;
import com.sajjad.info.Adapter.PersonRecyclerAdapter;
import com.sajjad.info.Adapter.SliderPagerAdapter;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class AddUpdateFragment extends DialogFragment {

    private Button addPerson, updatePerson, browsePicture;
    private TextInputEditText personName, personSaying;
    private ImageView personPicture;
    private SQLiteHelper sqLiteHelper;
    private String cameraPermission = Manifest.permission.CAMERA;
    private int cameraRequestCode = 104, captureCode = 103;
    private byte[] imageInByte;
    PersonInfo personInfo;

    public AddUpdateFragment() {
    }

    public static AddUpdateFragment newInstance(int id) {
        AddUpdateFragment frag = new AddUpdateFragment();
        Bundle args = new Bundle();
        args.putInt("personId", id);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_update_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // setting up views
        setUpViews(view);
        // init. database
        sqLiteHelper = new SQLiteHelper(view.getContext());
        // showing info
        showInfo();
        // browse image from gallery
        browsePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
        // insert person info into database
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(view);

            }
        });
        // update person info
        updatePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(view);
            }
        });
    }

    private void update(View view) {
        sqLiteHelper.updatePerson(personInfo.getId(),
                personName.getText().toString()
                , personSaying.getText().toString()
                , getImageInBytes(personPicture));
        dismiss();

    }

    private void insert(View view) {
        sqLiteHelper.insertPerson(
                personName.getText().toString()
                , personSaying.getText().toString()
                , imageInByte);
        dismiss();
    }

    private void showInfo() {
        int id = getArguments().getInt("personId");
        // -1 for add person, so we don't need to showing information
        if (id != -1) {
            personInfo = sqLiteHelper.getPersonAt(id);
            personName.setText(personInfo.getPersonName());
            personSaying.setText(personInfo.getPersonSaying());
            personPicture.setImageBitmap(
                    BitmapFactory.decodeByteArray(personInfo.getPersonPicture(), 0
                            , personInfo.getPersonPicture().length));
        }
    }

    private void setUpViews(View view) {
        addPerson = view.findViewById(R.id.addPerson);
        updatePerson = view.findViewById(R.id.updatePerson);
        browsePicture = view.findViewById(R.id.browseImage);
        personName = view.findViewById(R.id.personName);
        personSaying = view.findViewById(R.id.personSaying);
        personPicture = view.findViewById(R.id.personImage);
    }

    private void captureImage() {
        // must check permission for camera at runtime
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(getContext(), cameraPermission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{cameraPermission}, cameraRequestCode);
            } else {
                capture();
            }
        } else {
            capture();
        }
    }

    private void capture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, captureCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == captureCode && resultCode == RESULT_OK) {
            if (data != null) {
                // display image from camera
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                personPicture.setImageBitmap(photo);
                // picture into array of bytes
                imageInByte = getImageInBytes(personPicture);
            }
        }
    }

    private byte[] getImageInBytes(ImageView personPicture) {
        // convert image into bytes
        Bitmap bitmap = ((BitmapDrawable) personPicture.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        ((InfoActivity)getActivity()).personRecyclerAdapter=new PersonRecyclerAdapter(getContext(),sqLiteHelper.getAll());
        ((InfoActivity)getActivity()).infoRecyclerView.setAdapter(((InfoActivity)getActivity()).personRecyclerAdapter);
        ((InfoActivity)getActivity()).sliderPagerAdapter = new SliderPagerAdapter(sqLiteHelper.getLastThree());
        ((InfoActivity)getActivity()).sliderPager.setAdapter(((InfoActivity)getActivity()).sliderPagerAdapter);
        super.onDismiss(dialog);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == cameraRequestCode
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            capture();
        }
    }
}