package com.unab.tads.expensesapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.unab.tads.expensesapp.model.entities.Spending;
import com.unab.tads.expensesapp.repositories.SpendingRepository;

public class SpendingFormActivityViewModel extends AndroidViewModel {

    private SpendingRepository spendingRepository;

    public SpendingFormActivityViewModel(@NonNull Application application) {
        super(application);
        //---------------------------------------------------------------------------------------------------------------
        //---------------------------Instantiate objects to prevent null pointer exception-------------------------------
        //---------------------------------------------------------------------------------------------------------------
        this.spendingRepository = new SpendingRepository(application);

    }

    //--------------------------------------------------------------------------------------
    //---------------- Get result transaction message from repository ----------------------
    //--------------------------------------------------------------------------------------
    public LiveData<String> getResultMessage(){
        return spendingRepository.getResultTransactionMsg();
    }

    public void createSpending(Spending spending, String projectId) {
        spendingRepository.createSpending(spending, projectId);
    }

    public void updateSpending(Spending spending) {
        spendingRepository.updateSpending(spending);
    }
}
