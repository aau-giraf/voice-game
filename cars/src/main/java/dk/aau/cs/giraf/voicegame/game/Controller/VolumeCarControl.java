package dk.aau.cs.giraf.voicegame.game.Controller;

import android.media.MediaRecorder;

import java.io.IOException;

import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.voicegame.Interfaces.CarControl;
import dk.aau.cs.giraf.voicegame.game.Enums.SoundMode;

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

    /**
     * Calculates the movement up and down based on sound volumen
     * <p>
     * Calculates the movement based on volumen and the state of the SoundMode.
     * And returns the movement that should be taken.
     * <p>
     * @param  touchEvents legacy param.
     * @param  soundMode states if car should move up or down with high sound.
     * @return float, that describes how much up and down the car should move.
     */
    @Override
    public float getMove(Input.TouchEvent[] touchEvents, SoundMode soundMode) {
        float volume = this.getAmplitude();
        //Volume is bound by min and max amplitude
        volume = Math.max(Math.min(volume, maxAmplitude), minAmplitude);

        // calculate move on wether the car reacts to noise or silence.
        float move;

        if(soundMode == SoundMode.highUp) {
            move = (volume - minAmplitude) / (maxAmplitude - minAmplitude);
        } else {
            move = ( maxAmplitude - volume) / (maxAmplitude - minAmplitude);
        }

        return move;
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
