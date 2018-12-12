package com.easytekk.ele18;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.easytekk.ele18.dummy.DummyContent;
import com.easytekk.ele18.dummy.DummyContent.DummyItem;
import com.easytekk.ele18.models.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StudentsFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;
    private DatabaseReference mStudentsReference;
    private ArrayList<Student> studentList;
    private ProgressBar mProgress;
    private SearchView search;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StudentsFragment() {
        Log.d("ELE_MAIN_LIST_FRAG_CONS", "List Fragment constructed");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        Context context = view.getContext();
        RecyclerView recyclerView =  view.findViewById(R.id.list);
        mProgress = view.findViewById(R.id.student_list_progress);
        mProgress.setVisibility(View.VISIBLE);

        mStudentsReference = FirebaseDatabase.getInstance().getReference("students");
        studentList = new ArrayList<Student>();
        final MyStudentRecyclerViewAdapter adapter = new MyStudentRecyclerViewAdapter(
                studentList,mListener,getContext());

        search = view.findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(adapter);

        Log.d("ELE_FRAG_CREATE_VIEW", "Fragment on create view");
        mStudentsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Student cs = new Student();
                String gender = dataSnapshot.child("gender").getValue().toString();
                cs.setMatric_number(dataSnapshot.child("matric").getValue().toString());
                cs.setName(dataSnapshot.child("name").getValue().toString());
                cs.setPhone_number(dataSnapshot.child("phone").getValue().toString());
                cs.setNick(dataSnapshot.child("nick").getValue().toString());
                cs.setGender(gender);
                cs.setPassword(dataSnapshot.child("pin").getValue().toString());
                final String resource_uri = String.
                        format("android.resource://com.easytekk.ele18/drawable/%s",
                                gender.toLowerCase());
                cs.setFacebook_username(dataSnapshot.child("facebook")
                        .getValue().toString());
                cs.setInstagram_username(dataSnapshot.child("instagram")
                        .getValue().toString());
                cs.setSnapchat_username(dataSnapshot.child("snapchat")
                        .getValue().toString());
                cs.setTwitter_username(dataSnapshot.child("twitter")
                        .getValue().toString());
                cs.setLinkedin_username(dataSnapshot.child("linkedin")
                        .getValue().toString());
                cs.setId(dataSnapshot.getKey());
                if(dataSnapshot.child("bio").exists()){
                    cs.setBio(dataSnapshot.child("bio").getValue().toString());
                }
                else{
                    cs.setBio("");
                }
                if(dataSnapshot.child("profile_photo").exists()){
                    cs.setProfile_picture(
                            Uri.parse(dataSnapshot.child("profile_photo").getValue().toString())
                    );
                }
                else{
                    cs.setProfile_picture(
                            Uri.parse(resource_uri)
                    );
                }
                if(dataSnapshot.child("email").exists()){
                    cs.setEmail(dataSnapshot.child("email").getValue().toString());
                }
                Log.d("ELE_FRAG_STUDENT", cs.getName());
                studentList.add(cs);
                mProgress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String id = dataSnapshot.getKey();
                Student ss = new Student();
                ss.setId(id);
                ss.setName(dataSnapshot.child("name").getValue().toString());
                ss.setEmail(dataSnapshot.child("email").getValue().toString());
                ss.setNick(dataSnapshot.child("nick").getValue().toString());
                ss.setMatric_number(dataSnapshot.child("matric").getValue().toString());
                ss.setPhone_number(dataSnapshot.child("phone").getValue().toString());
                ss.setGender(dataSnapshot.child("gender").getValue().toString());
                ss.setFacebook_username(dataSnapshot.child("facebook").getValue().toString());
                ss.setTwitter_username(dataSnapshot.child("twitter").getValue().toString());
                ss.setLinkedin_username(dataSnapshot.child("linkedin").getValue().toString());
                ss.setInstagram_username(dataSnapshot.child("instagram").getValue().toString());
                ss.setSnapchat_username(dataSnapshot.child("snapchat").getValue().toString());
                ss.setPassword(dataSnapshot.child("pin").getValue().toString());
                if(dataSnapshot.child("bio").exists()){
                    ss.setBio(dataSnapshot.child("bio").getValue().toString());
                }
                if(dataSnapshot.child("profile_photo").exists()){
                    ss.setProfile_picture(
                            Uri.parse(dataSnapshot.child("profile_photo").getValue().toString())
                    );
                }
                if(dataSnapshot.child("email").exists()){
                    ss.setEmail(dataSnapshot.child("email").getValue().toString());
                }
                adapter.updateStudent(ss);
                Log.d("FRAG_ON_CHILD_CHANGED", dataSnapshot.getKey());
                Log.d("FRAG_ON_CHILD_CHANGED_S", s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Student item);
    }


}
