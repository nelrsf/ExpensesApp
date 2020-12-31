package com.unab.tads.expensesapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.unab.tads.expensesapp.model.entities.User;
import com.unab.tads.expensesapp.repositories.UserRepository;


public class RegisterActivityViewModel extends AndroidViewModel {

    private User user;
    private UserRepository userRepository;


    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
        //---------------------------------------------------------------------------------------------------------------
        //---------------------------Instantiate objects to prevent null pointer exception-------------------------------
        //---------------------------------------------------------------------------------------------------------------
        this.userRepository = new UserRepository(application);
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Set user object --------------------------------------
    //--------------------------------------------------------------------------------------
    public void setUser(User user){
        this.user = user;
    }


    //--------------------------------------------------------------------------------------
    //---------------- Get result transaction message from repository ----------------------
    //--------------------------------------------------------------------------------------
    public LiveData<String> getResultMessage(){
        return userRepository.getResultTransactionMsg();
    }

    //--------------------------------------------------------------------------------------
    //-------------------------- Get user from repository ----------------------------------
    //--------------------------------------------------------------------------------------
    public LiveData<User> getUser(){
        return userRepository.getUserMutableLiveData();
    }

    //--------------------------------------------------------------------------------------
    //------------------ Sign In user method from repository--------------------------------
    //--------------------------------------------------------------------------------------
    public void signIn(String password){
         userRepository.signInUser(user, password);
    }

}
