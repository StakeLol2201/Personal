package com.mephisto.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class PrincipalActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    String pais;
    Button buttonPrincipalDisconnect;
    Spinner spinnerPaises;
    ArrayList<String> paises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        buttonPrincipalDisconnect = findViewById(R.id.buttonPrincipalDisconnect);
        spinnerPaises = findViewById(R.id.spinnerPaises);

        paises = getPaises();

        spinnerPaises.setAdapter(new ArrayAdapter<String>(PrincipalActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises));

        buttonPrincipalDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private ArrayList<String> getPaises() {
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> paises = new ArrayList<String>();
        for (Locale locale : locales) {
            pais = locale.getDisplayCountry();
            if (pais.trim().length() > 0 && !paises.contains(pais)) {
                paises.add(pais);
            }
        }
        Collections.sort(paises);
        return paises;
    }

    private void signOut() {
        showProgressDialog();
        FirebaseAuth.getInstance().signOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Intent launchIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(launchIntent);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setTitle("Cerrar aplicación");
        exitDialog.setIcon(R.mipmap.ic_launcher);
        exitDialog.setMessage("¿Seguro de cerrar la aplicación?")
                .setCancelable(false)
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = exitDialog.create();
        alert.show();
    }

}