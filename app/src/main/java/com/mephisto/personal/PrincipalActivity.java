package com.mephisto.personal;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrincipalActivity extends BaseActivity {

    //private FirebaseAuth mAuth;
    //private GoogleSignInClient mGoogleSignInClient;
    Button buttonPrincipalDisconnect;

    /*Spinner spinnerPaises;
    ArrayList<String> paises;
    String pais;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        buttonPrincipalDisconnect = findViewById(R.id.buttonPrincipalDisconnect);
        /*spinnerPaises = findViewById(R.id.spinnerPaises);
        paises = getPaises();
        spinnerPaises.setAdapter(new ArrayAdapter<String>(PrincipalActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises));*/
        buttonPrincipalDisconnect.setOnClickListener(view -> signOut());
    }

    /*private ArrayList<String> getPaises() {
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
    }*/

    private void signOut() {
        showProgressDialog();
        FirebaseAuth.getInstance().signOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Intent launchIntent = new Intent(this, MainActivity.class);
            startActivity(launchIntent);
        }
    }

    @Override
    public void onBackPressed() {
        closeApp();
    }



}