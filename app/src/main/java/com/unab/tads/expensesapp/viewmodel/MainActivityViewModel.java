package com.unab.tads.expensesapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.unab.tads.expensesapp.model.entities.User;
import com.unab.tads.expensesapp.repositories.UserRepository;

public class MainActivityViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        //---------------------------------------------------------------------------------------------------------------
        //---------------------------Instantiate objects to prevent null pointer exception-------------------------------
        //---------------------------------------------------------------------------------------------------------------
        this.userRepository = new UserRepository(application);
    }

    //--------------------------------------------------------------------------------------
    //---------------------------- Check if user is logged in ------------------------------
    //--------------------------------------------------------------------------------------
    public boolean CheckLoggedIn(){
        if(userRepository.getmAuth().getCurrentUser()!=null){
            return true;
        }{
            return false;
        }
    }

    //--------------------------------------------------------------------------------------
    //---------------- Get result transaction message from repository ----------------------
    //--------------------------------------------------------------------------------------
    public LiveData<String> getResultMessage(){
        return userRepository.getResultTransactionMsg();
    }

    //--------------------------------------------------------------------------------------
    //---------------- Get result transaction message from repository ----------------------
    //--------------------------------------------------------------------------------------
    public LiveData<User> getUser(){
        return userRepository.getUserMutableLiveData();
    }

    //--------------------------------------------------------------------------------------
    //----------------------- Log In user method from repository ---------------------------
    //--------------------------------------------------------------------------------------
    public void logIn(String email, String password){
        userRepository.logInUser(email, password);
    }
}
