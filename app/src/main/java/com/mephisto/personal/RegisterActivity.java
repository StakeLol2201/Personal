package com.mephisto.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {

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

        registerButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = registerUsername.getText().toString();
                password = registerPassword.getText().toString();
                repassword = registerRepeatPassword.getText().toString();

                createAccount(username, password ,repassword);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void createAccount(String email, String password, String repassword) {

        Intent loginIntent = new Intent(this, MainActivity.class);

        if (password.equals(repassword)) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Log.d(TAGEmail, "createUserWithEmail:success");

                                startActivity(loginIntent);
                                
                            } else {
                                
                                Log.w(TAGEmail, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, getText(R.string.registerTextError), Toast.LENGTH_SHORT).show();
                                
                            }
                            
                        }
                    });

        } else {

            Toast.makeText(this, getString(R.string.registerButtonRegister), Toast.LENGTH_SHORT).show();

        }

    }

}