package com.ltr.taskmanagementsystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Akshat Sharma on 26-02-2018.
 */

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    class MeetingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMeetingName, tvMeetingFrequency, tvMeetingDate, tvMeetingId;

        private MeetingViewHolder(View view) {
            super(view);
            tvMeetingName = view.findViewById(R.id.tvMeetingName);
            tvMeetingFrequency = view.findViewById(R.id.tvFrequency);
            tvMeetingDate = view.findViewById(R.id.tvMeetingDate);
            tvMeetingId = view.findViewById(R.id.tvMeetingId);
        }
    }

    private final LayoutInflater mInflater;
    private List<Meeting> mMeetings; // cached copy of meetings

    MeetingAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_view_meeting, parent, false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {
        if(mMeetings != null) {
            Meeting current = mMeetings.get(position);
            holder.tvMeetingName.setText(current.getTitle());
            holder.tvMeetingFrequency.setText(current.getFrequency());
            holder.tvMeetingDate.setText(current.getMeeting_date());
            holder.tvMeetingId.setText(Integer.toString(current.getMeeting_id()));
        }
    }

    void setMeetings(List<Meeting> meetings) {
        mMeetings = meetings;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mMeetings != null) {
            return mMeetings.size();
        } else {
            return 0;
        }
    }
}

