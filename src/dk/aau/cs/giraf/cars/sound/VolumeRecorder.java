package dk.aau.cs.giraf.cars.sound;

import android.media.MediaRecorder;

public class VolumeRecorder extends InputThread {
    private MediaRecorder mediaRecorder;

    public VolumeRecorder()
    {
        mediaRecorder = new MediaRecorder();
    }

    @Override
    public void run()
    {
        // Use this to get dB:
        mediaRecorder.getMaxAmplitude();
    }
}
