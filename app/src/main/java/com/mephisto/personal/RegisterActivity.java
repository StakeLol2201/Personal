package com.mephisto.personal;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mephisto.personal.Classes.BaseActivity;

public class RegisterActivity extends BaseActivity {

    EditText registerUsername, registerPassword, registerRepeatPassword;

    Button registerButtonRegister;
    
    private static final String TAGEmail = "EmailPassword";

    private FirebaseAuth mAuth;

    String username, password, repassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUsername = findViewById(R.id.editRegisterUsername);
        registerPassword = findViewById(R.id.editRegisterPassword);
        registerRepeatPassword = findViewById(R.id.editRegisterRepeatPassword);

        registerButtonRegister = findViewById(R.id.buttonRegisterRegistrar);

        mAuth = FirebaseAuth.getInstance();

        registerButtonRegister.setOnClickListener(view -> {

            username = registerUsername.getText().toString();
            password = registerPassword.getText().toString();
            repassword = registerRepeatPassword.getText().toString();

            if (password.equals(repassword)) {

                showProgressDialog();

                createAccount(username, password);

            } else {

                Toast.makeText(RegisterActivity.this, getString(R.string.registerTextPassNotEquals), Toast.LENGTH_SHORT).show();

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void createAccount(String email, String password) {

        Intent loginIntent = new Intent(this, MainActivity.class);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, task -> {

                        if (task.isSuccessful()) {

                            Log.d(TAGEmail, "createUserWithEmail:success");

                        } else {

                            Log.w(TAGEmail, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, getText(R.string.registerTextError), Toast.LENGTH_SHORT).show();

                        }

                    });

            hideProgressDialog();

            startActivity(loginIntent);

    }

}