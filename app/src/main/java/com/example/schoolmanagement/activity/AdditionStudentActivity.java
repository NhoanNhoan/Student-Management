package com.example.schoolmanagement.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolmanagement.R;
import com.example.schoolmanagement.adapter.StudentAdapter;
import com.example.schoolmanagement.data_class.Student;
import com.example.schoolmanagement.database_handler.StudentHandler;
import com.example.schoolmanagement.database_handler.StudentPositionHandler;
import com.example.schoolmanagement.enums.Sex;
import com.example.schoolmanagement.utils.BitmapUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdditionStudentActivity extends AppCompatActivity {
    private boolean areShowingViews;
    private boolean isUpdatedMode;
    int RESULT_TAKE_PHOTO = 1;
    int RESULT_LOAD_IMAGE = 2;
    private Student student;
    Object selectedSpinnerValue;
    FloatingActionButton fabOptions;
    ImageButton ibSelectBirthDate;
    ImageButton ibSave;
    ImageButton ibRefresh;
    ImageView ivAvatar;

    ImageView ivProfile;
    EditText editName;
    TextView txtBirthDateValue;
    EditText editPhone;
    EditText editMail;
    RadioButton rbMale;
    RadioButton rbFemale;
    Spinner spinnerStudentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition_student);

        student = new Student();
        initViews();

        insertDataInStudentPositionTable();
        initSpinnerStudentPosition();

        try {
            setValueForViews();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setSpinnerSelected();
        ibSelectBirthDateClick();
        ivAvatarClick();
        setFabOptionsClick();
        setIbSaveClick();
    }

    private void initViews() {
        ivProfile = findViewById(R.id.imageViewAvatar);
        editName = findViewById(R.id.editName);
        txtBirthDateValue = findViewById(R.id.txtBirthDateValue);
        editPhone = findViewById(R.id.editPhoneNumber);
        editMail = findViewById(R.id.editMail);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        spinnerStudentPosition = findViewById(R.id.spinnerStudentPosition);
        fabOptions = findViewById(R.id.fabAdditionStudentOptions);
        ibSelectBirthDate = findViewById(R.id.ibSelectBirthDate);
        ivAvatar = findViewById(R.id.imageViewAvatar);
        ibSave = findViewById(R.id.ibSaveAdditionStudent);
        ibRefresh = findViewById(R.id.ibRefreshAdditionStudent);
    }

    private void insertDataInStudentPositionTable() {
        StudentPositionHandler handler = new StudentPositionHandler(this);
        ArrayList<String> listPositions = handler.getAllStudentPosition();
        if (0 == listPositions.size()) {
            handler = new StudentPositionHandler(this);
            handler.insertStudentPosition("monitor");
            handler.insertStudentPosition("vice monitor");
            handler.insertStudentPosition("group leader");
        }
    }

    private void setFabOptionsClick() {
        final Animation fabOptionRotateForward = AnimationUtils.loadAnimation(this, R.anim.fab_options_addition_student_rotate_forward);
        final Animation fabOptionRotateBackward = AnimationUtils.loadAnimation(this, R.anim.fab_options_addition_student_rotate_backward);
        if (null != fabOptions) {
            fabOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (areShowingViews) {
                        setVisibilityForViews(View.INVISIBLE);
                        v.startAnimation(fabOptionRotateBackward);
                    }
                    else {
                        setVisibilityForViews(View.VISIBLE);
                        v.startAnimation(fabOptionRotateForward);
                    }

                    areShowingViews = !areShowingViews;
                }
            });
        }
    }

    private void ibSelectBirthDateClick() {
        if (null != ibSelectBirthDate) {
            ibSelectBirthDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDateDialogPicker();
                }
            });
        }
    }

    private void ivAvatarClick() {
        if (null != ivAvatar) {
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });
        }
    }

    private void setIbSaveClick() {
        if (null == ibSave) {
            return;
        }

        final StudentPositionHandler studentPositionHandler = new StudentPositionHandler(this);
        final StudentHandler studentHandler = new StudentHandler(this);

        ibSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                String name = (editName.getText().toString());
                String phone = (editPhone.getText().toString());
                String mail = (editMail.getText().toString());
                Sex sex = rbMale.isChecked() ? Sex.MALE : Sex.FEMALE;
                Date birthDate = null;
                try {
                    String str = txtBirthDateValue.getText().toString();
                    birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(txtBirthDateValue.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String studentPosition = (String)selectedSpinnerValue;
                Bitmap profileBitmap = ((BitmapDrawable)ivAvatar.getDrawable()).getBitmap();

                String id = student.getId();
                student = new Student(id, name, phone, mail, sex, birthDate, studentPosition, profileBitmap);

                if (!isUpdatedMode) {
                    student.createNewId();
                    while (studentHandler.wasExistId(student.getId())) {
                        student.createNewId();
                    }

                    if (null == StudentsActivity.adapter) {
                        StudentsActivity.adapter = new StudentAdapter(new ArrayList<Student>());
                    }

                    StudentsActivity.adapter.addItem(student);
                    studentHandler.insertStudent(student);

                    Toast.makeText(v.getContext(), "Inserted!", Toast.LENGTH_SHORT).show();
                }
                else {
                    studentHandler.update(student);
                    Toast.makeText(v.getContext(), "Updated!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AdditionStudentActivity.this, StudentsActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        });
    }

    private void initSpinnerStudentPosition() {
        if (null == spinnerStudentPosition) {
            return;
        }

        StudentPositionHandler handler = new StudentPositionHandler(this);
        ArrayList<String> listPosition = handler.getAllStudentPosition();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listPosition);
        spinnerStudentPosition.setAdapter(adapter);
    }

    private Object setSpinnerSelected() {
        if (null == spinnerStudentPosition) {
            return null;
        }


        spinnerStudentPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpinnerValue = parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return selectedSpinnerValue;
    }

    private void openDateDialogPicker() {
        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DATE);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                txtBirthDateValue.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
            }
        }, y, m, d);

        dialog.show();
    }

    private void selectImage() {
        final CharSequence[] options = {"Take photo", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Take photo")) {
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePhotoIntent, RESULT_TAKE_PHOTO);
                }
                else if (options[which].equals("Choose from gallery")) {
                    Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(choosePictureIntent, RESULT_LOAD_IMAGE);
                }
                else if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    @SuppressLint("SimpleDateFormat")
    private void setValueForViews() throws ParseException {
        setStudentInfoFromIntent();
        if (!isUpdatedMode) return;

        editName.setText(student.getName());
        editPhone.setText(student.getPhone());
        editMail.setText(student.getEmail());
        if (null != student.getBirthDate())
            txtBirthDateValue.setText(new SimpleDateFormat("MM-dd-yyyy").format(student.getBirthDate()));

        if (Sex.MALE == student.getSex())
            rbMale.setChecked(true);
        else
            rbFemale.setChecked(true);

        ivProfile.setImageBitmap(student.getAvatar());

        int index = findIndexOfStudentPositionValueSpinner(student.getPosition());
        spinnerStudentPosition.setSelection(index);
    }

    @SuppressLint("SimpleDateFormat")
    private void setStudentInfoFromIntent() throws ParseException {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id_updated");
        if (null == id) {
            isUpdatedMode = false;
            return;
        }
        isUpdatedMode = true;
        String name = intent.getStringExtra("name_updated");
        String birthDateStr = intent.getStringExtra("birth_date_updated");
        String phone = intent.getStringExtra("phone_updated");
        String mail = intent.getStringExtra("mail_updated");
        String sexStr = intent.getStringExtra("sex_updated");
        String position = intent.getStringExtra("position_updated");
        Bitmap profileBitmap = BitmapUtility.getBitmap(intent.getByteArrayExtra("profile_updated"));

        Date birthDate = null;
        if (null != birthDateStr)
            birthDate = new SimpleDateFormat("MM-dd-yyyy").parse(birthDateStr);

        Sex sex = Sex.FEMALE;
        if ("male".equals(sexStr))
            sex = Sex.MALE;

        student = new Student(id, name, phone, mail, sex, birthDate, position, profileBitmap);
    }

    private int findIndexOfStudentPositionValueSpinner(String position) {
        StudentPositionHandler positionHandler = new StudentPositionHandler(this);
        ArrayList<String>  listPositions = positionHandler.getAllStudentPosition();
        int index = listPositions.indexOf(position);
        return index;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (RESULT_LOAD_IMAGE == requestCode && resultCode == RESULT_OK && null != data) {
            Uri selectedImageUri = data.getData();
            ivAvatar.setImageURI(selectedImageUri);
            return;
        }

        if (RESULT_TAKE_PHOTO == requestCode && requestCode == RESULT_FIRST_USER && null != data) {
            Bitmap selectedImage = (Bitmap)data.getExtras().get("data");
            ivAvatar.setImageBitmap(selectedImage);
        }
    }

    private void setVisibilityForViews(int visibility) {
        if (null != ibSave) {
            ibSave.setVisibility(visibility);
        }

        if (null != ibRefresh) {
            ibRefresh.setVisibility(visibility);
        }
    }
}
