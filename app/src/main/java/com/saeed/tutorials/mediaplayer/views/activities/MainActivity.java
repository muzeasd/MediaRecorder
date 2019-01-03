package com.saeed.tutorials.mediaplayer.views.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.saeed.tutorials.mediaplayer.R;
import com.saeed.tutorials.mediaplayer.views.fragments.HistoryFragment;
import com.saeed.tutorials.mediaplayer.views.fragments.RecordFragment;

public class MainActivity extends AppCompatActivity
{

    TextView btnRecord;
    TextView btnHistory;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        btnRecord = (TextView) findViewById(R.id.btnRecordFragment);
        btnRecord.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectRecordFragment();
            }
        });

        btnHistory = (TextView) findViewById(R.id.btnHistoryFragment);
        btnHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectHistoryFragment();
            }
        });

        selectRecordFragment();

        if(!arePermissionsEnabled()){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private void selectRecordFragment(){
        btnRecord.setTextColor(getResources().getColor(R.color.white));
        btnRecord.setBackgroundColor(getResources().getColor(R.color.peacock));

        btnHistory.setTextColor(getResources().getColor(R.color.black));
        btnHistory.setBackgroundColor(getResources().getColor(R.color.fragment_options_bgcolor));

        RecordFragment fragment = new RecordFragment();
        fragment.setActivity(MainActivity.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    private void selectHistoryFragment(){
        btnRecord.setTextColor(getResources().getColor(R.color.black));
        btnRecord.setBackgroundColor(getResources().getColor(R.color.fragment_options_bgcolor));

        btnHistory.setTextColor(getResources().getColor(R.color.white));
        btnHistory.setBackgroundColor(getResources().getColor(R.color.peacock));

        HistoryFragment fragment = new HistoryFragment();
        fragment.setActivity(MainActivity.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
//        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
//        switch (item.getItemId()) {
//
//            case R.id.share:
//                Toast.makeText(MainActivity.this, "Share Button", Toast.LENGTH_LONG).show();
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_ALL)
        {
            boolean isAllPermissionGranted = arePermissionsEnabled();
            if(!isAllPermissionGranted){
                finish();
            }

        } else finish();
    }

    private boolean arePermissionsEnabled(){
        for(String permission : PERMISSIONS){
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case REQUEST_RECORD_AUDIO_PERMISSION:
//                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                break;
//        }
//        if (!permissionToRecordAccepted ) finish();
//        else {
//            startRecording();
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
