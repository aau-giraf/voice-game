package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsFragment;

public class SpeedFragment extends CarsFragment {
    private Screen screen = null;
    private float INITIAL_SPEED = 2.0f;

    @Override
    public Screen getFirstScreen() {
        this.screen = new Screen(this, 10);
        screen.speed = INITIAL_SPEED;

        return screen;
    }

    public void setSpeed(float speed) {

        INITIAL_SPEED = speed;
        if (screen != null) {
            screen.speed = speed;
        }
    }

    public float getSpeed() {
        return screen != null ? screen.speed : INITIAL_SPEED;
    }

    private class Screen extends SettingsScreen {
        float speed;
        private final float MAX_SPEED = 10.0f;
        private final int MAX_SPEED_PPS = 1000;
        private final int GAUGE_MARGIN = 10;
        private final int GAUGE_HEIGHT = 70;
        private SpeedGauge gauge;

        public Screen(Game game, int grassSize) {
            super(game, grassSize, game.getWidth(), 2 * grassSize + Assets.GetCar().getHeight());
            int gameHeight = 2 * grassSize + Assets.GetCar().getHeight();
            gauge = new SpeedGauge(GAUGE_MARGIN, gameHeight + GAUGE_MARGIN, game.getWidth() - 2 * GAUGE_MARGIN, GAUGE_HEIGHT);
            gauge.SetSpeed(INITIAL_SPEED);

            setCarYToCenter();
            setCarX(-getCarWidth());
        }

        @Override
        public void paint(Graphics graphics, float deltaTime) {
            super.paint(graphics, deltaTime);
            gauge.Draw(graphics, deltaTime);
        }

        @Override
        public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
            gauge.Update(touchEvents, deltaTime);

            speed = gauge.GetSpeed();
            int speedInPPS = (int)((gauge.GetSpeed() / MAX_SPEED) * MAX_SPEED_PPS);
            float x = getCarX();
            x += speedInPPS * (deltaTime / 1000.0f);
            if (x > game.getWidth())
                x = -getCarWidth();
            setCarX(x);
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
