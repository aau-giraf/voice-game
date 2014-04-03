package dk.aau.cs.giraf.cars.game;

import android.media.MediaRecorder;

import java.io.IOException;

import dk.aau.cs.giraf.cars.framework.Game;

public class VolumeCarControl implements CarControl {
    private MediaRecorder mediaRecorder;

    private final float lower = 500f;
    private final float upper = 5000f;

    public VolumeCarControl() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile("/dev/null");
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    @Override
    public float getMove(Game game) {

        float volume = (float)mediaRecorder.getMaxAmplitude();

        if (volume < lower)
            return 0;

        if (volume > upper)
            volume = upper;

        volume -= lower;
        volume /= (upper - lower) / 2f;
        volume -= 1;

        return -volume; //Inverted controls
    }

    @Override
    public void Reset(){}
}
