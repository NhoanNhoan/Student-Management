package com.example.schoolmanagement.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolmanagement.R;
import com.example.schoolmanagement.data_class.Student;

import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter {
    public static int order = 0;
    private ArrayList<Student> listStudent;

    public StudentAdapter(ArrayList<Student> listStudent) {
        this.listStudent = listStudent;
    }

    @Override
    public int getCount() {
        return listStudent.size();
    }

    @Override
    public Object getItem(int position) {
        return listStudent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listStudent.get(position).getOrder();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (null == convertView) {
            view = View.inflate(parent.getContext(), R.layout.student_listview_item, null);
        }
        else {
            view = convertView;
        }

        Student student = (Student)getItem(position);
        ((TextView)view.findViewById(R.id.txtStudentName)).setText(student.getName());
        ((TextView)view.findViewById(R.id.txtStudentId)).setText(student.getId());

        if (student.getAvatar() != null) {
            ((ImageView) view.findViewById(R.id.ivAvatar)).setImageBitmap(student.getAvatar());
        }

        return view;
    }

    public void addItem(Object item) {
        listStudent.add((Student)item);
    }

    public void remove(int index) {
        listStudent.remove(index);
    }
}
