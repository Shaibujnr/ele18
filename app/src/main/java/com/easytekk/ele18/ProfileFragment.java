package com.easytekk.ele18;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easytekk.ele18.models.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private ImageView profile_picture;
    private TextView name, nick, matric, gender, email, phone, facebook, twitter, linkedin, bio;
    private TextView instagram, snapchat;
    private Button changePhoto, removePhoto;
    private ProgressBar photoProgress, detailProgress;
    private Student student;
    private View dc;


    public ProfileFragment() {
        // Required empty public constructor
        Log.d("ELE_MAIN_PROF_CONS", "From profile fragment constructr");
        student = new Student();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("ELE_MAIN_PROF_VIEW", "From profile fragment on create view");
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);
        snapchat = v.findViewById(R.id.profile_snapchat);
        instagram = v.findViewById(R.id.profile_instagram);
        bio = v.findViewById(R.id.profile_bio);
        linkedin = v.findViewById(R.id.profile_linkedin);
        twitter = v.findViewById(R.id.profile_twitter);
        facebook = v.findViewById(R.id.profile_facebook);
        phone = v.findViewById(R.id.profile_phone);
        email = v.findViewById(R.id.profile_email);
        gender = v.findViewById(R.id.profile_gender);
        matric = v.findViewById(R.id.profile_matric);
        nick = v.findViewById(R.id.profile_nick);
        profile_picture = v.findViewById(R.id.profile_image);
        name = v.findViewById(R.id.profile_name);
        changePhoto = v.findViewById(R.id.profile_btn_change_photo);
        removePhoto = v.findViewById(R.id.profile_btn_remove_photo);
        photoProgress = v.findViewById(R.id.profile_photo_progress);
        detailProgress = v.findViewById(R.id.profile_details_progress);
        dc = v.findViewById(R.id.details_container);

        photoProgress.setVisibility(View.VISIBLE);
        changePhoto.setEnabled(false);
        removePhoto.setEnabled(false);

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getActivity());

            }
        });

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference("students");
        dbref.child(new Helper(getContext()).getCurrentStudentId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        student.setName(dataSnapshot.child("name").getValue().toString());
                        name.setText(dataSnapshot.child("name").getValue().toString());

                        student.setEmail(dataSnapshot.child("email").getValue().toString());
                        email.setText(dataSnapshot.child("email").getValue().toString());

                        student.setMatric_number(dataSnapshot.child("matric").getValue().toString());
                        matric.setText(dataSnapshot.child("matric").getValue().toString());
                        
                        student.setPhone_number(dataSnapshot.child("phone").getValue().toString());
                        phone.setText(dataSnapshot.child("phone").getValue().toString());
                        
                        student.setGender(dataSnapshot.child("gender").getValue().toString());
                        gender.setText(dataSnapshot.child("gender").getValue().toString());
                        
                        student.setNick(dataSnapshot.child("nick").getValue().toString());
                        nick.setText(dataSnapshot.child("nick").getValue().toString());
                        
                        student.setSnapchat_username(dataSnapshot.child("snapchat").getValue().toString());
                        snapchat.setText(dataSnapshot.child("snapchat").getValue().toString());
                        
                        student.setInstagram_username(dataSnapshot.child("instagram").getValue().toString());
                        instagram.setText(dataSnapshot.child("instagram").getValue().toString());
                        
                        student.setTwitter_username(dataSnapshot.child("twitter").getValue().toString());
                        twitter.setText(dataSnapshot.child("twitter").getValue().toString());
                        
                        student.setLinkedin_username(dataSnapshot.child("linkedin").getValue().toString());
                        linkedin.setText(dataSnapshot.child("linkedin").getValue().toString());
                        
                        student.setFacebook_username(dataSnapshot.child("facebook").getValue().toString());
                        facebook.setText(dataSnapshot.child("facebook").getValue().toString());

                        if(dataSnapshot.child("bio").exists()){
                            student.setBio(dataSnapshot.child("bio").getValue().toString());
                            bio.setText(dataSnapshot.child("bio").getValue().toString());
                        }else{
                            student.setBio("");
                            bio.setText("");
                        }

                        
                        if(dataSnapshot.child("profile_photo").exists()){
                            Glide.with(getContext())
                                    .load(dataSnapshot.child("profile_photo").getValue().toString())
                                    .into(profile_picture);
                        }
                        else{
                            String uri = "android.resource://com.easytekk.ele18/drawable/"+
                                    dataSnapshot.child("gender").getValue().toString().toLowerCase();
                            Glide.with(getContext())
                                    .load(Uri.parse(uri))
                                    .into(profile_picture);
                        }

                        photoProgress.setVisibility(View.GONE);
                        changePhoto.setEnabled(true);
                        removePhoto.setEnabled(true);

                        MainActivity activity = (MainActivity) getActivity();
                        activity.activateFab();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return v;
    }

    public void updateProfilePhoto(final Uri photo_uri){
        photoProgress.setVisibility(View.VISIBLE);
        StorageReference ref = FirebaseStorage.getInstance().getReference("profile_photos");
        final StorageReference imgref = ref.child(new Helper(getContext()).getCurrentStudentId()+".jpg");
        UploadTask uploadTask = imgref.putFile(photo_uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                photoProgress.setVisibility(View.GONE);
                Snackbar.make(profile_picture, "Unable to upload image", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                String url = taskSnapshot.getMetadata().getDownloadUrl().toString();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference dbref = db.getReference("students");
                dbref.child(new Helper(getContext()).getCurrentStudentId()).child("profile_photo")
                        .setValue(url);
                photoProgress.setVisibility(View.GONE);
                Glide.with(getContext()).load(photo_uri).into(profile_picture);
                Snackbar.make(profile_picture, "Successfully uploaded image", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Snackbar.make(profile_picture, imgref.getDownloadUrl().toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void editProfile(){
        Intent intent = new Intent(getActivity(),EditProfileActivity.class);
        intent.putExtra("name", student.getName());
        intent.putExtra("nick", student.getNick());
        intent.putExtra("email", student.getEmail());
        intent.putExtra("phone", student.getPhone_number());
        intent.putExtra("matric", student.getMatric_number());
        intent.putExtra("gender", student.getGender());
        intent.putExtra("facebook", student.getFacebook_username());
        intent.putExtra("twitter", student.getTwitter_username());
        intent.putExtra("snapchat", student.getSnapchat_username());
        intent.putExtra("linkedin", student.getLinkedin_username());
        intent.putExtra("instagram", student.getInstagram_username());
        intent.putExtra("bio", student.getBio());
        getActivity().startActivityForResult(intent, 1);
    }

    public void updateProfileDetails(){
        changePhoto.setEnabled(false);
        removePhoto.setEnabled(false);
        dc.setVisibility(View.GONE);
        detailProgress.setVisibility(View.VISIBLE);
        MainActivity activity = (MainActivity) getActivity();
        activity.deactivateFab();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference("students");
        dbref.child(new Helper(getContext()).getCurrentStudentId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        student.setName(dataSnapshot.child("name").getValue().toString());
                        name.setText(dataSnapshot.child("name").getValue().toString());

                        student.setEmail(dataSnapshot.child("email").getValue().toString());
                        email.setText(dataSnapshot.child("email").getValue().toString());

                        student.setMatric_number(dataSnapshot.child("matric").getValue().toString());
                        matric.setText(dataSnapshot.child("matric").getValue().toString());

                        student.setPhone_number(dataSnapshot.child("phone").getValue().toString());
                        phone.setText(dataSnapshot.child("phone").getValue().toString());

                        student.setGender(dataSnapshot.child("gender").getValue().toString());
                        gender.setText(dataSnapshot.child("gender").getValue().toString());

                        student.setNick(dataSnapshot.child("nick").getValue().toString());
                        nick.setText(dataSnapshot.child("nick").getValue().toString());

                        student.setSnapchat_username(dataSnapshot.child("snapchat").getValue().toString());
                        snapchat.setText(dataSnapshot.child("snapchat").getValue().toString());

                        student.setInstagram_username(dataSnapshot.child("instagram").getValue().toString());
                        instagram.setText(dataSnapshot.child("instagram").getValue().toString());

                        student.setTwitter_username(dataSnapshot.child("twitter").getValue().toString());
                        twitter.setText(dataSnapshot.child("twitter").getValue().toString());

                        student.setLinkedin_username(dataSnapshot.child("linkedin").getValue().toString());
                        linkedin.setText(dataSnapshot.child("linkedin").getValue().toString());

                        student.setFacebook_username(dataSnapshot.child("facebook").getValue().toString());
                        facebook.setText(dataSnapshot.child("facebook").getValue().toString());

                        if(dataSnapshot.child("bio").exists()){
                            student.setBio(dataSnapshot.child("bio").getValue().toString());
                            bio.setText(dataSnapshot.child("bio").getValue().toString());
                        }else{
                            student.setBio("");
                            bio.setText(dataSnapshot.child("bio").getValue().toString());
                        }

                        changePhoto.setEnabled(true);
                        removePhoto.setEnabled(true);
                        dc.setVisibility(View.VISIBLE);
                        detailProgress.setVisibility(View.GONE);
                        MainActivity activity = (MainActivity) getActivity();
                        activity.activateFab();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
