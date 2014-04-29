package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.cars.framework.*;
import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.cars.game.Overlay.OverlayButton;

public class CalibrationScreen extends Screen {
    public VolumeCarControl control;
    private OverlayButton loud;
    private OverlayButton silence;
    private double currentvol = 0.0;

    private double highest_recorded_vol = 0.0;
    private ArrayList<Float> volumes = new ArrayList<Float>();

    public CalibrationScreen(GameFragment game, VolumeCarControl control) {
        super(game);

        this.control = control;

        loud = new OverlayButton(20, 100, Color.BLUE, Color.YELLOW, "HØJ", Paint.Align.LEFT);
        silence = new OverlayButton(20, 260, Color.BLUE, Color.YELLOW, "LAV", Paint.Align.LEFT);

        if (control.GetMaxAmplitude() > highest_recorded_vol)
            highest_recorded_vol = control.GetMaxAmplitude();
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        loud.Update(touchEvents, deltaTime);
        silence.Update(touchEvents, deltaTime);

        if (loud.IsButtonPressed(touchEvents)) {
            float avg = getAverageValueOfList(volumes);
            control.setMaxAmplitude(avg);
            volumes.clear();
            return;
        } else if (silence.IsButtonPressed(touchEvents)) {
            float avg = getAverageValueOfList(volumes);
            control.setMinAmplitude(avg);
            volumes.clear();
            return;
        }
        float vol = control.getAmplitude();
        if (vol > 0.0)
            volumes.add(vol);
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.drawARGB(255, 0, 0, 0);
        loud.Draw(graphics, deltaTime);
        silence.Draw(graphics, deltaTime);

        short volume = (short) control.getAmplitude();

        if (volume > 0) {
            if (volume > highest_recorded_vol)
                highest_recorded_vol = volume;
            currentvol = volume;
        }

        double percentVol = currentvol / highest_recorded_vol;
        double percentMax = control.GetMaxAmplitude() / highest_recorded_vol;
        double percentMin = control.GetMinAmplitude() / highest_recorded_vol;

        graphics.drawRect(game.getWidth() - 120, game.getHeight() - (int) (percentVol * (double) game.getHeight()), 20, (int) (percentVol * (double) game.getHeight()), Color.RED);
        currentvol -= 0.01 * Short.MAX_VALUE;

        graphics.drawRect(game.getWidth() - 30, game.getHeight() - (int) (percentMax * (double) game.getHeight()), 20, (int) (percentMax * (double) game.getHeight()), Color.YELLOW);
        graphics.drawRect(game.getWidth() - 60, game.getHeight() - (int) (percentMin * (double) game.getHeight()), 20, (int) (percentMin * (double) game.getHeight()), Color.RED);
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
