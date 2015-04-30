package dk.aau.cs.giraf.cars.Settings;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.cars.game.GameScreens.OverlayButton;
import dk.aau.cs.giraf.game_framework.*;

import dk.aau.cs.giraf.gui.GComponent;

public class CalibrationScreen extends Screen {
    public VolumeCarControl control;
    private OverlayButton loud;
    private OverlayButton silence;
    private double currentvol = 0.0;

    private double highest_recorded_vol = 0.0;
    private ArrayList<Float> volumes = new ArrayList<Float>();

    private int readstate;
    private final int NOT_READING = 0;
    private final int READING_LOW = 1;
    private final int READING_HIGH = 2;

    public CalibrationScreen(GameFragment game, VolumeCarControl control) {
        super(game);

        this.control = control;
        this.readstate = NOT_READING;

        loud = new OverlayButton(30, 100, Color.RED, "HØJ", Paint.Align.LEFT, 80);
        silence = new OverlayButton(30, 245, Color.GREEN, "LAV", Paint.Align.LEFT, 80);

        if (control.GetMaxAmplitude() > highest_recorded_vol)
            highest_recorded_vol = control.GetMaxAmplitude();
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        loud.Update(touchEvents, deltaTime);
        silence.Update(touchEvents, deltaTime);

        int newstate = loud.IsPressed() ? READING_HIGH : silence.IsPressed() ? READING_LOW : NOT_READING;
        if(newstate == NOT_READING && readstate != NOT_READING){
            //Store average
            float avg = getAverageValueOfList(volumes);
            volumes.clear();
            if(readstate == READING_HIGH)
                control.setMaxAmplitude(avg);
            else
                control.setMinAmplitude(avg);
        }
        else if(newstate != NOT_READING && newstate == readstate){
            float vol = control.getAmplitude();
            if (vol > 0.0)
                volumes.add(vol);
        }
        readstate = newstate;
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.drawColor(GComponent.GetBackgroundColor());
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

        graphics.drawRect(game.getWidth() - 120, game.getHeight() - (int) (percentVol * (double) game.getHeight()), 20, (int) (percentVol * (double) game.getHeight()), Color.BLACK);
        currentvol -= 0.01 * Short.MAX_VALUE;

        graphics.drawRect(game.getWidth() - 30, game.getHeight() - (int) (percentMax * (double) game.getHeight()), 20, (int) (percentMax * (double) game.getHeight()), Color.RED);
        graphics.drawRect(game.getWidth() - 60, game.getHeight() - (int) (percentMin * (double) game.getHeight()), 20, (int) (percentMin * (double) game.getHeight()), Color.GREEN);
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
