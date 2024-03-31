package com.heartmonitoring.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.MyViewHolder> {

    Context context;
    List<RecordingModel> list;
    SimpleDateFormat sdf = new SimpleDateFormat();
    RecordingClickedListener recordingClickedListener;
    int selectedPosition;

    public RecordingAdapter(Context context, List<RecordingModel> list, RecordingClickedListener recordingClickedListener) {
        this.context = context;
        this.list = list;
        this.recordingClickedListener = recordingClickedListener;
        sdf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
        selectedPosition = -1;
    }

    public interface RecordingClickedListener {

        void onRecordingClicked(RecordingModel rm);

    }

    @NonNull
    @Override
    public RecordingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordingAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_recording, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingAdapter.MyViewHolder holder, int position) {
        RecordingModel rm = list.get(position);

        if(rm.getName()==null) rm.setName("No Name");
        holder.name.setText(rm.getName());
        holder.note.setText(rm.getNote());
        holder.time.setText(sdf.format(rm.getTimeStamp()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedPosition = position;

                recordingClickedListener.onRecordingClicked(rm);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, time, note;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            note = itemView.findViewById(R.id.note);
            time = itemView.findViewById(R.id.date);

        }
    }
}
