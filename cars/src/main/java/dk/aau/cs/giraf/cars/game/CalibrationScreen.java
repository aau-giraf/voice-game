package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.method.Touch;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.cars.framework.*;
import dk.aau.cs.giraf.cars.game.Controller.TouchCarControl;
import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.cars.game.Overlay.OverlayButton;

/**
 * Created by Stefan on 08-04-2014.
 */
public class CalibrationScreen extends SettingsScreen {

    enum RecordingState { Loud, Silence, None }

    private VolumeCarControl control;
    private OverlayButton loud;
    private OverlayButton silence;
    private double currentvol = 0.0;
    private double currentvolindb = 0.0;
    private double highest_recorded_vol = 0.0;
    private ArrayList<Float> volumes = new ArrayList<Float>();
    private RecordingState recordingState = RecordingState.None;

    public CalibrationScreen(GameFragment game, VolumeCarControl control)
    {
        super(game);

        this.control = control;

        loud = new OverlayButton(20,100, Color.BLUE,Color.YELLOW,"HØJ", Paint.Align.LEFT);
        silence = new OverlayButton(20,300, Color.BLUE,Color.YELLOW,"STILLE",Paint.Align.LEFT);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime)
    {
        loud.Update(touchEvents, deltaTime);
        silence.Update(touchEvents, deltaTime);
        if (loud.IsButtonPressed(touchEvents)) {
            float avg = getAverageValueOfList(volumes);
            Log.d("avgmax", avg + "");
            control.setMaxAmplitude(avg);
            volumes.clear();
            recordingState = RecordingState.None;
            return;
        }
        else if (silence.IsButtonPressed(touchEvents)) {
            float avg = getAverageValueOfList(volumes);
            Log.d("avgmin", avg + "");
            control.setMinAmplitude(avg);
            volumes.clear();
            recordingState = RecordingState.None;
            return;
        }

        switch (recordingState) {
            case Loud:
            case Silence:
                float vol = control.getAmplitude();
                if (vol > 0.0)
                    volumes.add(vol);
                break;
            case None:
            default:
                break;
        }

        if (loud.IsButtonHeld(touchEvents))
            recordingState = RecordingState.Loud;
        else if (silence.IsButtonHeld(touchEvents))
            recordingState = RecordingState.Silence;
        else
            recordingState = RecordingState.None;
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics,deltaTime);

        loud.Draw(graphics,deltaTime);
        silence.Draw(graphics,deltaTime);

        short volume = (short)control.getAmplitude();

        if (volume > 0) {
            if (volume > highest_recorded_vol)
                highest_recorded_vol = volume;
            currentvol = volume;
            currentvolindb = 20 * Math.log10(volume / 2700.0);
        }
        double volmax = 20 * Math.log10(highest_recorded_vol / 2700.0);

        double percentdB = (currentvolindb + volmax) / (volmax * 2.0);
        double percentVol = currentvol / highest_recorded_vol;

        graphics.drawRect(game.getWidth()/2-25, game.getHeight() - (int)(percentVol * (double)game.getHeight()), 20, (int)(percentVol * (double)game.getHeight()), Color.RED);
        currentvol -= 0.01 * Short.MAX_VALUE;

        graphics.drawRect(game.getWidth()/2, game.getHeight() - (int)(percentdB * (double)game.getHeight()), 20, (int)(percentdB * (double)game.getHeight()), Color.YELLOW);
        currentvolindb -= 0.01 * volmax;
    }

    private float getAverageValueOfList(List<Float> list) {
        float sum = 0;

        for (float i : list)
            sum += i;

        return sum / list.size();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        control.Stop();
    }

    @Override
    public void backButton() {

    }
}
