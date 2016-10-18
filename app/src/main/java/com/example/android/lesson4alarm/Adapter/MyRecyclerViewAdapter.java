package com.example.android.lesson4alarm.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.lesson4alarm.Activity.MainActivity;
import com.example.android.lesson4alarm.Alarm;
import com.example.android.lesson4alarm.R;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Alarm> mAlarms;

    public interface OnItemClick {
        void onItemClickListenerRedactor(int position);
        void onItemClickListenerStatus(int position);
    }

    OnItemClick positionListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageButton mImageButton;
        public LinearLayout mLinearLayout;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.time);
            mImageButton = (ImageButton) v.findViewById(R.id.alarm_button);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.row_linear_layout);
            Log.v("log", "View holder ok");
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerViewAdapter(ArrayList<Alarm> alarms, MainActivity mainActivity) {
        mAlarms = alarms;
        positionListener = mainActivity;

        Log.v("log", "MyRecyclerViewAdapter" + mAlarms);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(view);

        Log.v("log", "onCreateviewHolder"+ view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(String.format("%02d:%02d", mAlarms.get(position).hourOfDay, mAlarms.get(position).minute));
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionListener.onItemClickListenerRedactor(position);
            }
        });

        if (mAlarms.get(position).status) {
            holder.mImageButton.setImageResource(R.mipmap.alarm_active);
        } else {
            holder.mImageButton.setImageResource(R.mipmap.alarm_not_active);
        }

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionListener.onItemClickListenerStatus(position);
            }
        });
        Log.v("log", "onBind ok");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        Log.v("log", "get Count ok");

        return mAlarms.size();

    }

}