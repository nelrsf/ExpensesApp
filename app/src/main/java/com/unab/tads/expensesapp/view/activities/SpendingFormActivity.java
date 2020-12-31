package com.unab.tads.expensesapp.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.unab.tads.expensesapp.R;
import com.unab.tads.expensesapp.databinding.ActivitySpendingFormBinding;
import com.unab.tads.expensesapp.model.entities.Spending;
import com.unab.tads.expensesapp.repositories.ProjectRepository;
import com.unab.tads.expensesapp.view.dialogs.DatePickerDialogBox;
import com.unab.tads.expensesapp.view.dialogs.ProgressDialogBox;
import com.unab.tads.expensesapp.viewmodel.SpendingFormActivityViewModel;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SpendingFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int REQUEST_CODE_CAMERA = 10;
    private static final int REQUEST_CODE_GALLERY = 20;
    ActivitySpendingFormBinding binding;
    private SpendingFormActivityViewModel viewModel;
    private int CRUDMode;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_form);
        //-----------------------------------------------------------------
        //------------------ Instantiate binding---------------------------
        //-----------------------------------------------------------------
        binding = DataBindingUtil.setContentView(SpendingFormActivity.this,R.layout.activity_spending_form);
        //-----------------------------------------------------------------
        //------------------ Instantiate view model -----------------------
        //-----------------------------------------------------------------
        viewModel = new ViewModelProvider(SpendingFormActivity.this).get(SpendingFormActivityViewModel.class);

        //--------------------------------------------------------------------------
        //------------------ set context menu on image view  -----------------------
        //--------------------------------------------------------------------------
        binding.ivInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(SpendingFormActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.picture_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent myIntent;
                        switch (item.getItemId()){
                            case R.id.mi_picture_by_camera:
                                myIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if(myIntent.resolveActivity(getPackageManager())!=null){
                                    File image = null;
                                    try {
                                        image = createPictureFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if(image!=null){
                                        imageUri = FileProvider.getUriForFile(SpendingFormActivity.this, "com.unab.tads.expensesapp.fileprovider", image);
                                        myIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                        startActivityForResult(myIntent, REQUEST_CODE_CAMERA);
                                    }
                                }
                                break;
                            case R.id.mi_picture_by_gallery:
                                myIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                if(myIntent.resolveActivity(getPackageManager())!=null){
                                    startActivityForResult(myIntent, REQUEST_CODE_GALLERY);
                                }
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        //-----------------------------------------------------------------
        //------------------ set submit button text -----------------------
        //-----------------------------------------------------------------
        Bundle bundle = getIntent().getExtras();
        this.CRUDMode = bundle.getInt("requesterCRUDFunction");
        switch (this.CRUDMode){
            case ProjectListActivity.CRUD_FUNCTION_CREATE:
                binding.setSubmitbutton(getString(R.string.bt_new_spending_text));
                break;
            case ProjectListActivity.CRUD_FUNCTION_UPDATE:
                binding.setSubmitbutton(getString(R.string.bt_edit_spending_text));
                break;
        }
        binding.setCrudmode(this.CRUDMode);


        //-----------------------------------------------------------------
        //---------------------- set form fields --------------------------
        //-----------------------------------------------------------------
        Spending s = (Spending) getIntent().getExtras().getSerializable("spending");
        binding.setSpending(s);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String d = dateFormat.format(s.getCreatedAt());
        binding.etDate.setText(d);
        Double a = s.getAmount();
        binding.etAmount.setText(a.toString());

        //-----------------------------------------------------------------
        //------------------ Setting Spinner values-----------------------
        //-----------------------------------------------------------------
        Spinner categorySpinner = binding.spCategory;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(s.getCategory());

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
                    Toast.makeText(SpendingFormActivity.this, R.string.text_error_invalid_date, Toast.LENGTH_SHORT).show();
                    return;
                }
                //Display progress dialog
                ProgressDialogBox.showProgressDialogBox(SpendingFormActivity.this);
                //Create project object
                Spending s = binding.getSpending();
                s.setCreatedAt(date);
                String t = binding.etAmount.getText().toString();
                if(!t.isEmpty()){
                    s.setAmount(Double.parseDouble(t));
                }
                s.setCategory(binding.spCategory.getSelectedItemPosition());
                s.setDescription(binding.etDescription.getText().toString());
                if(imageUri!=null){
                    s.setImageUri(imageUri);
                }
                switch (binding.getCrudmode()){
                    case ExpensesListActivity.CRUD_FUNCTION_CREATE:
                        //Create project in viewModel and repository
                        viewModel.createSpending(s, getIntent().getStringExtra("projectId"));
                        break;
                    case ExpensesListActivity.CRUD_FUNCTION_UPDATE:
                        //Update project in viewModel and repository
                        viewModel.updateSpending(s);
                        break;
                }
            }
        });

        //-------------------------------------------------------------------------------------
        //------------------ Observe result of repository transaction -------------------------
        //-------------------------------------------------------------------------------------
        viewModel.getResultMessage().observe(SpendingFormActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals(ProjectRepository.RESULT_MESSAGE_OK)){
                    finish();
                }
                else {
                    Toast.makeText(SpendingFormActivity.this, s, Toast.LENGTH_SHORT).show();
                }
                //Close progress bar
                if(ProgressDialogBox.progressDialog!=null){
                    ProgressDialogBox.hideProgressDialogBox(ProgressDialogBox.progressDialog);
                }
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
         Calendar c = Calendar.getInstance();
         c.set(Calendar.YEAR, year);
         c.set(Calendar.MONTH, month);
         c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

         String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
         binding.etDate.setText(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                Glide.with(SpendingFormActivity.this).load(imageUri).into(binding.ivInvoice);
                break;
            case REQUEST_CODE_GALLERY:
                imageUri = data.getData();
                Glide.with(SpendingFormActivity.this).load(imageUri).into(binding.ivInvoice);
                break;
        }
    }

    private File createPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        return image;
    }
}