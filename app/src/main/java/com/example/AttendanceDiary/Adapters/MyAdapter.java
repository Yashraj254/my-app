package com.example.AttendanceDiary.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AttendanceDiary.MainActivity2;
import com.example.AttendanceDiary.Models.Model;
import com.example.AttendanceDiary.R;
import com.example.AttendanceDiary.Room.DbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Model> userList;
    private onClickListener clickListener;
    private onLongClickListener longClickListener;
    TextView detailedView;
    String table_name,newTable;
    DbHelper db;
    private static final String TAG = "MyAdapter";
    MainActivity2 myActivity;
    Context context;
    Model model;
    int status, totalCount, totalPresent, totalAbsent, totalPercentage;
    FloatingActionButton fab;
    String detail;
    public MyAdapter(ArrayList<Model> userList, onClickListener clickListener, onLongClickListener longClickListener,
                     Context context, String table_name, FloatingActionButton fab, TextView detailedView) {

        this.userList = userList;
        this.context = context;
        this.table_name = table_name;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.fab = fab;
        this.detailedView = detailedView;
        db = new DbHelper(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView nameText;
        RadioGroup radioGroup;
        RadioButton present, absent;
        LinearLayout mainLayout;
        private MyAdapter.onClickListener clickListener;
        private MyAdapter.onLongClickListener longClickListener;

        public MyViewHolder(@NonNull final View view, onClickListener clickListener, onLongClickListener longClickListener) {
            super(view);
            this.clickListener = clickListener;
            this.longClickListener = longClickListener;
            detailedView = view.findViewById(R.id.detailView);
            nameText = view.findViewById(R.id.textView1);
            radioGroup = view.findViewById(R.id.radioStatus);
            present = view.findViewById(R.id.present);
            absent = view.findViewById(R.id.absent);
            mainLayout = view.findViewById(R.id.mainLayout);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.onLongClick(getAdapterPosition());
            return true;
        }
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_items, parent, false);
        return new MyViewHolder(itemView, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {
        final String date = userList.get(position).getDate();
        final int state = userList.get(position).getStatus();
        holder.nameText.setText(date);
        if (state == 1) {
            holder.present.setChecked(true);
            holder.absent.setEnabled(false);
        } else if (state == 2) {
            holder.absent.setChecked(true);
            holder.present.setEnabled(false);
        }
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.present:

                        status = 1;
                        db.addDateData(date, status);
                        getData();
                        holder.absent.setEnabled(false);
                        fab.setVisibility(View.VISIBLE);

                        break;
                    case R.id.absent:

                        status = 2;
                        model = new Model(status);
                        db.addDateData(date, status);
                        getData();
                        holder.present.setEnabled(false);
                        fab.setVisibility(View.VISIBLE);

                        break;
                }
            }
        });
    }

    public void getData() {
        totalCount = db.totalCount(table_name);
        totalAbsent = db.getAbsentCount(table_name);
        totalPresent = db.getPresentCount(table_name);
        totalPercentage = 0;

        try {
            totalPercentage = totalPresent * 100 / totalCount;
            double requiredAttendance = ((0.75 * totalCount) - totalPresent)/0.25;
            int attendance = (int)requiredAttendance;
            if(totalPercentage<75)
                detail = "You have to attend " + attendance + " more classes to maintain 75% attendance";
            else
                detail = "Your attendance is above 75%, Keep up!!!";

            MainActivity2.update_counter(detail);

            notifyDataSetChanged();
            Log.d(TAG, "Total Count: "+totalCount+" Total Present: "+totalPresent+" Total Absent: "+totalAbsent);
            db.updateData(totalCount, table_name, totalPresent, totalAbsent, totalPercentage);

        } catch (Exception e) {
            Log.d(TAG, "Detail: "+detail);
            Log.d(TAG, "Error generated Table name: "+table_name+" Total Count: "+totalCount+" Total Present: "+totalPresent+" Total Absent: "+totalAbsent);
            db.updateData(totalCount, newTable, totalPresent, totalAbsent, totalPercentage);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface onClickListener {
        void onClick(int position);
    }

    public interface onLongClickListener {
        boolean onLongClick(int position);
    }
}
