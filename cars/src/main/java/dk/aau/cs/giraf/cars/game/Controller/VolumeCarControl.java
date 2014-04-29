package dk.aau.cs.giraf.cars.game.Controller;

import android.media.MediaRecorder;

import java.io.IOException;

import android.util.Log;

import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

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

    public VolumeCarControl(float minAmplitude, float maxAmplitude) {
        this.minAmplitude = minAmplitude;
        this.maxAmplitude = maxAmplitude;

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
    public float getMove(Input.TouchEvent[] touchEvents) {
        float volume = (float) mediaRecorder.getMaxAmplitude();
        //Volume is bound by min and max amplitude
        volume = Math.max(Math.min(volume, maxAmplitude), minAmplitude);
        return (volume - minAmplitude) / (maxAmplitude - minAmplitude);
    }

    public float getAmplitude() {
        return mediaRecorder.getMaxAmplitude();
    }

    @Override
    public int getBarometerNumber(float y, float height) {
        return 0;
    }

    @Override
    public void Reset() {
    }

    public void Stop() {
        mediaRecorder.stop();
        mediaRecorder.release();
    }
}
