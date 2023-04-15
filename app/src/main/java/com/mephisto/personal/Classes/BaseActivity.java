package com.mephisto.personal.Classes;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.mephisto.personal.R;

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting
    public ProgressDialog mProgressDialog, mProgressRequest;

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

    public void showReceivingRequest() {
        if (mProgressRequest == null) {
            mProgressRequest = new ProgressDialog(this);
            mProgressRequest.setMessage(getString(R.string.getResponse));
            mProgressRequest.setIndeterminate(true);
        }
    }

    public void hideReceivingRequest() {
        if (mProgressRequest != null && mProgressRequest.isShowing()) {
            mProgressRequest.dismiss();
        }
    }

    public void closeApp() {
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
        exitDialog.setTitle(getString(R.string.titleCloseDialog));
        exitDialog.setMessage(getString(R.string.messageCloseDialog))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.positiveButtonCloseDialog), (dialogInterface, i) -> {
                    finishAffinity();
                    moveTaskToBack(true);
                })
                .setNegativeButton(getString(R.string.negativeButtonCloseDialog), (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog alert = exitDialog.create();
        alert.show();
    }

    public void errorMessage() {
        AlertDialog.Builder errorDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.titleErrorDialog)
                .setMessage(R.string.messageErrorDialog)
                .setPositiveButton(R.string.closeButtonErrorDialog, ((dialogInterface, i) -> dialogInterface.cancel()));
        AlertDialog alert = errorDialog.create();
        alert.show();
    }

}
