package com.unab.tads.expensesapp.view.dialogs;

import android.app.Activity;
import android.app.ProgressDialog;

import com.unab.tads.expensesapp.R;

public abstract class ProgressDialogBox {

    //Define progress dialog object
    public static ProgressDialog progressDialog;

    //---------------------------------------------------------------------
    //------------------- Display progress dialog -------------------------
    //---------------------------------------------------------------------
    public static void showProgressDialogBox(Activity activity){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    //---------------------------------------------------------------------
    //------------------- Hide progress dialog -------------------------
    //---------------------------------------------------------------------
    public static void hideProgressDialogBox(ProgressDialog progressDialog){
        progressDialog.dismiss();
    }
}
