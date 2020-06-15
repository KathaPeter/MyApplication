package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public void btLoginEvent(View v) {

        EditText emailInput = (EditText) findViewById(R.id.input_user);
        EditText pwdInput = (EditText) findViewById(R.id.input_pwd);
        String userEMail = emailInput.getText().toString().trim();
        String userPWD = pwdInput.getText().toString().trim();

        if (Globals.USE_UID_AS_USER != null) {
            Intent intent = prepareIntentForWelcome(Globals.USE_UID_AS_USER);
            intent.putExtra("user_email", "<dummyLogin>");
            startActivityForResult(intent, 1);
        } else if (userEMail.length() == 0 || userPWD.length() == 0) {
            Toast.makeText(getApplicationContext(), "EMail and Password required!",
                    Toast.LENGTH_LONG).show();
        } else {

            Task<AuthResult> loginTask = mAuth.signInWithEmailAndPassword(userEMail, userPWD)   //
                    .addOnCompleteListener(this, (@NonNull Task<AuthResult> task) -> {

                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login erfolgreich", Toast.LENGTH_LONG).show();
                            Log.d(MainActivity.class.toString(), "signInWithEmailAndPassword:success");
                            Intent intent = prepareIntentForWelcome(task.getResult().getUser().getUid());
                            intent.putExtra("user_email", userEMail);
                            startActivityForResult(intent, 2);
                        } else {
                            authenticationFailed(task.getException());
                        }

                    }) //
                    .addOnFailureListener((fExc) -> {
                        authenticationFailed(fExc);
                    });
        }
    }

    private void authenticationFailed(Exception exc) {
        Log.w(MainActivity.class.toString(), "signInWithEmailAndPassword:failure", exc);
        Toast.makeText(getApplicationContext(), "No authentication possible. Please proove your login details.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            mAuth.signOut();
        } else if (requestCode == 1) {
            //NO logout needed
        } else {
            //??
        }

        Log.d("MainActivity.class", "onActivityResult: " + requestCode + " " + resultCode + " "+ data);
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

        FirebaseUser currentUser = mAuth.getCurrentUser();

        Log.d(MainActivity.class.toString(), "OnStart: CurrentUser is" + currentUser);

        if (currentUser != null) {
            Log.d(MainActivity.class.toString(), "onStart getCurrentUser:success");
            Toast.makeText(getApplicationContext(), "Willkommen zurück",
                    Toast.LENGTH_LONG).show();
            Intent intent = prepareIntentForWelcome(currentUser.getUid());
            intent.putExtra("user_email", currentUser.getEmail());
            startActivityForResult(intent, 2);
        } else {
            Log.d(MainActivity.class.toString(), "onStart signInWithEmailAndPassword:failure");
        }

    }

    private Intent prepareIntentForWelcome(String userID) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra("user_uid", userID);
        return intent;
    }
}
