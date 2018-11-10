package com.easytekk.ele18;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.easytekk.ele18.models.Student;

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
        editor.putString("logged_in_student_email", student.getEmail());
        editor.putString("logged_in_student_matric_number", student.getMatric_number());
        editor.putString("logged_in_student_nick", student.getNick());
        editor.putString("logged_in_student_phone_number", student.getPhone_number());
        editor.putString("logged_in_student_gender", student.getGender());
        editor.putString("logged_in_student_bio", student.getBio());
        editor.putString("logged_in_student_facebook_username", student.getFacebook_username());
        editor.putString("logged_in_student_instagram_username", student.getInstagram_username());
        editor.putString("logged_in_student_twitter_username", student.getTwitter_username());
        editor.putString("logged_in_student_linkedin_username", student.getLinkedin_username());
        editor.putString("logged_in_student_profile_picture", student.getProfile_picture().toString());
        editor.putString("logged_in_student_snapchat_username", student.getSnapchat_username());
        editor.putString("logged_in_student_password", student.getPassword());
        editor.apply();

    }

    public Student getStudent(){
        Student student = new Student();
        if(sp.getString("logged_in_student_id",null) != null){
            Uri profile_picture = Uri.parse(sp.getString("logged_in_user_profile_picture",""));
            student.setMatric_number(sp.getString("logged_in_student_matric_number",""));
            student.setEmail(sp.getString("logged_in_student_email",""));
            student.setName(sp.getString("logged_in_student_name",""));
            student.setNick(sp.getString("logged_in_student_nick",""));
            student.setPhone_number(sp.getString("logged_in_student_phone_number",""));
            student.setGender(sp.getString("logged_in_student_gender",""));
            student.setBio(sp.getString("logged_in_student_bio",""));
            student.setFacebook_username(sp.getString("logged_in_student_facebook_username",""));
            student.setInstagram_username(sp.getString("logged_in_student_instagram_username",""));
            student.setTwitter_username(sp.getString("logged_in_student_twitter_username",""));
            student.setLinkedin_username(sp.getString("logged_in_student_linkedin_username",""));
            student.setProfile_picture(profile_picture);
            student.setSnapchat_username(sp.getString("logged_in_student_snapchat_username",""));
            student.setPassword(sp.getString("logged_in_student_password",""));
            student.setId(sp.getString("logged_in_student_id",""));
            return student;
        }
        return null;
    }
}
