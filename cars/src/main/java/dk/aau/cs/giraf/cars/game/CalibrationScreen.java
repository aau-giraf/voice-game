package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
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

    public CalibrationScreen(GameFragment game, VolumeCarControl control)
    {
        super(game);

        this.control = control;
        setCarXToCenter();
        setCarYToCenter();

        loud = new OverlayButton(20,100, Color.BLUE,Color.YELLOW,"HØJ");
        speak = new OverlayButton(20,200, Color.BLUE,Color.YELLOW,"TALE");
        silence = new OverlayButton(20,300, Color.BLUE,Color.YELLOW,"STILLE");
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
