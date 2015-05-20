package dk.aau.cs.giraf.voicegame.game.Controller;

import android.media.MediaRecorder;

import java.io.IOException;

import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.voicegame.Interfaces.CarControl;

public class VolumeCarControl implements CarControl {
    private MediaRecorder mediaRecorder;

    public void setMinAmplitude(float minAmplitude) {
        this.minAmplitude = minAmplitude;
    }

    public void setMaxAmplitude(float maxAmplitude) {
        this.maxAmplitude = maxAmplitude;
    }

    public float GetMinAmplitude() {
        return minAmplitude;
    }

    public float GetMaxAmplitude() {
        return maxAmplitude;
    }

    private float minAmplitude;
    private float maxAmplitude;

    private boolean running;

    public VolumeCarControl(float minAmplitude, float maxAmplitude) {
        this.minAmplitude = minAmplitude;
        this.maxAmplitude = maxAmplitude;

        this.Start();
    }

    @Override
    public float getMove(Input.TouchEvent[] touchEvents) {
        float volume = this.getAmplitude();
        //Volume is bound by min and max amplitude
        volume = Math.max(Math.min(volume, maxAmplitude), minAmplitude);
        return (volume - minAmplitude) / (maxAmplitude - minAmplitude);
    }

    public float getAmplitude() {
        return running && mediaRecorder != null ? mediaRecorder.getMaxAmplitude() : 0.0f;
    }

    @Override
    public int getBarometerNumber(float y, float height) {
        return 0;
    }

    @Override
    public void Reset() {
        //Does nothing for this particular controller
    }

    public void Stop() {
        running = false;
        mediaRecorder.stop();
        mediaRecorder.release();
    }

    public void Start() {
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
        running = true;
    }
}
