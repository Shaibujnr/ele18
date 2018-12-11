package com.easytekk.ele18;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.easytekk.ele18.models.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {
    // UI references.
    private EditText mOldPasswordView;
    private EditText mNewPasswordView;
    private EditText mConfirmPasswordView;
    private View mProgressView;
    private View mChangePasswordFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        // Set up the login form.
        mOldPasswordView = (EditText) findViewById(R.id.old_password);

        mNewPasswordView= (EditText) findViewById(R.id.new_password);

        mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);



        Button mChangePasswordButton = (Button) findViewById(R.id.change_password_button);
        mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChangePassword();
            }
        });

        mChangePasswordFormView = findViewById(R.id.change_password_form);
        mProgressView = findViewById(R.id.change_password_progress);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptChangePassword() {
        // Reset errors.
        mOldPasswordView.setError(null);
        mNewPasswordView.setError(null);
        mConfirmPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String old_password = mOldPasswordView.getText().toString();
        String new_password = mNewPasswordView.getText().toString();
        String confirm_password = mConfirmPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(old_password)) {
            mOldPasswordView.setError(getString(R.string.error_field_required));
            focusView = mOldPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(new_password)) {
            mNewPasswordView.setError(getString(R.string.error_field_required));
            focusView = mNewPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(confirm_password)) {
            mConfirmPasswordView.setError(getString(R.string.error_field_required));
            focusView = mConfirmPasswordView;
            cancel = true;
        }
        if(!old_password.equals(new Helper(this).getCurrentStudentPassword())){
            mOldPasswordView.setError("Wrong password");
            focusView = mOldPasswordView;
            cancel = true;
        }
        if(old_password.equals(new_password)){
            mNewPasswordView.setError("Can't change to the same password");
            focusView = mNewPasswordView;
            cancel=true;
        }
        if(!new_password.equals(confirm_password)){
            mConfirmPasswordView.setError("Passwords do not match");
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("students");
            db.child(new Helper(this).getCurrentStudentId()).child("pin")
                    .setValue(new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    showProgress(false);
                    Snackbar.make(mChangePasswordFormView,"Password changed successfully"
                            ,BaseTransientBottomBar.LENGTH_LONG)
                            .show();
                }
            });


        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mChangePasswordFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mChangePasswordFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mChangePasswordFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mChangePasswordFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
