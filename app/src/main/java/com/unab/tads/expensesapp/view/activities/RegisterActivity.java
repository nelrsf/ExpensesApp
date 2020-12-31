package com.unab.tads.expensesapp.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.unab.tads.expensesapp.R;
import com.unab.tads.expensesapp.databinding.ActivityRegisterBinding;
import com.unab.tads.expensesapp.model.entities.User;
import com.unab.tads.expensesapp.repositories.UserRepository;
import com.unab.tads.expensesapp.view.dialogs.ProgressDialogBox;
import com.unab.tads.expensesapp.viewmodel.RegisterActivityViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //-----------------------------------------------------------------
        //------------------ Instantiate binding---------------------------
        //-----------------------------------------------------------------
        binding = DataBindingUtil.setContentView(RegisterActivity.this,R.layout.activity_register);
        //-----------------------------------------------------------------
        //------------------ Instantiate view model -----------------------
        //-----------------------------------------------------------------
        viewModel = new ViewModelProvider(RegisterActivity.this).get(RegisterActivityViewModel.class);


        //-----------------------------------------------------------------
        //------------------ Set buttons Listeners-------------------------
        //-----------------------------------------------------------------
        binding.btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //-----------------------------------------------------------------
        //----------------- Set observers for sign In ---------------------
        //-----------------------------------------------------------------
        viewModel.getResultMessage().observe(RegisterActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //Check if user has changed
                if (s.equals(UserRepository.RESULT_MESSAGE_OK)){
                    viewModel.getUser().observe(RegisterActivity.this, new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                                setResult(RESULT_OK);
                                finish();
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                }
                //Close progress bar
                if(ProgressDialogBox.progressDialog!=null){
                    ProgressDialogBox.hideProgressDialogBox(ProgressDialogBox.progressDialog);
                }
            }
        });
    }

    //---------------------------------------------------------------------
    //------------------ Sign In action on button -------------------------
    //---------------------------------------------------------------------
    private void signIn(){
        //Set temporary properties
        String name = binding.etName.getText().toString();
        String company = binding.etCompany.getText().toString();
        String email = binding.etEmail.getText().toString();

        if (binding.etPassword.getText().toString().equals(binding.etConfPassword.getText().toString())){
            if(name.isEmpty() || email.isEmpty() || binding.etPassword.getText().toString().isEmpty()){
                //-------------------------------------------------------------------------------------
                //------------ Show error if name, password or email is empty -------------------------
                //-------------------------------------------------------------------------------------
                Toast.makeText(RegisterActivity.this, R.string.text_error_empty_field, Toast.LENGTH_LONG).show();
            }
            else {
                //Display progress dialog
                ProgressDialogBox.showProgressDialogBox(RegisterActivity.this);
                //Create user from register form
                User user;
                user = new User(name, company, email);
                //Set user in view model
                viewModel.setUser(user);
                //Sign in on firebase auth service
                viewModel.signIn(binding.etPassword.getText().toString());
            }
        }
        else {
            //----------------------------------------------------------------------------------
            //------------------- Show error if password doesn't match -------------------------
            //----------------------------------------------------------------------------------
            Toast.makeText(RegisterActivity.this, R.string.text_error_match_password, Toast.LENGTH_LONG).show();
        }
    }

    //---------------------------------------------------------------------
    //--------------------- set back button event -------------------------
    //---------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ProgressDialogBox.progressDialog!=null){
            ProgressDialogBox.hideProgressDialogBox(ProgressDialogBox.progressDialog);
        }
        Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(myIntent);
    }
}