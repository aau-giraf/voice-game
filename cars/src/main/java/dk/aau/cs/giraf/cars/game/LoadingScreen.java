package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.Screen;

public class LoadingScreen extends Screen {
    GameSettings gamesettings;

    public LoadingScreen(GameActivity game, GameSettings gs) {
        super(game);
        gamesettings = gs;
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        Assets.LoadAssets(graphics);
        game.setScreen(new GameScreen((GameActivity) game, new TestObstacles(), gamesettings));
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}
