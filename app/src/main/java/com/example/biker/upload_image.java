package com.example.biker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import static com.example.biker.user.user_service_data.setProblem_image;
import static com.example.biker.user.user_service_data.setProblem_image_flag;

public class upload_image extends AppCompatActivity {
    public static Activity upload_image_activity;
    ImageView upload_image;
    Button next;
    Boolean profile_image_flag_this = false;
    Bitmap bitmap;

    private static final int FILECHOOSER_RESULTCODE = 2888;
    private static final int MULTI_PERMISSION_REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);

        upload_image_activity = upload_image.this;
        upload_image=findViewById(R.id.upload_image);
        next=findViewById(R.id.next);

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takingPhoto();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread() {
                    @Override
                    public void run() {
                        if (profile_image_flag_this) {
                            setProblem_image_flag(true);
                            setProblem_image(bitmap);
                        } else {
                            setProblem_image_flag(false);
                        }
                    }
                }.start();

                startActivity(new Intent(upload_image.this, bike_service_location.class));
            }
        });

    }


    // Task Related to PROFILE IMAGE
    Intent cameraIntent, chooserIntent;

    private void takingPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    //Manifest.permission_group.STORAGE
            }, MULTI_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == FILECHOOSER_RESULTCODE && resultCode == Activity.RESULT_OK) {
                final Bitmap photo;
                if (data.getData() == null) {
                    photo = (Bitmap) data.getExtras().get("data");
                } else {
                    Uri galleryImageUri = data.getData();
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), galleryImageUri);
                }
                upload_image.setImageBitmap(photo);
                profile_image_flag_this = true;
                bitmap = photo;
            }
        } catch (Exception e) {
            profile_image_flag_this = false;
            e.printStackTrace();
        }
    }

    // used for multiple permission in a single request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTI_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    Intent otherIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    otherIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    otherIntent.setType("image/*");

                    chooserIntent = Intent.createChooser(otherIntent, "Upload Image via");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{cameraIntent});

                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    chooserIntent = Intent.createChooser(cameraIntent, "Upload Image via");

                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Intent otherIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    otherIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    otherIntent.setType("image/*");

                    chooserIntent = Intent.createChooser(otherIntent, "Upload Image via");

                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    //Snackbar.make(MainActivity.this,"Required Permissions DENIED",Snackbar.LENGTH_SHORT).show(); // Showing ERROR
                    Toast.makeText(this, "Required Permissions DENIED, You can't set Profile Image until you grant required permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, "Something went wrong..........", Toast.LENGTH_SHORT).show();
        }
    }

}