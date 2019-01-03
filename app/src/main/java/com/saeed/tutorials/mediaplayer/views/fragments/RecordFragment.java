package com.saeed.tutorials.mediaplayer.views.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.saeed.tutorials.mediaplayer.R;
import com.saeed.tutorials.mediaplayer.views.activities.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment
{


    private int [] channels = {1, 2};
    private int [] bitRates = {8, 16, 32};
    private int [] samplingRates = {8000, 11025, 22050, 44100, 48000, 96000};

    private final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    private TextView mRecordButton = null;
    private Spinner spinnerCannels = null,spinnerBitDepth = null, spinnerSamplingRate= null ;

    private boolean permissionToRecordAccepted = false;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    boolean mStartRecording = true;
    boolean mStartPlaying = true;

    Activity mActivity;

    public RecordFragment()
    {
        // Required empty public constructor
    }

    public void setActivity(Activity activity)
    {
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mRecordButton = (TextView) view.findViewById(R.id.btnRecord);
        mRecordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    mRecordButton.setText("Stop recording");
                } else {
                    mRecordButton.setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        });

        ((TextView) view.findViewById(R.id.tvStorageInfo)).setText(mActivity.getExternalCacheDir().getAbsolutePath());

        spinnerCannels = (Spinner) view.findViewById(R.id.spinnerChannels);
        spinnerBitDepth = (Spinner) view.findViewById(R.id.spinnerBitDepth);
        spinnerSamplingRate = (Spinner) view.findViewById(R.id.spinnerSamplingRate);

        spinnerCannels.setSelection(1);
        spinnerBitDepth.setSelection(1);
        spinnerSamplingRate.setSelection(3);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1)
        {
            boolean isAllPermissionGranted = arePermissionsEnabled();
            if(isAllPermissionGranted)
                startRecording();
            else mActivity.finish();

        }
        else mActivity.finish();

    }

    private boolean arePermissionsEnabled(){
        for(String permission : PERMISSIONS){
            if(ActivityCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }


    private void onRecord(boolean start) {
        if (start) {

            // used for activity
            //ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);

            // used for v4 fragment
//            requestPermissions(PERMISSIONS, PERMISSION_ALL);
            startRecording();

        } else {
            stopRecording();
        }
    }

    private int getChannels(){
        String value = (String)spinnerCannels.getSelectedItem();
        if(value.trim().toLowerCase().equalsIgnoreCase("mono"))
            return channels[0];
        else return channels[1];
    }

    private int getBitDepth(){
        int bitDepthPosition = spinnerBitDepth.getSelectedItemPosition();//.getSelectedItem();
        return bitRates[bitDepthPosition];
    }

    private int getSamplingRates(){
        int samplingRatesPosition = spinnerSamplingRate.getSelectedItemPosition();//.getSelectedItem();
        return samplingRates[samplingRatesPosition];
    }

    private void startRecording() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");

        // Record to the external cache directory for visibility
        mFileName = mActivity.getExternalCacheDir().getAbsolutePath();
        mFileName += ( "/" + dateFormat.format(new Date()));
        mFileName += ".wav";


        mRecorder = new MediaRecorder();

        try {

            int channels = getChannels();
            int bitDepth = getBitDepth();
            int samplingRate = getSamplingRates();

            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            mRecorder.setAudioChannels(channels);
            mRecorder.setAudioEncodingBitRate(bitDepth);
            mRecorder.setAudioSamplingRate(samplingRate);

            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.prepare();

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {

//        sendEmail();

        if(mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

//        EventBus.getDefault().unregister(this);
    }

    private void sendEmail(){
        try {

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if(mFileName != null) {

                ArrayList<Uri> uris = new ArrayList<Uri>();

                Uri path = Uri.fromFile(new File(mFileName));
                uris.add(path);

                String to[] = {"muze.saeed@gmail.com"};

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("audio/*");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uris);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");

                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }
    }
}
