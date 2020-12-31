package com.unab.tads.expensesapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.unab.tads.expensesapp.model.entities.Project;
import com.unab.tads.expensesapp.repositories.ProjectRepository;

import java.util.ArrayList;

public class ProjectFormActivityViewModel extends AndroidViewModel {

    private ProjectRepository projectRepository;

    public ProjectFormActivityViewModel(@NonNull Application application) {
        super(application);
        //---------------------------------------------------------------------------------------------------------------
        //---------------------------Instantiate objects to prevent null pointer exception-------------------------------
        //---------------------------------------------------------------------------------------------------------------
        projectRepository = new ProjectRepository(application);
    }

    //--------------------------------------------------------------------------------------
    //---------------- Get result transaction message from repository ----------------------
    //--------------------------------------------------------------------------------------
    public LiveData<String> getResultMessage(){
        return projectRepository.getResultTransactionMsg();
    }

    //--------------------------------------------------------------------------------------
    //---------------------- Get project list from repository ------------------------------
    //--------------------------------------------------------------------------------------
    public LiveData<ArrayList<Project>> getProjectList(){
        return projectRepository.getProjectListMutableLiveData();
    }

    //--------------------------------------------------------------------------------------
    //----------------------- Add project method from repository ---------------------------
    //--------------------------------------------------------------------------------------
    public void createProject(Project project){
        projectRepository.createProject(project);
    }

    //--------------------------------------------------------------------------------------
    //----------------------- Update project method from repository ------------------------
    //--------------------------------------------------------------------------------------
    public void updateProject(Project project){
        projectRepository.updateProject(project);
    }
}
