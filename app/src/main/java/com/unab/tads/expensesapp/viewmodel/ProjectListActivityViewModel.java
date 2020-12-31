package com.unab.tads.expensesapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.unab.tads.expensesapp.model.entities.Project;
import com.unab.tads.expensesapp.repositories.ProjectRepository;
import com.unab.tads.expensesapp.repositories.UserRepository;

import java.util.ArrayList;

public class ProjectListActivityViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private ProjectRepository projectRepository;

    public ProjectListActivityViewModel(@NonNull Application application) {
        super(application);
        //---------------------------------------------------------------------------------------------------------------
        //---------------------------Instantiate objects to prevent null pointer exception-------------------------------
        //---------------------------------------------------------------------------------------------------------------
        this.userRepository = new UserRepository(application);
        this.projectRepository = new ProjectRepository(application);
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
    //----------------------- Load all projects from repository  ---------------------------
    //--------------------------------------------------------------------------------------
    public void loadAllProjects(){
        projectRepository.loadAllProjects();
    }

    //--------------------------------------------------------------------------------------
    //-------------------------- Delete project from repository  ---------------------------
    //--------------------------------------------------------------------------------------
    public void removeProject(Project project){projectRepository.removeProject(project);}

    //--------------------------------------------------------------------------------------
    //----------------------- Log Out user method from repository ---------------------------
    //--------------------------------------------------------------------------------------
    public void logOut(){
        userRepository.logOutUser();
    }
}
