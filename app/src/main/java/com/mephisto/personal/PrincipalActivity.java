package com.mephisto.personal;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mephisto.personal.Classes.BaseActivity;

public class PrincipalActivity extends BaseActivity {

    private Dialog infoDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.disconnect) {
            signOut();
        } else if (id == R.id.help) {
            showInfo();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInfo() {
        infoDialog = new Dialog(this);
        infoDialog.setContentView(R.layout.dialog_info);

        Button btnClose = infoDialog.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(view -> infoDialog.dismiss());

        infoDialog.show();
    }

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