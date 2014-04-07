package dk.aau.cs.giraf.cars.game.Controller;

import android.media.MediaRecorder;

import java.io.IOException;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

public class VolumeCarControl implements CarControl {
    private MediaRecorder mediaRecorder;

    private final float lower = 500f;
    private final float upper = 5000f;

    private float minAmplitude;
    private float normalAmplitude;
    private float maxAmplitude;

    public VolumeCarControl(float minAmplitude, float normalAmplitude, float maxAmplitude) {
        this.minAmplitude = minAmplitude;
        this.normalAmplitude = normalAmplitude;
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
    public float getMove(Game game, Car car) {

        float volume = (float)mediaRecorder.getMaxAmplitude();

        if (volume < minAmplitude)
            return 1;

        if (volume > maxAmplitude)
            volume = maxAmplitude;

        volume -= minAmplitude;
        volume /= (maxAmplitude - minAmplitude) / 2f;
        volume -= 1;

        return -volume; //Inverted controls
    }

    @Override
    public int getBarometerNumber(float y, float height)
    {
        return 0;//not implemented yet
    }

    @Override
    public void Reset(){}
}
