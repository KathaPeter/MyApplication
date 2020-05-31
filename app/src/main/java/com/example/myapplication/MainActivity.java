package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public void btLoginEvent(View v) {
        EditText emailInput = (EditText) findViewById(R.id.input_user);
        EditText pwdInput = (EditText) findViewById(R.id.input_pwd);

        mAuth.signInWithEmailAndPassword(emailInput.getText().toString(),
                pwdInput.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login erfolgreich",
                            Toast.LENGTH_LONG).show();
                    Log.d(MainActivity.class.toString(), "signInWithEmailAndPassword:success");
                    Intent intent = prepareIntentForWelcome(task.getResult().getUser());
                    startActivity(intent);
                } else {
                    Log.w(MainActivity.class.toString(), "signInWithEmailAndPassword:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO ADE: Remove signOut when we got a logout button
        mAuth.signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Log.d(MainActivity.class.toString(), "getCurrentUser:success");
            Toast.makeText(getApplicationContext(), "Willkommen zurück",
                    Toast.LENGTH_LONG).show();
            Intent intent = prepareIntentForWelcome(currentUser);
            startActivity(intent);
        }else {
            Log.w(MainActivity.class.toString(), "signInWithEmailAndPassword:failure");
        }
       // updateUI(currentUser);
    }

    private Intent prepareIntentForWelcome(  FirebaseUser currentUser){
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra("user_uid", currentUser.getUid());
        return intent;
    }
}
