package com.unab.tads.expensesapp.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unab.tads.expensesapp.model.entities.User;



public class UserRepository {

    private static final String COLLECTION_USERS = "users";

    public static final String RESULT_MESSAGE_OK = "ok";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<String> resultTransactionMsg;


    public UserRepository(Context context){
        //------------------------------------------------------------------------------
        //------------------ Instantiate Firebase Auth object---------------------------
        //------------------------------------------------------------------------------
        this.mAuth = FirebaseAuth.getInstance();
        //------------------------------------------------------------------------------
        //------------------ Instantiate Firebase Store object---------------------------
        //------------------------------------------------------------------------------
        db = FirebaseFirestore.getInstance();
        //------------------------------------------------------------------------------
        //------------------ Instantiate Mutable live data user ------------------------
        //------------------------------------------------------------------------------
        userMutableLiveData = new MutableLiveData<>();
        //------------------------------------------------------------------------------
        //--------------- Instantiate Mutable live data message ------------------------
        //------------------------------------------------------------------------------
        resultTransactionMsg = new MutableLiveData<>();
    }

    //-------------------------------------------------------------------------------------
    //------------------ Get firebase authentication object -------------------------------
    //-------------------------------------------------------------------------------------
    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    //------------------------------------------------------------------------------
    //------------------ Get user live data method ---------------------------------
    //------------------------------------------------------------------------------
    public LiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    //------------------------------------------------------------------------------
    //-------------- Get result message live data method ---------------------------
    //------------------------------------------------------------------------------
    public LiveData<String> getResultTransactionMsg() {
        return resultTransactionMsg;
    }

    //------------------------------------------------------------------------------
    //------------------ Sign In user method----------------------------------------
    //------------------------------------------------------------------------------
    public void signInUser(User user, String password){
        this.mAuth.createUserWithEmailAndPassword(user.getEmailAddress(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            //Execute when authentication has ended
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    db.collection(COLLECTION_USERS).add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        //Execute when user has been created on db
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
                                userMutableLiveData.setValue(user);
                            }
                            else {
                                resultTransactionMsg.setValue(task.getException().getMessage());
                            }
                        }
                    });
                }
                else {
                    resultTransactionMsg.setValue(task.getException().getMessage());
                }
            }
        });
    }


    //------------------------------------------------------------------------------
    //------------------ log In user method-----------------------------------------
    //------------------------------------------------------------------------------
    public void logInUser(String email, String password){
        this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    db.collection(COLLECTION_USERS).document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
                                userMutableLiveData.setValue(task.getResult().toObject(User.class));
                            }else {
                                resultTransactionMsg.setValue(task.getException().getMessage());
                            }
                        }
                    });
                }
                else{
                    resultTransactionMsg.setValue(task.getException().getMessage());
                }
            }
        });
    }
    //------------------------------------------------------------------------------
    //------------------ log out user method ---------------------------------------
    //------------------------------------------------------------------------------
    public void logOutUser(){
        this.mAuth.signOut();
    }
}
