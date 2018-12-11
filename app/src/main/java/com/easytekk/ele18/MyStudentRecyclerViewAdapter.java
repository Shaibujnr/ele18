package com.easytekk.ele18;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easytekk.ele18.StudentsFragment.OnListFragmentInteractionListener;
import com.easytekk.ele18.dummy.DummyContent.DummyItem;
import com.easytekk.ele18.models.Student;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyStudentRecyclerViewAdapter
        extends RecyclerView.Adapter<MyStudentRecyclerViewAdapter.ViewHolder>
        implements Filterable {

    private final ArrayList<Student> mStudents;
    private ArrayList<Student> filteredList;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;
    private StudentFilter studentFilter;

    public MyStudentRecyclerViewAdapter(ArrayList<Student> students,
                                        OnListFragmentInteractionListener listener, Context context) {
        mStudents = students;
        filteredList = mStudents;
        mListener = listener;
        mContext = context;

        getFilter();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mStudent = filteredList.get(position);
        Glide.with(mContext)
                .load(holder.mStudent.getProfile_picture().toString())
                .centerCrop()
                .into(holder.mImg);
        holder.mName.setText(holder.mStudent.getName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mStudent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        if (studentFilter == null) {
            studentFilter= new StudentFilter();
        }

        return studentFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircularImageView mImg;
        public final TextView mName;
        public Student mStudent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImg = view.findViewById(R.id.student_list_img);
            mName = view.findViewById(R.id.student_list_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }

    public class StudentFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults fr = new FilterResults();
            if (charSequence!=null && charSequence.length()>0) {
                ArrayList<Student> tempList = new ArrayList<Student>();

                // search content in friend list
                for (Student student : mStudents) {
                    if (student.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        tempList.add(student);
                    }
                }

                fr.count = tempList.size();
                fr.values = tempList;
            } else {
                fr.count = mStudents.size();
                fr.values = mStudents;
            }

            return fr;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredList = (ArrayList<Student>) filterResults.values;
            notifyDataSetChanged();
        }
    }

}
