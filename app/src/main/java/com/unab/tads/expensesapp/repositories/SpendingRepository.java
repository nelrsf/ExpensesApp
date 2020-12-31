package com.unab.tads.expensesapp.repositories;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.unab.tads.expensesapp.model.entities.Spending;

import java.util.ArrayList;


public class SpendingRepository {

    public static final String EXPENSES_COLLECTION = "expenses";
    private static final String RESULT_MESSAGE_OK = "ok";
    private static final String IMAGE_DIRECTORY = "images";
    private StorageReference mStorageRef;

    private FirebaseFirestore db;
    private MutableLiveData<String> resultTransactionMsg;
    private MutableLiveData<ArrayList<Spending>> expensesListMutableData;
    private ProjectRepository projectRepository;

    public SpendingRepository(Application application) {
        //------------------------------------------------------------------------------
        //------------------ Instantiate Firebase Store object--------------------------
        //------------------------------------------------------------------------------
        db = FirebaseFirestore.getInstance();
        //------------------------------------------------------------------------------
        //------------------ Instantiate Cloud storage object---------------------------
        //------------------------------------------------------------------------------
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //------------------------------------------------------------------------------
        //--------------- Instantiate Mutable live data message ------------------------
        //------------------------------------------------------------------------------
        resultTransactionMsg = new MutableLiveData<>();
        //------------------------------------------------------------------------------
        //------------------ Instantiate Mutable list of expenses ----------------------
        //------------------------------------------------------------------------------
        expensesListMutableData = new MutableLiveData<>();
        //------------------------------------------------------------------------------
        //--------------------- Instantiate Project repository -------------------------
        //------------------------------------------------------------------------------
        projectRepository = new ProjectRepository(application);
    }

    //------------------------------------------------------------------------------
    //------------------ Get project live data method ------------------------------
    //------------------------------------------------------------------------------
    public LiveData<ArrayList<Spending>> getExpensesListMutableLiveData() {
        return expensesListMutableData;
    }


    //------------------------------------------------------------------------------
    //-------------- Get result message live data method ---------------------------
    //------------------------------------------------------------------------------
    public LiveData<String> getResultTransactionMsg() {
        return resultTransactionMsg;
    }


    //------------------------------------------------------------------------------
    //------------------ Load all expenses method ----------------------------------
    //------------------------------------------------------------------------------
    public void loadAllExpenses(String projectId){
        db.collection(EXPENSES_COLLECTION).whereEqualTo("projectId", projectId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Double totalAmount = 0d;
                    ArrayList<Spending> sl = new ArrayList<>();
                    for(DocumentSnapshot item: task.getResult().getDocuments() ) {
                        Spending s = item.toObject(Spending.class);
                        s.setSpendingId(item.getId());
                        totalAmount+=s.getAmount();
                        sl.add(s);
                    }
                    db.collection(ProjectRepository.COLLECTION_PROJECTS).document(projectId).update("totalAmount", totalAmount);
                    expensesListMutableData.setValue(sl);
                    resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
                }
                else {
                    resultTransactionMsg.setValue(task.getException().getMessage());
                }
            }
        });
    }


    //------------------------------------------------------------------------------
    //------------------ Create spending method ------------------------------------
    //------------------------------------------------------------------------------
    public void createSpending(Spending spending, String projectId) {
        spending.setProjectId(projectId);
        db.collection(EXPENSES_COLLECTION).add(spending).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
               if(task.isSuccessful()){
                   ArrayList<Spending> el = expensesListMutableData.getValue();
                   if (el==null){el = new ArrayList<>();}
                   el.add(spending);
                   expensesListMutableData.setValue(el);
                   loadImage(spending, task.getResult().getId());
                   //resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
               }
            else {
                resultTransactionMsg.setValue(task.getException().getMessage());
              }
            }
        });
    }

    //------------------------------------------------------------------------------
    //----------------------- load image method ------------------------------------
    //------------------------------------------------------------------------------
    private void loadImage(Spending spending, String spId){
        Uri imageUri = spending.getImageUri();
        if(imageUri!=null){
            String imageStr = imageUri.toString().substring(imageUri.toString().lastIndexOf("/"));
            StorageReference myImage = mStorageRef.child(IMAGE_DIRECTORY+"/"+imageStr);
            myImage.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    String url;
                                    url = task.getResult().toString();
                                    db.collection(EXPENSES_COLLECTION).document(spId).update("imageUrl",url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                              if (task.isSuccessful()){
                                                  resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
                                              }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
        else {
            resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
        }
    }

    //------------------------------------------------------------------------------
    //---------------------- Remove project method ---------------------------------
    //------------------------------------------------------------------------------
    public void removeSpending(Spending spending){
        db.collection(EXPENSES_COLLECTION).document(spending.getSpendingId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }

    //------------------------------------------------------------------------------
    //---------------------- Edit/update spending method ---------------------------
    //------------------------------------------------------------------------------
    public void updateSpending(Spending spending){
        db.collection(EXPENSES_COLLECTION).document(spending.getSpendingId()).set(spending).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    loadImage(spending, spending.getSpendingId());
                    //resultTransactionMsg.setValue(RESULT_MESSAGE_OK);
                }else {
                    resultTransactionMsg.setValue(task.getException().getMessage());
                }
            }
        });
    }
}
