package com.ltr.taskmanagementsystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Akshat Sharma on 13-02-2018.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTaskName, tvPriority, tvExpFinishDate;

        private TaskViewHolder(View view) {
            super(view);
            tvTaskName = view.findViewById(R.id.tvTaskName);
            tvPriority = view.findViewById(R.id.tvPriority);
            tvExpFinishDate = view.findViewById(R.id.tvExpFinishDate);
        }
    }

    private final LayoutInflater mInflater;
    private List<Task> mTasks; // cached copy of tasks

    TaskAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_view_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        if(mTasks != null) {
            Task current = mTasks.get(position);
            holder.tvTaskName.setText(current.getSubject());
            holder.tvPriority.setText(current.getPriority());
            holder.tvExpFinishDate.setText(current.getExp_finish_date());
        }
    }

    void setTasks(List<Task> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mTasks != null) {
            return mTasks.size();
        } else {
            return 0;
        }
    }

}
