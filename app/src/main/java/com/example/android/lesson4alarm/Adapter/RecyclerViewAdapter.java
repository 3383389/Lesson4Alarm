package com.example.android.lesson4alarm.Adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.android.lesson4alarm.EventBus.MessageEvent;
import com.example.android.lesson4alarm.EventBus.Messages;
import com.example.android.lesson4alarm.R;
import com.example.android.lesson4alarm.SingletonAlarm;
import org.greenrobot.eventbus.EventBus;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private SingletonAlarm sAlarms;

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
    public RecyclerViewAdapter() {
        sAlarms = SingletonAlarm.getInstatce();

        Log.v("log", "RecyclerViewAdapter" + sAlarms);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(view);

        Log.v("log", "onCreateviewHolder" + view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(String.format("%02d:%02d", sAlarms.getAlarms().get(position).hourOfDay, sAlarms.getAlarms().get(position).minute));
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(Messages.REDACTOR_ALARM, position));
            }
        });

        setStatusImage(holder, position);

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(Messages.CHANGE_STATUS, position));
                setStatusImage(holder, position);
            }
        });
        Log.v("log", "onBind ok");

    }

    private void setStatusImage(ViewHolder holder, int position) {
        if (sAlarms.getAlarms().get(position).status) {
            holder.mImageButton.setImageResource(R.mipmap.alarm_active);
        } else {
            holder.mImageButton.setImageResource(R.mipmap.alarm_not_active);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        Log.v("log", "get Count ok");

        return sAlarms.getAlarms().size();

    }

}