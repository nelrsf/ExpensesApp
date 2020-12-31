package com.unab.tads.expensesapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.unab.tads.expensesapp.model.entities.Project;
import com.unab.tads.expensesapp.model.entities.Spending;
import com.unab.tads.expensesapp.repositories.ProjectRepository;
import com.unab.tads.expensesapp.repositories.SpendingRepository;

import java.util.ArrayList;

public class ExpensesListActivityViewModel extends AndroidViewModel {

    private SpendingRepository spendingRepository;
    private ProjectRepository projectRepository;

    public ExpensesListActivityViewModel(@NonNull Application application) {
        super(application);
        //---------------------------------------------------------------------------------------------------------------
        //-------------------------- Instantiate objects to prevent null pointer exception ------------------------------
        //---------------------------------------------------------------------------------------------------------------
        this.spendingRepository = new SpendingRepository(application);
        this.projectRepository = new ProjectRepository(application);
    }

    //--------------------------------------------------------------------------------------
    //---------------- Get result transaction message from repository ----------------------
    //--------------------------------------------------------------------------------------
    public LiveData<String> getResultMessage(){
        return spendingRepository.getResultTransactionMsg();
    }

    //--------------------------------------------------------------------------------------
    //---------------------- Get expenses list from repository -----------------------------
    //--------------------------------------------------------------------------------------
    public LiveData<ArrayList<Spending>> getExpenseList(){
        return spendingRepository.getExpensesListMutableLiveData();
    }

    //--------------------------------------------------------------------------------------
    //------------------- Get expenses parent project from repository ----------------------
    //--------------------------------------------------------------------------------------
    public LiveData<Project> getProjectLiveData(){
        return projectRepository.getProjectMutableLiveData();
    }

    //--------------------------------------------------------------------------------------
    //------------------- Get expenses parent project from repository ----------------------
    //--------------------------------------------------------------------------------------
    public void getProjectById(String id){
        projectRepository.getProjectById(id);
    }

    //--------------------------------------------------------------------------------------
    //----------------------- Load all expenses from repository  ---------------------------
    //--------------------------------------------------------------------------------------
    public void loadAllExpenses(String projectId){
        spendingRepository.loadAllExpenses(projectId);
    }

    //--------------------------------------------------------------------------------------
    //------------------------ remove spending from repository -----------------------------
    //--------------------------------------------------------------------------------------
    public void removeSpending(Spending spending){
        spendingRepository.removeSpending(spending);
    }
}
