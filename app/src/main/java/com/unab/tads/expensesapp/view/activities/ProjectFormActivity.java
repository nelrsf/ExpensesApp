package com.unab.tads.expensesapp.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.unab.tads.expensesapp.R;
import com.unab.tads.expensesapp.databinding.ActivityProjectFormBinding;
import com.unab.tads.expensesapp.model.entities.Project;
import com.unab.tads.expensesapp.repositories.ProjectRepository;
import com.unab.tads.expensesapp.view.dialogs.DatePickerDialogBox;
import com.unab.tads.expensesapp.view.dialogs.ProgressDialogBox;
import com.unab.tads.expensesapp.viewmodel.ProjectFormActivityViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProjectFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private ActivityProjectFormBinding binding;
    private ProjectFormActivityViewModel viewModel;
    private int CRUDMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_form);
        //-----------------------------------------------------------------
        //------------------ Instantiate binding --------------------------
        //-----------------------------------------------------------------
        binding = DataBindingUtil.setContentView(ProjectFormActivity.this,R.layout.activity_project_form);
        //-----------------------------------------------------------------
        //------------------ Instantiate view model -----------------------
        //-----------------------------------------------------------------
        viewModel = new ViewModelProvider(ProjectFormActivity.this).get(ProjectFormActivityViewModel.class);



        //-----------------------------------------------------------------
        //------------------ set submit button text -----------------------
        //-----------------------------------------------------------------
        Bundle bundle = getIntent().getExtras();
        this.CRUDMode = bundle.getInt("requesterCRUDFunction");
        switch (this.CRUDMode){
            case ProjectListActivity.CRUD_FUNCTION_CREATE:
                    binding.setSubmitbutton(getString(R.string.bt_new_project_text));
                    binding.setCrudmode(ProjectListActivity.CRUD_FUNCTION_CREATE);
                    break;
            case ProjectListActivity.CRUD_FUNCTION_UPDATE:
                binding.setSubmitbutton(getString(R.string.bt_edit_project_text));
                binding.setCrudmode(ProjectListActivity.CRUD_FUNCTION_UPDATE);
                break;
        }
        //-----------------------------------------------------------------
        //---------------------- set form fields --------------------------
        //-----------------------------------------------------------------
        Project p = (Project) getIntent().getExtras().getSerializable("project");
        binding.setProject(p);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String d = dateFormat.format(p.getDate());
        binding.etDate.setText(d);

        //-----------------------------------------------------------------
        //------------------ Set edit text Listeners-----------------------
        //-----------------------------------------------------------------
        binding.etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DialogFragment datePicker = new DatePickerDialogBox();
                    datePicker.show(getSupportFragmentManager(),"Fecha");
                }
            }
        });
        //-----------------------------------------------------------------
        //------------------ Set buttons Listeners-------------------------
        //-----------------------------------------------------------------
        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Convert Date from string
                Date date = new Date();
                try {
                    date = DateFormat.getDateInstance(DateFormat.MEDIUM).parse(binding.etDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(ProjectFormActivity.this, R.string.text_error_invalid_date, Toast.LENGTH_SHORT).show();
                    return;
                }
                //Display progress dialog
                ProgressDialogBox.showProgressDialogBox(ProjectFormActivity.this);
                //Create project object
                Project p = binding.getProject();
                p.setDate(date);
                switch (binding.getCrudmode()){
                    case ProjectListActivity.CRUD_FUNCTION_CREATE:
                        //Create project in viewModel and repository
                        viewModel.createProject(p);
                        break;
                    case ProjectListActivity.CRUD_FUNCTION_UPDATE:
                        //Update project in viewModel and repository
                        viewModel.updateProject(p);
                        break;
                }
            }
        });
        //------------------------------------------------------------------------
        //----------------- Set observers for create project ---------------------
        //------------------------------------------------------------------------
        viewModel.getProjectList().observe(ProjectFormActivity.this, new Observer<ArrayList<Project>>() {
            @Override
            public void onChanged(ArrayList<Project> projects) {

            }
        });

        viewModel.getResultMessage().observe(ProjectFormActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals(ProjectRepository.RESULT_MESSAGE_OK)){
                    finish();
                }
                else {
                    Toast.makeText(ProjectFormActivity.this, s, Toast.LENGTH_SHORT).show();
                }
                //Close progress bar
                if(ProgressDialogBox.progressDialog!=null){
                    ProgressDialogBox.hideProgressDialogBox(ProgressDialogBox.progressDialog);
                }
            }
        });

    }

    //-----------------------------------------------------------------
    //---------------------- Set date picker --------------------------
    //-----------------------------------------------------------------
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        binding.etDate.setText(new DatePickerDialogBox().setDate(year, month, dayOfMonth));
    }
}