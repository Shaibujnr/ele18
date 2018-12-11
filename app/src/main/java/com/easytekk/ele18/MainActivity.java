package com.easytekk.ele18;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easytekk.ele18.dummy.DummyContent;
import com.easytekk.ele18.models.Student;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements StudentsFragment.OnListFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private View mainContent;

    private FloatingActionButton fab;

    private ProfileFragment mProfileFragment;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ELE_MAIN_ON_CREATE","Called from mainactivity on create");
        doBefore();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainContent = findViewById(R.id.main_content);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setEnabled(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileFragment.editProfile();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            new Helper(this).logOut();
            gotoLogin();
            return true;
        }
        if(id == R.id.action_change_password){
            intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ELE_MAIN_ON_START","Called from mainactivity on start");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void doBefore(){
        Log.d("ELE_MAIN_DO_BEFORE", "called from main do before");
        String student_id = new Helper(this).getCurrentStudentId();
        if(student_id == null){
            Log.d("ELE_MAIN_Do_BEFORE_STD", "student not found");
            gotoLogin();
        }
    }

    private void gotoLogin(){
        Log.d("ELE_MAIN_GOTOL", "goto login called");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onListFragmentInteraction(Student item) {
        Intent intent = new Intent(this,StudentActivity.class);
        intent.putExtra("name", item.getName());
        intent.putExtra("nick", item.getNick());
        intent.putExtra("email", item.getEmail());
        intent.putExtra("phone", item.getPhone_number());
        intent.putExtra("matric", item.getMatric_number());
        intent.putExtra("gender", item.getGender());
        intent.putExtra("facebook", item.getFacebook_username());
        intent.putExtra("twitter", item.getTwitter_username());
        intent.putExtra("snapchat", item.getSnapchat_username());
        intent.putExtra("linkedin", item.getLinkedin_username());
        intent.putExtra("instagram", item.getInstagram_username());
        intent.putExtra("picture", item.getProfile_picture().toString());
        intent.putExtra("bio", item.getBio().toString());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ELE_MAIN_OAR", "On activity result called");
        Log.d("ELE_MAIN_OAR_DET", "requestcode: "+String.valueOf(requestCode)+
                " resultcdde: "+String.valueOf(resultCode));
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Snackbar.make(mainContent, resultUri.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mProfileFragment.updateProfilePhoto(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Snackbar.make(mainContent, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                if(data.getBooleanExtra("edit_profile",false)){
                    Log.d("ELE_MAIN_DETAILS", "Called from update details");
                    mProfileFragment.updateProfileDetails();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.d("ELE_MAIN_DETAILS", "called from update details cancelled");
            }
        }
    }

    public void activateFab(){
        fab.setEnabled(true);
    }

    public void deactivateFab(){
        fab.setEnabled(false);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
//                    fab.setVisibility(View.VISIBLE);
                    mProfileFragment = new ProfileFragment();
                    return mProfileFragment;
                case 1:
//                    fab.setVisibility(View.GONE);
                    return new StudentsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Profile";
                case 1:
                    return "Class";
            }
            return null;
        }
    }
}
