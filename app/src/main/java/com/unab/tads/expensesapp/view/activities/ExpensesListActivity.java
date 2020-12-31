package com.unab.tads.expensesapp.view.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.unab.tads.expensesapp.R;
import com.unab.tads.expensesapp.databinding.ActivityExpensesListBinding;
import com.unab.tads.expensesapp.model.entities.Project;
import com.unab.tads.expensesapp.model.entities.Spending;
import com.unab.tads.expensesapp.view.adapters.SpendingAdapter;
import com.unab.tads.expensesapp.view.adapters.eventlisteners.EditItemSpendingListener;
import com.unab.tads.expensesapp.view.adapters.eventlisteners.RemoveItemSpendingListener;
import com.unab.tads.expensesapp.view.dialogs.ProgressDialogBox;
import com.unab.tads.expensesapp.viewmodel.ExpensesListActivityViewModel;


import java.util.ArrayList;


public class ExpensesListActivity extends AppCompatActivity {

    public static final int CRUD_FUNCTION_CREATE = 100;
    public static final int CRUD_FUNCTION_UPDATE = 200;

    private SpendingAdapter spendingAdapter;
    private ActivityExpensesListBinding binding;
    private ExpensesListActivityViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_list);
        //-----------------------------------------------------------------
        //------------------ Instantiate binding---------------------------
        //-----------------------------------------------------------------
        binding = DataBindingUtil.setContentView(ExpensesListActivity.this, R.layout.activity_expenses_list);
        //-----------------------------------------------------------------
        //------------------ Instantiate view model -----------------------
        //-----------------------------------------------------------------
        viewModel = new ViewModelProvider(ExpensesListActivity.this).get(ExpensesListActivityViewModel.class);


        //-----------------------------------------------------------------
        //----------------- populate adapter observer ---------------------
        //-----------------------------------------------------------------
        viewModel.getExpenseList().observe(ExpensesListActivity.this, new Observer<ArrayList<Spending>>() {
            @Override
            public void onChanged(ArrayList<Spending> spendings) {
                //-----------------------------------------------------------------
                //------------------ Instantiate adapter --------------------------
                //-----------------------------------------------------------------
                spendingAdapter = new SpendingAdapter(spendings);
                setAdapterListeners();
                binding.rvExpensesList.setAdapter(spendingAdapter);
                //Get main project to set card view
                viewModel.getProjectById(((Project)getIntent().getExtras().getSerializable("project")).getProjectId());
                //Close progress bar
                if(ProgressDialogBox.progressDialog!=null){
                    ProgressDialogBox.hideProgressDialogBox(ProgressDialogBox.progressDialog);
                }
            }
        });


        //-----------------------------------------------------------------
        //------------------ Set recyclerview adapter----------------------
        //-----------------------------------------------------------------
        binding.rvExpensesList.setLayoutManager(new LinearLayoutManager(ExpensesListActivity.this));
        binding.rvExpensesList.setHasFixedSize(true);

        //-----------------------------------------------------------------
        //------------------ Set project cardView--------------------------
        //-----------------------------------------------------------------
        viewModel.getProjectLiveData().observe(ExpensesListActivity.this, new Observer<Project>() {
            @Override
            public void onChanged(Project project) {
                binding.includeCardProject.setProject(project);
            }
        });
        binding.includeCardProject.ibEditProject.setVisibility(View.INVISIBLE);
        binding.includeCardProject.ibRemoveProject.setVisibility(View.INVISIBLE);

        //-----------------------------------------------------------------
        //------------------ Set buttons Listeners ------------------------
        //-----------------------------------------------------------------
        binding.ibNewSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSpendingFormActivity(CRUD_FUNCTION_CREATE, new Spending());
            }
        });
    }

    //-----------------------------------------------------------------
    //-------------- set all listeners to adapter ---------------------
    //-----------------------------------------------------------------
    private void setAdapterListeners() {
        spendingAdapter.setRemoveItemSpendingListener(new RemoveItemSpendingListener() {
            @Override
            public void OnClickRemoveButton(Spending spending) {
                viewModel.removeSpending(spending);
                finish();
                startActivity(getIntent());
            }
        });
        spendingAdapter.setEditItemSpendingListener(new EditItemSpendingListener() {
            @Override
            public void OnClickEditButton(Spending spending) {
                goToSpendingFormActivity(CRUD_FUNCTION_UPDATE, spending);
            }
        });
    }

    //-----------------------------------------------------------------
    //------------ Go to spending form activity method -----------------
    //-----------------------------------------------------------------
    private void goToSpendingFormActivity(int requestCRUDFunction, Spending spending){
        Intent myIntent = new Intent(ExpensesListActivity.this, SpendingFormActivity.class);
        myIntent.putExtra("requesterCRUDFunction", requestCRUDFunction);
        myIntent.putExtra("spending", spending);
        myIntent.putExtra("projectId", binding.includeCardProject.getProject().getProjectId());
        startActivity(myIntent);
    }

    //--------------------------------------------------------------------
    //------------------ call all expenses from db -----------------------
    //--------------------------------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();
        //Display progress dialog
        ProgressDialogBox.showProgressDialogBox(ExpensesListActivity.this);
        String projectId = ((Project)getIntent().getSerializableExtra("project")).getProjectId();
        viewModel.loadAllExpenses(projectId);
    }
}