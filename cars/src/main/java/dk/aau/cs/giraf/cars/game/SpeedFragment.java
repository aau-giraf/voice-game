package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class SpeedFragment extends CarsFragment {
    @Override
    public Screen getFirstScreen() {
        Screen screen = new Screen(this, 10);
        screen.speed = 100;

        return screen;
    }

    private class Screen extends SettingsScreen {
        float speed;
        private Paint paint;

        public Screen(Game game, int grassSize) {
            super(game, grassSize);

            setCarYToCenter();

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
