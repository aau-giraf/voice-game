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
        private final float MAX_SPEED = Car.MAX_SCALE;
        private final float MAX_SPEED_PPS = Car.MAX_PIXELSPERSECOND;
        private final int GAUGE_MARGIN = 10;
        private final int GAUGE_HEIGHT = 70;
        private SpeedGauge gauge;
        private PictoButton snailPicto, rabbitPicto, tigerPicto;

        public Screen(Game game, int grassSize) {
            super(game, grassSize, game.getWidth(), 2 * grassSize + Assets.GetCar().getHeight());
            int gameHeight = 2 * grassSize + Assets.GetCar().getHeight();
            int gaugeY = gameHeight + GAUGE_MARGIN;
            gauge = new SpeedGauge(GAUGE_MARGIN, gaugeY, game.getWidth() - 2 * GAUGE_MARGIN, GAUGE_HEIGHT);
            gauge.SetSpeed(INITIAL_SPEED);

            int pictoSize = GAUGE_HEIGHT;
            int pictoY = gaugeY + GAUGE_HEIGHT + GAUGE_MARGIN;
            snailPicto = new PictoButton(gauge.GetPictoPos(1) - pictoSize/2, pictoY, pictoSize, pictoSize, Assets.GetSnailPicto());
            rabbitPicto = new PictoButton(gauge.GetPictoPos(5) - pictoSize/2, pictoY, pictoSize, pictoSize, Assets.GetRabbitPicto());
            tigerPicto = new PictoButton(gauge.GetPictoPos(9) - pictoSize/2, pictoY, pictoSize, pictoSize, Assets.GetTigerPicto());

            setCarYToCenter();
            setCarX(-getCarWidth());
        }

        @Override
        public void paint(Graphics graphics, float deltaTime) {
            super.paint(graphics, deltaTime);
            gauge.Draw(graphics, deltaTime);

            snailPicto.Draw(graphics, deltaTime);
            rabbitPicto.Draw(graphics, deltaTime);
            tigerPicto.Draw(graphics, deltaTime);
        }

        @Override
        public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
            gauge.Update(touchEvents, deltaTime);

            if (snailPicto.IsPressed(touchEvents, deltaTime))
                gauge.SetSpeed(1.0f);
            if (rabbitPicto.IsPressed(touchEvents, deltaTime))
                gauge.SetSpeed(5.0f);
            if (tigerPicto.IsPressed(touchEvents, deltaTime))
                gauge.SetSpeed(9.0f);

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
