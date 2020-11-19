package com.example.uts_amub_ti7a_1711500001_heri_anggara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwoActivity extends AppCompatActivity {
    Button btnRe,btnAdd;
    ImageView ImageContainer;
    EditText hobbi, alamat;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY=" usernamekey";
    String username_key="";
    String username_key_new="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        getUsernameLocal();
        hobbi = findViewById(R.id.txtHobbi);
        alamat = findViewById(R.id.txtAlamat);
        btnRe =findViewById(R.id.btnRegister);
        btnAdd =findViewById(R.id.btnAdd);
        ImageContainer = findViewById(R.id.imageContainer);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
        btnRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Register Two").child(username_key_new);
                storage = FirebaseStorage.getInstance().getReference().child("Photouser").child(username_key_new);
                System.out.println("photo_location: "+photo_location);
                if(photo_location != null){
                    Toast.makeText(getApplicationContext(), "Silahkan tunggu, sedang dalam proses", Toast.LENGTH_LONG).show();
                    StorageReference storageReference = storage.child(System.currentTimeMillis() + " "+
                            getFileExtension(photo_location));
                    storageReference.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference.getRef().child("url_photo_profile").toString();
                                    reference.getRef().child("hobbi").setValue(hobbi.getText().toString());
                                    reference.getRef().child("alamat").setValue(alamat.getText().toString());
                                    Toast.makeText(getApplicationContext(), "Tersimpan", Toast.LENGTH_LONG).show();
                                }
                            }).addOnCompleteListener(task -> {
                                Intent gotonextregister = new Intent(RegisterTwoActivity.this, MainActivity.class);
                                startActivity(gotonextregister);});


                }


            }
        });
    }
    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == photo_max && resultCode == RESULT_OK && data !=null && data.getData()!=null){
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(ImageContainer);
        }
    }
}