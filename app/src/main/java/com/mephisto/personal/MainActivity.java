package com.mephisto.personal;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MainActivity extends BaseActivity {

    EditText editUsername, editPassword;
    Button buttonIngresar, buttonRegistrar, buttonGoogle, buttonTwitter, buttonFacebook, buttonMicrosoft;
    TextView textviewRecuperarContraseña;
    private static final String TAGEmail = "EmailPassword";
    private static final String TAGGoogle = "GoogleLogin";
    private static final String TAGMessaging = "Messaging";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //  BUTTONS
        buttonIngresar = findViewById(R.id.buttonIngresar);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        //  EDITTEXT
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        textviewRecuperarContraseña  = findViewById(R.id.textForgorPassword);
        Intent registerIntent = new Intent(this, RegisterActivity.class);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAGMessaging, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.d("Token", token);
                    }
                });

        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                String email = editUsername.getText().toString();
                String pass = editPassword.getText().toString();
                if (pass.equals("")) {
                    hideProgressDialog();
                    Toast.makeText(MainActivity.this, R.string.loginEmptyPass, Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    hideProgressDialog();
                    Toast.makeText(MainActivity.this, R.string.loginEmptyMail, Toast.LENGTH_SHORT).show();
                } else {
                    signIn(email, pass);
                }
            }
        });

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(registerIntent);
            }
        });

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        showProgressDialog();
        if (currentUser != null) {
            updateUI(currentUser);
        }
        hideProgressDialog();
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAGEmail, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            hideProgressDialog();
                            updateUI(user);
                        } else {
                            Log.w(TAGEmail,"signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, getString(R.string.loginTextIncorrectPass), Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAGGoogle, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        showProgressDialog();
        AuthCredential credencial = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credencial)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAGGoogle, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAGGoogle, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void googleSignIn() {
        Intent signInGoogle = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInGoogle, RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        closeApp();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent launchIntent = new Intent(getApplicationContext(), PrincipalActivity.class);
            startActivity(launchIntent);
        }
        hideProgressDialog();
    }

}