package com.example.schoolmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolmanagement.R;
import com.example.schoolmanagement.data_class.Student;
import com.example.schoolmanagement.enums.Sex;
import com.example.schoolmanagement.utils.BitmapUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentInforActivity extends AppCompatActivity {
    private boolean areShowingButtons;
    private FloatingActionButton fabStudentInfo;
    private Button btnUpdate;
    private Button btnBack;
    private Button btnRemove;
    private Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_infor);

        try {
            setValueForViews();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fabStudentInfo = findViewById(R.id.fabStudentInfo);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnRemove = findViewById(R.id.btnRemove);
        btnBack = findViewById(R.id.btnBack);

        fabStudentInfoClick();
        btnUpdateClick();
    }

    private void setValueForViews() throws ParseException {
        Intent intent = getIntent();

        TextView txtName = findViewById(R.id.txtStudentInfoName);
        TextView txtBirthDate = findViewById(R.id.txtStudentInfoBirthDate);
        TextView txtPhone = findViewById(R.id.txtStudentInfoPhone);
        TextView txtEmail = findViewById(R.id.txtStudentInfoMail);
        TextView txtSex = findViewById(R.id.txtStudentInfoSex);
        TextView txtPosition = findViewById(R.id.txtStudentInfoPosition);
        ImageView profile = findViewById(R.id.ivStudentInfoProfile);

        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String birthDate = intent.getStringExtra("birth date");
        String phone = intent.getStringExtra("phone");
        String mail = intent.getStringExtra("mail");
        String sex = intent.getStringExtra("sex");
        String position = intent.getStringExtra("position");
        Bitmap profileBitmap = BitmapUtility.getBitmap(intent.getByteArrayExtra("profile"));

        txtName.setText(intent.getStringExtra("name"));
        txtBirthDate.setText(intent.getStringExtra("birth date"));
        txtPhone.setText(intent.getStringExtra("phone"));
        txtEmail.setText(intent.getStringExtra("mail"));
        txtSex.setText(intent.getStringExtra("sex"));
        txtPosition.setText(intent.getStringExtra("position"));
        profile.setImageBitmap(BitmapUtility.getBitmap(intent.getByteArrayExtra("profile")));

        @SuppressLint("SimpleDateFormat") Date birthDateValue = (birthDate == null) ? null : new SimpleDateFormat("MM-dd-yyy").parse(birthDate);

        Sex value = sex.equals("male") ? Sex.MALE : Sex.FEMALE;
        student = new Student(id,
                name,
                phone,
                mail,
                value,
                birthDateValue,
                position,
                profileBitmap);
    }

    private void fabStudentInfoClick() {
        final Animation fabOptionOpenAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_top_left_90);
        final Animation fabOptionCloseAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_left_top_90);
        fabStudentInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!areShowingButtons) {
                    showButtons();
                    v.startAnimation(fabOptionOpenAnim);
                }
                else {
                    hideButtons();
                    v.startAnimation(fabOptionCloseAnim);
                }

                areShowingButtons = !areShowingButtons;
            }
        });
    }

    private void showButtons() {
        btnUpdate.setVisibility(View.VISIBLE);
        btnRemove.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
    }

    private void hideButtons() {
        btnUpdate.setVisibility(View.INVISIBLE);
        btnRemove.setVisibility(View.INVISIBLE);
        btnBack.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("SimpleDateFormat")
    private void btnUpdateClick() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentInforActivity.this, AdditionStudentActivity.class);

                intent.putExtra("id_updated", student.getId());
                intent.putExtra("name_updated", student.getName());
                intent.putExtra("birth_date_updated", student.getBirthDate() != null ? new SimpleDateFormat("MM-dd-yyyy").format(student.getBirthDate()) : null);
                intent.putExtra("phone_updated", student.getPhone());
                intent.putExtra("mail_updated", student.getEmail());
                intent.putExtra("sex_updated", student.getSex() == Sex.MALE ? "male" : "female");
                intent.putExtra("position_updated", student.getPosition());
                intent.putExtra("profile_updated", BitmapUtility.getBytes(student.getAvatar()));

                startActivity(intent);
            }
        });
    }
}
