package com.unab.tads.expensesapp.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.unab.tads.expensesapp.R;
import com.unab.tads.expensesapp.databinding.ActivityProjectListBinding;
import com.unab.tads.expensesapp.model.entities.Project;
import com.unab.tads.expensesapp.view.adapters.ProjectAdapter;
import com.unab.tads.expensesapp.view.adapters.eventlisteners.EditItemProjectListener;
import com.unab.tads.expensesapp.view.adapters.eventlisteners.RemoveItemProjectListener;
import com.unab.tads.expensesapp.view.dialogs.ProgressDialogBox;
import com.unab.tads.expensesapp.viewmodel.ProjectListActivityViewModel;

import java.util.ArrayList;


public class ProjectListActivity extends AppCompatActivity {

    public static final int CRUD_FUNCTION_CREATE = 100;
    public static final int CRUD_FUNCTION_UPDATE = 200;
    private ProjectAdapter projectAdapter;
    private ActivityProjectListBinding binding;
    private ProjectListActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        //-----------------------------------------------------------------
        //------------------ Instantiate binding---------------------------
        //-----------------------------------------------------------------
        binding = DataBindingUtil.setContentView(ProjectListActivity.this, R.layout.activity_project_list);

        //-----------------------------------------------------------------
        //------------------ Instantiate view model -----------------------
        //-----------------------------------------------------------------
        viewModel = new ViewModelProvider(ProjectListActivity.this).get(ProjectListActivityViewModel.class);

        //-----------------------------------------------------------------
        //------------------ Set recyclerview adapter----------------------
        //-----------------------------------------------------------------
        binding.rvProjectList.setLayoutManager(new LinearLayoutManager(ProjectListActivity.this));
        binding.rvProjectList.setHasFixedSize(true);

        //-----------------------------------------------------------------------
        //------------------ populate recyclerview from db ----------------------
        //-----------------------------------------------------------------------
        viewModel.getProjectList().observe(ProjectListActivity.this, new Observer<ArrayList<Project>>() {
            @Override
            public void onChanged(ArrayList<Project> projects) {
                //-----------------------------------------------------------------
                //------------------ Instantiate adapter---------------------------
                //-----------------------------------------------------------------
                projectAdapter = new ProjectAdapter(projects);
                projectAdapter.setOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Project project) {
                        Intent myIntent = new Intent(ProjectListActivity.this, ExpensesListActivity.class);
                        myIntent.putExtra("project", project);
                        startActivity(myIntent);
                    }
                });
                setAdapterListeners();
                binding.rvProjectList.setAdapter(projectAdapter);
                //Close progress bar
                if(ProgressDialogBox.progressDialog!=null){
                    ProgressDialogBox.hideProgressDialogBox(ProgressDialogBox.progressDialog);
                }
            }
        });



        //-----------------------------------------------------------------
        //------------------ Set buttons Listeners-------------------------
        //-----------------------------------------------------------------
        binding.ibNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProjectFormActivity(CRUD_FUNCTION_CREATE, new Project());
            }
        });


    }


    //-----------------------------------------------------------------
    //-------------- set all listeners to adapter ---------------------
    //-----------------------------------------------------------------
    private void setAdapterListeners() {
        projectAdapter.setRemoveItemProjectListener(new RemoveItemProjectListener() {
            @Override
            public void onClickRemoveButton(Project project) {
                viewModel.removeProject(project);
                finish();
                startActivity(getIntent());
            }
        });
        projectAdapter.setEditItemProjectListener(new EditItemProjectListener() {
            @Override
            public void onClickEditButton(Project project, int position) {
                goToProjectFormActivity(CRUD_FUNCTION_UPDATE, project);
            }
        });
    }

    //-----------------------------------------------------------------
    //------------ Go to project form activity method -----------------
    //-----------------------------------------------------------------
    private void goToProjectFormActivity(int requestCRUDFunction, Project project){
        Intent myIntent = new Intent(ProjectListActivity.this, ProjectFormActivity.class);
        myIntent.putExtra("requesterCRUDFunction", requestCRUDFunction);
        myIntent.putExtra("project", project);
        startActivity(myIntent);
    }


    //--------------------------------------------------------------------
    //------------------ Create options menu -----------------------------
    //--------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myInflater = getMenuInflater();
        myInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //----------------------------------------------------------------------
    //------------------ Set menu items action -----------------------------
    //----------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mi_logout:
                viewModel.logOut();
                finish();
                break;
        }
        return true;
    }

    //--------------------------------------------------------------------
    //------------------ call all projects from db -----------------------
    //--------------------------------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();
        //Display progress dialog
        ProgressDialogBox.showProgressDialogBox(ProjectListActivity.this);
        viewModel.loadAllProjects();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //Prevent Progress dialog anchored if app is in the background
        if(ProgressDialogBox.progressDialog!=null){
            ProgressDialogBox.hideProgressDialogBox(ProgressDialogBox.progressDialog);
        }
    }
}