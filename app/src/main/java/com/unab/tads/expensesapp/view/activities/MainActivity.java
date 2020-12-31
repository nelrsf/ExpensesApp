package com.unab.tads.expensesapp.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.unab.tads.expensesapp.R;
import com.unab.tads.expensesapp.databinding.ActivityMainBinding;
import com.unab.tads.expensesapp.repositories.UserRepository;
import com.unab.tads.expensesapp.view.dialogs.ProgressDialogBox;
import com.unab.tads.expensesapp.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_REGISTER_USER = 200;

    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-----------------------------------------------------------------
        //------------------ Instantiate binding---------------------------
        //-----------------------------------------------------------------
        binding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
        //-----------------------------------------------------------------
        //------------------ Instantiate view model -----------------------
        //-----------------------------------------------------------------
        viewModel = new ViewModelProvider(MainActivity.this).get(MainActivityViewModel.class);

        //-----------------------------------------------------------------
        //------------------ Set buttons Listeners-------------------------
        //-----------------------------------------------------------------
        binding.btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInAction();
            }
        });

        //-----------------------------------------------------------------
        //----------------- Set observers for log In ----------------------
        //-----------------------------------------------------------------
        viewModel.getResultMessage().observe(MainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals(UserRepository.RESULT_MESSAGE_OK)){
                    goToProjectsActivity();
                }else {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }
                //Close progress bar
                if(ProgressDialogBox.progressDialog!=null){
                    ProgressDialogBox.hideProgressDialogBox(ProgressDialogBox.progressDialog);
                }
            }
        });
    }

    //-----------------------------------------------------------------
    //------------------ Buttons listeners methods---------------------
    //-----------------------------------------------------------------
    private void logInAction() {
        String email = binding.etEmail.getText().toString();
        if(email.isEmpty() || binding.etPassword.getText().toString().isEmpty())
        {
            Toast.makeText(MainActivity.this, R.string.text_error_empty_field,Toast.LENGTH_SHORT).show();
        }else {
            viewModel.logIn(email, binding.etPassword.getText().toString());
            //Display progress dialog
            ProgressDialogBox.showProgressDialogBox(MainActivity.this);
        }
    }

    private void goToRegisterActivity(){
        Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivityForResult(myIntent, REQUEST_CODE_REGISTER_USER);
    }

    //-------------------------------------------------------------------
    //------------------ On activity results method ---------------------
    //-------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_REGISTER_USER:
                    Toast.makeText(MainActivity.this, R.string.text_successful_registration, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    //---------------------------------------------------------------------------------------
    //------------------ Go to projects activity if user is logged in -----------------------
    //---------------------------------------------------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();
        if(viewModel.CheckLoggedIn()){
            goToProjectsActivity();
        }
    }

    private void goToProjectsActivity(){
        Intent myIntent = new Intent(MainActivity.this, ProjectListActivity.class);
        startActivity(myIntent);
    }
}