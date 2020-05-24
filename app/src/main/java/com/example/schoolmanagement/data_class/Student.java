package com.example.schoolmanagement.data_class;

import android.graphics.Bitmap;

import com.example.schoolmanagement.adapter.StudentAdapter;
import com.example.schoolmanagement.enums.Sex;
import com.example.schoolmanagement.utils.TableUtility;

import java.util.Date;

public class Student {
    private String id;
    private long order;
    private double avgScore;
    private String name;
    private String phone;
    private String email;
    private Sex sex;
    private String position;
    private Date birthDate;
    private Bitmap avatar;

    public Student(long order,
                   String id,
                   String name,
                   String phone,
                   String email,
                   Date birthDate,
                   double avgScore,
                   Bitmap avatar) {
        this.order = order;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.birthDate = birthDate;
        this.email = email;
        this.avgScore = avgScore;
        this.avatar = avatar;
    }

    public Student(String id,
                   String name,
                   String phone,
                   String email,
                   Sex sex,
                   Date birthDate,
                   String position,
                   Bitmap avatar) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.birthDate = birthDate;
        this.position = position;
        this.avatar = avatar;
    }

    public Student(String name, String phone, String mail, String position, Date birthDate, Bitmap profile) {
        this.name = name;
        this.phone = phone;
        this.email = mail;
        this.position = position;
        this.birthDate = birthDate;
        this.avatar = profile;
    }

    public Student(long order, String name, String phone, String email, Date birthDate) {
        this.order = order;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
    }

    public Student(long order, String name, String phone, String mail, String position, Date birthDate, Bitmap profile) {
        this.order = order;
        this.name = name;
        this.phone = phone;
        this.email = mail;
        this.position = position;
        this.birthDate = birthDate;
        this.avatar = profile;
    }

    public Student(String id, String name, String phone, String mail, String position, Date birthDate, Bitmap profile) {
        order = StudentAdapter.order++;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = mail;
        this.position = position;
        this.birthDate = birthDate;
        this.avatar = profile;
    }

    public Student() {

    }

    public void createNewId() {
        id = TableUtility.generateRandomId("ST", 10);
    }

    public void setId(String id) {this.id = id;}

    public String getId() {return id;}

    public long getOrder() {return order;}

    public void setOrder(long order) {this.order = order;}

    public double getAvgScore() {return avgScore;}

    public String getName() {return name;}

    public String getPosition() {return position;}

    public Bitmap getAvatar() {return avatar;}

    public String getPhone() {return phone;}

    public String getEmail() {return email;}

    public Sex getSex() {return sex;}

    public void setSex(Sex sex) {this.sex = sex;}

    public Date getBirthDate() {return birthDate;}
}
