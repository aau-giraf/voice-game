package dk.aau.cs.giraf.cars.game.CarsGames;

import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.Assets;

public abstract class CarsActivity extends GameActivity {
    public abstract Screen getFirstScreen();

    @Override
    public final Screen getInitScreen() {
        return new LoadingScreen(this);
    }

    private class LoadingScreen extends Screen {
        public LoadingScreen(CarsActivity game) {
            super(game);
        }

        @Override
        public void update(Input.TouchEvent[] touchEvents, float deltaTime) {

        }

        @Override
        public void paint(Graphics graphics, float deltaTime) {
            Assets.LoadAssets(graphics, game.getAudio());
            game.setScreen(((CarsActivity)game).getFirstScreen());
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

}
