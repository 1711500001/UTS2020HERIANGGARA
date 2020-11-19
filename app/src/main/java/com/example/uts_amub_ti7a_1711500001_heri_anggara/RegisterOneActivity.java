package com.example.uts_amub_ti7a_1711500001_heri_anggara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

public class RegisterOneActivity extends AppCompatActivity {
    EditText Username, Password, Email;
    Button btnN;
    String USERNAME_KEY=" usernamekey";
    String username_key="";
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);
        Username = findViewById(R.id.txtUsername);
        Password = findViewById(R.id.txtPassword);
        Email = findViewById(R.id.txtEmail);
        btnN = findViewById(R.id.btnNext);
        btnN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Silahkan tunggu, sedang dalam proses", Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, Username.getText().toString());
                editor.apply();

                //Proses Simpan ke Database Firebase
                reference = FirebaseDatabase.getInstance().getReference().child("Register One").child(Username.getText().toString());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("username").setValue(Username.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(Password.getText().toString());
                        dataSnapshot.getRef().child("email").setValue(Email.getText().toString());
                        Toast.makeText(getApplicationContext(), "Tersimpan", Toast.LENGTH_SHORT).show();
                        Intent gotonextregister = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
                        startActivity(gotonextregister);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}