package dk.aau.cs.giraf.voicegame.CarsGames;

import dk.aau.cs.giraf.game_framework.GameActivity;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.game_framework.Screen;
import dk.aau.cs.giraf.voicegame.Assets;
//import com.google.analytics.tracking.android.EasyTracker;

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
        /*
        //Google analytics - start logging
        @Override
        public void onStart() {
            super.onStart();
            EasyTracker.getInstance(this).activityStart(this);  // Start logging
        }
        //Google analytics - Stop logging
        @Override
        public void onStop() {
            super.onStop();
            EasyTracker.getInstance(this).activityStop(this);  // stop logging
        }*/

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
