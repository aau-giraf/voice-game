package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Screen;

public class LoadingScreen extends Screen {
    GameSettings gamesettings;

    public LoadingScreen(Game game, GameSettings gs) {
        super(game);
        gamesettings = gs;
    }

    @Override
    public void update(float deltaTime) {
        Assets.LoadAssets(game.getGraphics());
        game.setScreen(new GameScreen(game, new TestObstacles(),gamesettings));
    }

    @Override
    public void paint(float deltaTime) {

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
