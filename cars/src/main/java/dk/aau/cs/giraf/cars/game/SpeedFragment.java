package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class SpeedFragment extends CarsFragment {
    private Screen screen = null;
    private float INITIAL_SPEED = 100;

    @Override
    public Screen getFirstScreen() {
        this.screen = new Screen(this, 10);
        screen.speed = INITIAL_SPEED;

        return screen;
    }

    private class Screen extends SettingsScreen {
        float speed;
        private Paint paint;
        private final float MINIMUM_SPEED = 30;
        private final float MAXIMUM_SPEED = 1000;
        private final float SPEED_STEP = 10;
        private final int TOUCH_MARGIN = 50;

        public Screen(Game game, int grassSize) {
            super(game, grassSize);

            setCarYToCenter();
            setCarX(-getCarWidth());

            paint = new Paint();
            paint.setTextSize(100);
            paint.setTextAlign(Paint.Align.RIGHT);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
        }

        @Override
        public void paint(Graphics graphics, float deltaTime) {
            super.paint(graphics, deltaTime);
            graphics.drawString(String.valueOf(speed), graphics.getWidth(), graphics.getHeight(), paint);
        }

        @Override
        public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
            for (int i = 0; i < touchEvents.length; i++) {
                Input.TouchEvent event = touchEvents[i];
                if (event.type == Input.TouchEvent.TOUCH_DRAGGED || event.type == Input.TouchEvent.TOUCH_DOWN) {
                    if (event.x < TOUCH_MARGIN) {
                        speed -= SPEED_STEP;
                        if (speed < MINIMUM_SPEED) speed = MINIMUM_SPEED;
                    }
                    if (event.x > game.getWidth() - TOUCH_MARGIN) {
                        speed += SPEED_STEP;
                        if (speed > MAXIMUM_SPEED) speed = MAXIMUM_SPEED;
                    }
                }
            }

            float x = getCarX();
            x += speed * (deltaTime / 1000.0f);
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
