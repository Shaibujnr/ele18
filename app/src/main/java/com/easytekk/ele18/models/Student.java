package com.easytekk.ele18.models;

import android.net.Uri;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

/**
 * Created by shaibu on 11/9/18.
 */

public class Student implements Serializable{
    private String id;
    private String matric_number;
    private String name;
    private String nick;
    private String gender;
    private String email;
    private String bio;
    private String snapchat_username;
    private String twitter_username;
    private String facebook_username;
    private String linkedin_username;
    private String instagram_username;
    private String phone_number;
    private Uri profile_picture;
    private String password;

    public Student(){

    }

    public Student(String matric_number, String name) {
        this.matric_number = matric_number;
        this.name = name;
    }



    public Student(String matric_number) {
        this.matric_number = matric_number;
    }



    public String getMatric_number() {
        return matric_number;
    }

    public void setMatric_number(String matric_number) {
        this.matric_number = matric_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSnapchat_username() {
        return snapchat_username;
    }

    public void setSnapchat_username(String snapchat_username) {
        this.snapchat_username = snapchat_username;
    }

    public String getTwitter_username() {
        return twitter_username;
    }

    public void setTwitter_username(String twitter_username) {
        this.twitter_username = twitter_username;
    }

    public String getFacebook_username() {
        return facebook_username;
    }

    public void setFacebook_username(String facebook_username) {
        this.facebook_username = facebook_username;
    }

    public String getLinkedin_username() {
        return linkedin_username;
    }

    public void setLinkedin_username(String linkedin_username) {
        this.linkedin_username = linkedin_username;
    }

    public String getInstagram_username() {
        return instagram_username;
    }

    public void setInstagram_username(String instagram_username) {
        this.instagram_username = instagram_username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Uri getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(Uri profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
