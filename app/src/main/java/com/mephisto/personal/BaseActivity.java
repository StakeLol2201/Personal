package com.mephisto.personal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.ProgressBar;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void closeApp() {
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setTitle(getString(R.string.titleCloseDialog));
        exitDialog.setMessage(getString(R.string.messageCloseDialog))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.positiveButtonCloseDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton(getString(R.string.negativeButtonCloseDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = exitDialog.create();
        alert.show();
    }

}
