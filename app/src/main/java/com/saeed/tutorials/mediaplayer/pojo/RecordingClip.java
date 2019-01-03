package com.saeed.tutorials.mediaplayer.pojo;

import java.io.Serializable;

public class RecordingClip implements Serializable
{

    int recordingId;
    String recordingName;
    String recordingPath;
    String recordingDateTime;
    String recordingDuration;

    public int getRecordingId()
    {
        return recordingId;
    }

    public void setRecordingId(int recordingId)
    {
        this.recordingId = recordingId;
    }

    public String getRecordingName()
    {
        return recordingName;
    }

    public void setRecordingName(String recordingName)
    {
        this.recordingName = recordingName;
    }

    public String getRecordingPath()
    {
        return recordingPath;
    }

    public void setRecordingPath(String recordingPath)
    {
        this.recordingPath = recordingPath;
    }

    public String getRecordingDateTime()
    {
        return recordingDateTime;
    }

    public void setRecordingDateTime(String recordingDateTime)
    {
        this.recordingDateTime = recordingDateTime;
    }

    public String getRecordingDuration()
    {
        return recordingDuration;
    }

    public void setRecordingDuration(String recordingDuration)
    {
        this.recordingDuration = recordingDuration;
    }
}
