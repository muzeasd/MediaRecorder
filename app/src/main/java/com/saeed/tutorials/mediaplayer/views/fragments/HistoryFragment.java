package com.saeed.tutorials.mediaplayer.views.fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.saeed.tutorials.mediaplayer.R;
import com.saeed.tutorials.mediaplayer.adapters.HistoryRecordingsAdapter;
import com.saeed.tutorials.mediaplayer.navigators.EventBusEvents;
import com.saeed.tutorials.mediaplayer.pojo.RecordingClip;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment
{
    Activity mActivity;
    private MediaPlayer mPlayer = null;

    private final String LOG_TAG = "AudioRecordTest";
    RecyclerView listRecordingsRecyclerView;
    HistoryRecordingsAdapter historyRecordingsAdapter;
    List<RecordingClip> mListRecording;

    Handler mSeekbarUpdateHandler = null;
    Runnable mUpdateSeekbar = null;

    public HistoryFragment()
    {
        // Required empty public constructor
    }

    public void setActivity(Activity activity)
    {
        this.mActivity = activity;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mListRecording = prepareHistoryRecordingsAdapter();
        historyRecordingsAdapter = new HistoryRecordingsAdapter(mActivity, (ArrayList<RecordingClip>) mListRecording);

        listRecordingsRecyclerView = (RecyclerView) view.findViewById(R.id.listRecordingsRecyclerView);
        listRecordingsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        listRecordingsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        listRecordingsRecyclerView.setAdapter(historyRecordingsAdapter);
        historyRecordingsAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusEvents.OnPlayRecording event)
    {
        startPlaying(event.recordingPath, event.recordingName);
    }

    private ArrayList<RecordingClip> prepareHistoryRecordingsAdapter(){

        ArrayList<RecordingClip> listRecordings = new ArrayList<>();

        String path = getActivity().getExternalCacheDir().getAbsolutePath();
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            RecordingClip rc = new RecordingClip();
            rc.setRecordingPath(files[i].getParent());
            rc.setRecordingName(files[i].getName());
            listRecordings.add(rc);
        }

        return listRecordings;
    }

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(MediaPlayer mp)
        {
            stopPlaying();
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
        }
    };

    private int findRecordingTrackIndex(String fileName){
        for (RecordingClip rc : mListRecording){
            if(rc.getRecordingName().equalsIgnoreCase(fileName.toLowerCase())){
                return mListRecording.indexOf(rc);
            }
        }

        return -1;
    }

    private void hideAllSeekbar(){
        for (int index=0; index<mListRecording.size(); index++) {
            View view = listRecordingsRecyclerView.getLayoutManager().findViewByPosition(index);
            SeekBar seekBar = view.findViewById(R.id.seekBarRecording);
            seekBar.setVisibility(View.GONE);
        }
    }

    private void startPlaying(String filePath, String fileName) {

        String sFileToPlay = filePath+"/"+fileName;

        if(mPlayer != null) {
            stopPlaying();
        }

        int recordingTrackIndex = findRecordingTrackIndex(fileName);
        View view = listRecordingsRecyclerView.getLayoutManager().findViewByPosition(recordingTrackIndex);
        final SeekBar seekBar = view.findViewById(R.id.seekBarRecording);
        seekBar.setVisibility(View.VISIBLE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    mPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setOnCompletionListener(onCompletionListener);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(sFileToPlay);
            mPlayer.prepare();

            final int duration = mPlayer.getDuration();
            final int amountToUpdate = duration / 100;
            seekBar.setMax(duration);

            mSeekbarUpdateHandler = new Handler();
            mUpdateSeekbar = new Runnable() {
                @Override
                public void run() {
                    if(mPlayer == null)
                        return;

                    seekBar.setProgress(mPlayer.getCurrentPosition());
                    mSeekbarUpdateHandler.postDelayed(this, amountToUpdate);
                }
            };
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, amountToUpdate);

            mPlayer.start();

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void stopPlaying() {
        hideAllSeekbar();
        mPlayer.release();
        mPlayer = null;
    }


}
