package com.example.schoolmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.schoolmanagement.R;
import com.example.schoolmanagement.enums.Sex;
import com.example.schoolmanagement.adapter.StudentAdapter;
import com.example.schoolmanagement.data_class.Student;
import com.example.schoolmanagement.database_handler.StudentHandler;
import com.example.schoolmanagement.utils.BitmapUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StudentsActivity extends AppCompatActivity {
    private boolean areShowingButtons;
    private FloatingActionButton fabOption;
    private ImageButton btnAddStudent;
    private ListView lvStudent;
    public static StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        fabOption = findViewById(R.id.fabOption);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        lvStudent = findViewById(R.id.lvStudent);

        fabOptionClick();
        btnAddStudentClick();

        try {
            loadStudentListView();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setListViewItemClick();
        setListViewItemLongClick();

        try {
            ibDeleteListViewItemClick();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showButtons() {
        btnAddStudent.setVisibility(View.VISIBLE);
    }

    private void hideButtons() {
        btnAddStudent.setVisibility(View.INVISIBLE);
    }

    private void fabOptionClick() {
        final Animation fabOptionOpenAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_top_left_90);
        final Animation fabOptionCloseAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_left_top_90);
        fabOption.setOnClickListener(new View.OnClickListener() {

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

    private void btnAddStudentClick() {
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdditionStudentActivity();
            }
        });
    }

    private void openAdditionStudentActivity() {
        Intent intent = new Intent(StudentsActivity.this, AdditionStudentActivity.class);
        startActivity(intent);
    }

    private void loadStudentListView() throws ParseException {
        if (null == lvStudent) {
            return;
        }

        StudentHandler handler = new StudentHandler(this);
        adapter = new StudentAdapter(handler.getAllStudents());
        lvStudent.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setListViewItemClick() {
        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = (Student) parent.getItemAtPosition(position);
                Intent intent = new Intent(StudentsActivity.this, StudentInforActivity.class);

                intent.putExtra("id", student.getId());
                intent.putExtra("name", student.getName());
                intent.putExtra("birth date", new SimpleDateFormat("MM-dd-yyyy").format(student.getBirthDate()));
                intent.putExtra("phone", student.getPhone());
                intent.putExtra("mail", student.getEmail());
                intent.putExtra("sex", student.getSex() == Sex.MALE ? "male" : "female");
                intent.putExtra("position", student.getPosition());
                intent.putExtra("profile", BitmapUtility.getBytes(student.getAvatar()));

                startActivity(intent);
            }
        });
    }

    private void setListViewItemLongClick() {
        final ImageButton ibDelete = findViewById(R.id.ibDeleteListViewItems);
        lvStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(100);
                ibDelete.setVisibility(View.VISIBLE);


                View listViewItem = null;
                for (int i = 0; i < lvStudent.getChildCount(); i++) {
                    listViewItem = lvStudent.getChildAt(i);
                    if (null != listViewItem) {
                        CheckBox cbSelected = listViewItem.findViewById(R.id.cbSelected);
                        cbSelected.setVisibility(View.VISIBLE);
                    }
                }

                CheckBox cbSelected = view.findViewById(R.id.cbSelected);
                cbSelected.setVisibility(View.VISIBLE);
                cbSelected.setSelected(false);

                return true;
            }
        });
    }

    private void ibDeleteListViewItemClick() throws ParseException {
        final ImageButton ibDelete = findViewById(R.id.ibDeleteListViewItems);
        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View listViewItem = null;
                int count = adapter.getCount();

                // The number of items have been removed
                int numRemoved = 0;
                for (int i = 0; i < count; i++) {
                    listViewItem = lvStudent.getChildAt(i);
                    CheckBox cbSelected = listViewItem.findViewById(R.id.cbSelected);

                    if (cbSelected.isChecked()) {
                        // The index of this item in adapter get value i
                        // But after removing items, the index of this item decrease numRemoved value
                        // Example: a item get index in adapter equals 12 at beginning
                        // After removing 5 item, it get 7
                        removeListViewItem(listViewItem, i - numRemoved);
                        numRemoved++;
                        lvStudent.invalidateViews();
                    }
                }
                
                StudentAdapter.order -= numRemoved;

                // Hide checkboxes that in remainder items
                hideCheckboxes();
                ibDelete.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void removeListViewItem(View listViewItem, int position) {
        TextView txtId = listViewItem.findViewById(R.id.txtStudentId);
        StudentHandler studentHandler = new StudentHandler(getBaseContext());
        // Remove the value in database, respectively
        studentHandler.deleteStudent(txtId.getText().toString());
        adapter.remove(position);
    }
    
    private void hideCheckboxes() {
        View listViewItem = null;
        for (int i = 0; i < adapter.getCount(); i++) {
            listViewItem = lvStudent.getChildAt(i);
            CheckBox cbSelected = listViewItem.findViewById(R.id.cbSelected);
            cbSelected.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideCheckboxes();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideCheckboxes();
        }

        return super.onKeyDown(keyCode, event);
    }
}
