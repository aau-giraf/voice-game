package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.ObstacleGenerator;

public class RunningScreen extends GameScreen {
    private Rect pauseButtonRec = new Rect(20, 20, 100, 100);
    private Rect pauseButtonImageRec = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());

    public RunningScreen(GameActivity game, ObstacleGenerator obstacleGenerator, GameSettings gs) {
        super(game, obstacleGenerator, gs);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);

        if (isCarInAnimaitonZone())
            graphics.drawScaledImage(Assets.GetPauseButton(), pauseButtonRec, pauseButtonImageRec);
    }
}
