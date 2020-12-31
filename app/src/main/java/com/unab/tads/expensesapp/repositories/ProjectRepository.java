package com.unab.tads.expensesapp.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.unab.tads.expensesapp.R;
import com.unab.tads.expensesapp.model.entities.Project;
import com.unab.tads.expensesapp.model.entities.User;

import java.util.ArrayList;
import java.util.Objects;

public class ProjectRepository {

    public static final String COLLECTION_PROJECTS = "projects";
    public static final String RESULT_MESSAGE_OK = "ok";
    private FirebaseFirestore db;
    private MutableLiveData<String> resultTransactionMsg;
    private MutableLiveData<ArrayList<Project>> projectsListMutableData;
    private MutableLiveData<Project> projectMutableLiveData;
    private UserRepository userRepository;

    public ProjectRepository(Context context) {
        //------------------------------------------------------------------------------
        //---------------------- Instantiate user repository ---------------------------
        //------------------------------------------------------------------------------
        userRepository = new UserRepository(context);
        //------------------------------------------------------------------------------
        //------------------ Instantiate Firebase Store object--------------------------
        //------------------------------------------------------------------------------
        db = FirebaseFirestore.getInstance();
        //------------------------------------------------------------------------------
        //--------------- Instantiate Mutable live data message ------------------------
        //------------------------------------------------------------------------------
        resultTransactionMsg = new MutableLiveData<>();
        //------------------------------------------------------------------------------
        //--------------- Instantiate Mutable list of projects -------------------------
        //------------------------------------------------------------------------------
        projectsListMutableData = new MutableLiveData<>();
        //------------------------------------------------------------------------------
        //--------------------- Instantiate Mutable project ----------------------------
        //------------------------------------------------------------------------------
        projectMutableLiveData = new MutableLiveData<>();
    }

    //------------------------------------------------------------------------------
    //------------------ Get project live data method ------------------------------
    //------------------------------------------------------------------------------
    public LiveData<ArrayList<Project>> getProjectListMutableLiveData() {
        return projectsListMutableData;
    }

    //------------------------------------------------------------------------------
    //------------------ Get project by id live data method ------------------------
    //------------------------------------------------------------------------------
    public LiveData<Project> getProjectMutableLiveData() {
        return projectMutableLiveData;
    }

    //------------------------------------------------------------------------------
    //-------------- Get result message live data method ---------------------------
    //------------------------------------------------------------------------------
    public LiveData<String> getResultTransactionMsg() {
        return resultTransactionMsg;
    }

    //------------------------------------------------------------------------------
    //------------------ Load all projects method ----------------------------------
    //------------------------------------------------------------------------------
    public void loadAllProjects(){
        String uid = userRepository.getmAuth().getUid();
        db.collection(COLLECTION_PROJECTS).whereEqualTo("userId", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Project> pl = new ArrayList<>();
                    for(DocumentSnapshot item: task.getResult().getDocuments() ) {
                        Project p = item.toObject(Project.class);
                        p.setProjectId(item.getId());
                        pl.add(p);
                    }
                    projectsListMutableData.setValue(pl);
                    resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
                }
                else {
                    resultTransactionMsg.setValue(task.getException().getMessage());
                }
            }
        });
    }

    //------------------------------------------------------------------------------
    //------------------ Get project by id method ----------------------------------
    //------------------------------------------------------------------------------
    public void getProjectById(String id){
        db.collection(COLLECTION_PROJECTS).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  if(task.isSuccessful()){
                      Project p = Objects.requireNonNull(task.getResult()).toObject(Project.class);
                      p.setProjectId(id);
                      projectMutableLiveData.setValue(p);
                      resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
                  }
                  else {
                      resultTransactionMsg.setValue(task.getException().getMessage());
                  }
            }
        });
    }

    //------------------------------------------------------------------------------
    //------------------ Create project method -------------------------------------
    //------------------------------------------------------------------------------
    public void createProject(Project project){
        project.setUserId(userRepository.getmAuth().getUid());
        db.collection(COLLECTION_PROJECTS).add(project).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
               if(task.isSuccessful()){
                   ArrayList<Project> pl = projectsListMutableData.getValue();
                   if (pl==null){pl = new ArrayList<>();}
                   pl.add(project);
                   projectsListMutableData.setValue(pl);
                   resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
               }
               else {
                   resultTransactionMsg.setValue(task.getException().getMessage());
               }
            }
        });
    }
    //------------------------------------------------------------------------------
    //---------------------- Remove project method ---------------------------------
    //------------------------------------------------------------------------------
    public void removeProject(Project project){
        String pId = project.getProjectId();
        db.collection(COLLECTION_PROJECTS).document(pId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
                }
                else {
                    resultTransactionMsg.setValue(task.getException().getMessage());
                }
            }
        });
        db.collection(SpendingRepository.EXPENSES_COLLECTION).whereEqualTo("projectId", pId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   for (DocumentSnapshot item: Objects.requireNonNull(task.getResult()).getDocuments()){
                        db.collection(SpendingRepository.EXPENSES_COLLECTION).document(item.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){

                               }
                            }
                        });
                   }
               }
            }
        });
    }
    //------------------------------------------------------------------------------
    //---------------------- Edit/update project method ----------------------------
    //------------------------------------------------------------------------------
    public void updateProject(Project project){
        db.collection(COLLECTION_PROJECTS).document(project.getProjectId()).set(project).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
                }else {
                    resultTransactionMsg.setValue(task.getException().getMessage());
                }
            }
        });
    }
}
