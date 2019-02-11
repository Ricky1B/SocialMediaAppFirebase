package com.rikin.jain.socialmediaappfirebase;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSIgnUp, btnSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSIgnUp = findViewById(R.id.btnSignUp);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
       btnSIgnUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               signUp();
           }
       });
       btnSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               signIn();
           }
       });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //transition to next activity
            transitionToSocialMediaActivity();
        }

    }
    private void signUp(){
        if(edtEmail.getText().toString().equals("") || edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
            Toast.makeText(this,"Username, Email or Password cannot be empty",Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseDatabase.getInstance().getReference().child("my_users").child(task.getResult().getUser().getUid()).child("username").setValue(edtUsername.getText().toString());
                        Toast.makeText(MainActivity.this,"SignUp successful",Toast.LENGTH_SHORT).show();
                        transitionToSocialMediaActivity();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(edtUsername.getText().toString())
                                .build();

                        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                           Toast.makeText(MainActivity.this,"Display Name Updated",Toast.LENGTH_SHORT).show();                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(MainActivity.this,"SignUp failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    private void signIn(){
        if(edtEmail.getText().toString().equals("") || edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
            Toast.makeText(this,"Username, Email or Password cannot be empty",Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "SignIn successful", Toast.LENGTH_SHORT).show();
                        transitionToSocialMediaActivity();
                    } else {
                        Toast.makeText(MainActivity.this, "SignIn failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void transitionToSocialMediaActivity(){
        Intent intent = new Intent(this,SocialMedia.class);
        startActivity(intent);
        finish();
    }
}
