package com.easytekk.ele18;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easytekk.ele18.models.Student;

public class StudentActivity extends AppCompatActivity {
    private Student student;
    private ImageView profile_picture;
    private TextView name, nick, matric, gender, email, phone, facebook, twitter, linkedin, bio;
    private TextView instagram, snapchat;
    private ProgressBar photoProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Intent prev_intent = getIntent();
        student = new Student();

        student.setName(prev_intent.getStringExtra("name"));
        student.setNick(prev_intent.getStringExtra("nick"));
        student.setEmail(prev_intent.getStringExtra("email"));
        student.setPhone_number(prev_intent.getStringExtra("phone"));
        student.setMatric_number(prev_intent.getStringExtra("matric"));
        student.setGender(prev_intent.getStringExtra("gender"));
        student.setFacebook_username(prev_intent.getStringExtra("facebook"));
        student.setTwitter_username(prev_intent.getStringExtra("twitter"));
        student.setSnapchat_username(prev_intent.getStringExtra("snapchat"));
        student.setLinkedin_username(prev_intent.getStringExtra("linkedin"));
        student.setInstagram_username(prev_intent.getStringExtra("instagram"));
        student.setBio(prev_intent.getStringExtra("bio"));
        student.setProfile_picture(Uri.parse(prev_intent.getStringExtra("picture")));

        snapchat = (TextView) findViewById(R.id.student_snapchat);
        instagram = (TextView) findViewById(R.id.student_instagram);
        bio = (TextView) findViewById(R.id.student_bio);
        linkedin = (TextView) findViewById(R.id.student_linkedin);
        twitter = (TextView) findViewById(R.id.student_twitter);
        facebook = (TextView) findViewById(R.id.student_facebook);
        phone = (TextView) findViewById(R.id.student_phone);
        email = (TextView) findViewById(R.id.student_email);
        gender = (TextView) findViewById(R.id.student_gender);
        matric = (TextView) findViewById(R.id.student_matric);
        nick = (TextView) findViewById(R.id.student_nick);
        profile_picture = (ImageView) findViewById(R.id.student_image);
        name = (TextView) findViewById(R.id.student_name);;
        photoProgress = (ProgressBar) findViewById(R.id.student_photo_progress);
        photoProgress.setVisibility(View.VISIBLE);

        snapchat.setText(student.getSnapchat_username());
        instagram.setText(student.getInstagram_username());
        bio.setText(student.getBio());
        linkedin.setText(student.getLinkedin_username());
        twitter.setText(student.getTwitter_username());
        facebook.setText(student.getFacebook_username());
        phone.setText(student.getPhone_number());
        email.setText(student.getEmail());
        gender.setText(student.getGender());
        matric.setText(student.getMatric_number());
        nick.setText(student.getNick());
        name.setText(student.getName());
        Glide.with(this).load(student.getProfile_picture()).into(profile_picture);
        photoProgress.setVisibility(View.GONE);

        setTitle(student.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
