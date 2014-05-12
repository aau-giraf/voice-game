package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.ObstacleGenerator;

public class RunningScreen extends GameScreen {
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
    }
}
