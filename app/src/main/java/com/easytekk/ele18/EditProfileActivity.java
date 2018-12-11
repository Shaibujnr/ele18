package com.easytekk.ele18;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easytekk.ele18.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    Student student;
    EditText nick, email, facebook, twitter,instagram, linkedin, snapchat, bio, phone;
    ProgressBar progressBar;
    View main;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Profile");
        final Intent intent = getIntent();
        student = new Student();

        student.setName(intent.getStringExtra("name"));
        student.setNick(intent.getStringExtra("nick"));
        student.setEmail(intent.getStringExtra("email"));
        student.setPhone_number(intent.getStringExtra("phone"));
        student.setMatric_number(intent.getStringExtra("matric"));
        student.setGender(intent.getStringExtra("gender"));
        student.setFacebook_username(intent.getStringExtra("facebook"));
        student.setTwitter_username(intent.getStringExtra("twitter"));
        student.setSnapchat_username(intent.getStringExtra("snapchat"));
        student.setLinkedin_username(intent.getStringExtra("linkedin"));
        student.setInstagram_username(intent.getStringExtra("instagram"));
        student.setBio(intent.getStringExtra("bio"));


        snapchat = (EditText) findViewById(R.id.ep_snapchat);
        snapchat.setText(student.getSnapchat_username());

        instagram = (EditText) findViewById(R.id.ep_instagram);
        instagram.setText(student.getInstagram_username());

        bio = (EditText) findViewById(R.id.ep_bio);
        bio.setText(student.getBio());

        linkedin = (EditText) findViewById(R.id.ep_linkedin);
        linkedin.setText(student.getLinkedin_username());

        twitter = (EditText) findViewById(R.id.ep_twitter);
        twitter.setText(student.getTwitter_username());

        facebook = (EditText) findViewById(R.id.ep_facebook);
        facebook.setText(student.getFacebook_username());

        phone = (EditText) findViewById(R.id.ep_phone);
        phone.setText(student.getPhone_number());

        email = (EditText) findViewById(R.id.ep_email);
        email.setText(student.getEmail());

        nick = (EditText) findViewById(R.id.ep_nick);
        nick.setText(student.getNick());

        progressBar = (ProgressBar) findViewById(R.id.ep_progress);
        main = findViewById(R.id.ep_layout);
        done = (Button) findViewById(R.id.ep_done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("nick", nick.getText().toString());
                map.put("email", email.getText().toString());
                map.put("phone", phone.getText().toString());
                map.put("twitter", twitter.getText().toString());
                map.put("snapchat", snapchat.getText().toString());
                map.put("instagram", instagram.getText().toString());
                map.put("linkedin", linkedin.getText().toString());
                map.put("facebook", facebook.getText().toString());
                map.put("bio", bio.getText().toString());
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("students");
                String id = new Helper(EditProfileActivity.this).getCurrentStudentId();
                db.child(id).updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null){
                            progressBar.setVisibility(View.GONE);
                            Intent intent1 = new Intent();
                            intent1.putExtra("edit_profile", true);
                            setResult(RESULT_OK, intent1);
                            finish();
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            main.setVisibility(View.VISIBLE);
                            Snackbar.make(main, "unable to update profile", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            }
        });


    }
}
