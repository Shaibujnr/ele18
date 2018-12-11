package com.easytekk.ele18;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.easytekk.ele18.models.Student;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by shaibu on 11/10/18.
 */

public class Helper {
    private SharedPreferences sp;
    private Context context;

    public Helper(Context context){
        this.context = context;
        this.sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void storeStudent(Student student){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("logged_in_student_name", student.getName());
        editor.putString("logged_in_student_matric_number", student.getMatric_number());
        editor.putString("logged_in_student_gender", student.getGender());
        editor.putString("logged_in_student_password", student.getPassword());
        editor.putString("logged_in_student_id", student.getId());
        editor.apply();

    }

    public Student getStudent(){
        Student student = new Student();
        if(sp.getString("logged_in_student_id",null) != null){
            student.setMatric_number(sp.getString("logged_in_student_matric_number",""));
            student.setName(sp.getString("logged_in_student_name",""));
            student.setGender(sp.getString("logged_in_student_gender",""));
            student.setPassword(sp.getString("logged_in_student_password",""));
            student.setId(sp.getString("logged_in_student_id",""));
            return student;
        }
        return null;
    }

    public void persistStudentInfo(String id, String password){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("current_student_id", id);
        editor.putString("current_student_password", password);
        editor.apply();
    }

    public String getCurrentStudentId(){
        return sp.getString("current_student_id", null);
    }

    public String getCurrentStudentPassword(){
        return sp.getString("current_student_password", null);
    }

    public void logOut(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("current_student_id", null);
        editor.putString("current_student_password", null);
        editor.apply();
    }

}
