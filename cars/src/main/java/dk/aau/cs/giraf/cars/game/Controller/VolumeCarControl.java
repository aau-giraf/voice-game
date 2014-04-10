package dk.aau.cs.giraf.cars.game.Controller;

import android.media.MediaRecorder;

import java.io.IOException;

import android.util.Log;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

public class VolumeCarControl implements CarControl {
    private MediaRecorder mediaRecorder;

    private final float lower = 500f;
    private final float upper = 5000f;

    public void setMinAmplitude(float minAmplitude) {
        this.minAmplitude = minAmplitude;
    }

    public void setNormalAmplitude(float normalAmplitude) {
        this.normalAmplitude = normalAmplitude;
    }

    public void setMaxAmplitude(float maxAmplitude) {
        this.maxAmplitude = maxAmplitude;
    }

    private float minAmplitude;
    private float normalAmplitude;
    private float maxAmplitude;
    private float height;

    public VolumeCarControl(float minAmplitude, float normalAmplitude, float maxAmplitude, int height) {
        this.minAmplitude = minAmplitude;
        this.normalAmplitude = normalAmplitude;
        this.maxAmplitude = maxAmplitude;
        this.height = height;

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
        float volume = (float)mediaRecorder.getMaxAmplitude();
        Log.d("vol",volume+"v");
        if (volume < minAmplitude)
            return height;

        if (volume > maxAmplitude)
            return 0;

        float range = maxAmplitude-minAmplitude;
        float multiplier = range/ height;

        float res = (volume - minAmplitude)*multiplier;

        return res;
    }

    public float getAmplitude()
    {
        return mediaRecorder.getMaxAmplitude();
    }

    @Override
    public int getBarometerNumber(float y, float height)
    {
        return 0;
    }

    @Override
    public void Reset(){}

    public void Stop(){
        mediaRecorder.stop();
        mediaRecorder.release();
    }
}
