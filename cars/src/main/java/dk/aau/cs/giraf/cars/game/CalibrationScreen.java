package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.method.Touch;
import dk.aau.cs.giraf.cars.framework.*;
import dk.aau.cs.giraf.cars.game.Controller.TouchCarControl;
import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.cars.game.Overlay.OverlayButton;

/**
 * Created by Stefan on 08-04-2014.
 */
public class CalibrationScreen extends SettingsScreen {

    private VolumeCarControl control;
    private final int pixelsPerSecond =200;
    private OverlayButton loud;
    private OverlayButton speak;
    private OverlayButton silence;
    private double currentvol = 0.0;
    private double currentvolindb = 0.0;
    private double highest_recorded_vol = 0.0;

    public CalibrationScreen(GameFragment game, VolumeCarControl control)
    {
        super(game);

        this.control = control;
        setCarXToCenter();
        setCarYToCenter();

        loud = new OverlayButton(20,100, Color.BLUE,Color.YELLOW,"HØJ", Paint.Align.LEFT);
        speak = new OverlayButton(20,200, Color.BLUE,Color.YELLOW,"TALE",Paint.Align.LEFT);
        silence = new OverlayButton(20,300, Color.BLUE,Color.YELLOW,"STILLE",Paint.Align.LEFT);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime)
    {
        loud.Update(touchEvents,deltaTime);
        if(loud.IsButtonPressed(touchEvents))
            control.setMaxAmplitude(control.getAmplitude());

        speak.Update(touchEvents,deltaTime);
        if(speak.IsButtonPressed(touchEvents))
            control.setNormalAmplitude(control.getAmplitude());

        silence.Update(touchEvents,deltaTime);
        if(silence.IsButtonPressed(touchEvents))
            control.setMinAmplitude(control.getAmplitude());

        float move = control.getMove(touchEvents);
        move *=pixelsPerSecond*(deltaTime/1000);
        setCarY(getCarY()+move);

    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics,deltaTime);

        loud.Draw(graphics,deltaTime);
        speak.Draw(graphics,deltaTime);
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
