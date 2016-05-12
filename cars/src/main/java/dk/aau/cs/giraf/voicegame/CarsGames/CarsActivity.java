package dk.aau.cs.giraf.voicegame.CarsGames;

import dk.aau.cs.giraf.game_framework.GameActivity;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.game_framework.Screen;
import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.voicegame.R;

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
            Assets.LoadAssets(graphics, game.getAudio(), getResources().getString(R.string.sound_language));
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
