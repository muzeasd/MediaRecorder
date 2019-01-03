package com.saeed.tutorials.mediaplayer.adapters;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saeed.tutorials.mediaplayer.R;
import com.saeed.tutorials.mediaplayer.navigators.EventBusEvents;
import com.saeed.tutorials.mediaplayer.pojo.RecordingClip;
import com.saeed.tutorials.mediaplayer.utils.ViewScaleHandler;

import org.greenrobot.eventbus.EventBus;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class HistoryRecordingsAdapter extends RecyclerView.Adapter<HistoryRecordingsAdapter.HistoryRecordingHolder>
{

    Activity mActivity;
    ArrayList<RecordingClip> listRecordings;

    public HistoryRecordingsAdapter(Activity activity, ArrayList<RecordingClip> listRecordings){
        this.mActivity = activity;
        this.listRecordings = listRecordings;
    }

    @Override
    public HistoryRecordingHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_recording_row, parent, false);
        new ViewScaleHandler(this.mActivity).scaleRootView(itemView);
        return new HistoryRecordingHolder(itemView);

    }

    @Override
    public void onBindViewHolder(HistoryRecordingHolder holder, int position)
    {
        if(listRecordings != null) {
            if(listRecordings.size() > 0) {
                final RecordingClip recordingClip = listRecordings.get(position);
                holder.tvRecordingName.setText(recordingClip.getRecordingName());

                holder.imgEmailButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(HistoryRecordingsAdapter.this.mActivity, "TODO :)", Toast.LENGTH_SHORT).show();
                    }
                });

                holder.imgPlayButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(HistoryRecordingsAdapter.this.mActivity, recordingClip.getRecordingName()+ " Play", Toast.LENGTH_SHORT).show();
                        EventBusEvents.OnPlayRecording onPlayRecording = new EventBusEvents.OnPlayRecording();
                        onPlayRecording.recordingPath = recordingClip.getRecordingPath();
                        onPlayRecording.recordingName = recordingClip.getRecordingName();
                        EventBus.getDefault().post(onPlayRecording);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount()
    {
        if(listRecordings == null)
            return 0;
        return listRecordings.size();
    }

    public class HistoryRecordingHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView tvRecordingName;
        public ImageView imgPlayButton, imgEmailButton;
        public SeekBar seekBarRecording;

        public HistoryRecordingHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvRecordingName = (TextView) itemView.findViewById(R.id.tvRecordingName);
            imgPlayButton = (ImageView) itemView.findViewById(R.id.imgPlayButton);
            seekBarRecording = (SeekBar) itemView.findViewById(R.id.seekBarRecording);
            imgEmailButton = (ImageView) itemView.findViewById(R.id.imgEmailButton);
        }
    }
}
