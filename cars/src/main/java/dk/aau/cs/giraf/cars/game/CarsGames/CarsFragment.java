package dk.aau.cs.giraf.cars.game.CarsGames;

import dk.aau.cs.giraf.game_framework.GameFragment;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.game_framework.Screen;
import dk.aau.cs.giraf.cars.game.Assets;

public abstract class CarsFragment extends GameFragment {

    public abstract Screen getFirstScreen();

    @Override
    public final Screen getInitScreen() {
        return new LoadingScreen(this);
    }

    private class LoadingScreen extends Screen {
        public LoadingScreen(CarsFragment game) {
            super(game);
        }

        @Override
        public void update(Input.TouchEvent[] touchEvents, float deltaTime) {

        }

        @Override
        public void paint(Graphics graphics, float deltaTime) {
            Assets.LoadAssets(graphics, game.getAudio());
            game.setScreen(((CarsFragment)game).getFirstScreen());
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
