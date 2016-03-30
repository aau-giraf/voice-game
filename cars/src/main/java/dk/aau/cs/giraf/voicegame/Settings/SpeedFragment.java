package dk.aau.cs.giraf.voicegame.Settings;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.voicegame.CarsGames.CarsFragment;
import dk.aau.cs.giraf.voicegame.R;
import dk.aau.cs.giraf.voicegame.SettingsActivity;
import dk.aau.cs.giraf.voicegame.game.Car;
import dk.aau.cs.giraf.game_framework.Game;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;


public class SpeedFragment extends CarsFragment {
    private Screen screen = null;
    private float initialSpeed = 2.0f;
    private int initialColor = 0;

    @Override
    public Screen getFirstScreen() {
        this.screen = new Screen(this, 10);
        screen.speed = initialSpeed;
        screen.newColor = screen.oldColor = initialColor;

        return screen;
    }

    public void setSpeed(float speed) {
        initialSpeed = speed;
        if (screen != null)
            screen.speed = speed;
    }

    public void setCarColor(int color) {
        initialColor = color;
        if (screen != null)
            screen.newColor = color;
    }

    public float getSpeed() {
        return screen != null ? screen.speed : initialSpeed;
    }

    private class Screen extends SettingsScreen {
        float speed;
        int oldColor, newColor;
        private final float MAX_SPEED = Car.MAX_SCALE;
        private final float MAX_SPEED_PPS = Car.MAX_PIXELSPERSECOND;
        private final int GAUGE_MARGIN = 10;
        private final int GAUGE_HEIGHT = 70;
        private final int PICTO_SIZE = 120;
        private SpeedGauge gauge;
        private PictoButton snailPicto, rabbitPicto, tigerPicto;

        public Screen(Game game, int grassSize) {
            super(game, grassSize, game.getWidth(), 2 * grassSize + Assets.GetCar().getHeight());
            int gameHeight = 2 * grassSize + Assets.GetCar().getHeight();
            int gaugeY = gameHeight + GAUGE_MARGIN;
            gauge = new SpeedGauge(GAUGE_MARGIN, gaugeY, game.getWidth() - 2 * GAUGE_MARGIN, GAUGE_HEIGHT);
            gauge.SetSpeed(initialSpeed);

            int pictoY = gaugeY + GAUGE_HEIGHT + GAUGE_MARGIN;
            snailPicto = new PictoButton(gauge.GetValueX(1) - PICTO_SIZE/2, pictoY, PICTO_SIZE, PICTO_SIZE, Assets.GetSnailPicto());
            rabbitPicto = new PictoButton(gauge.GetValueX(5) - PICTO_SIZE/2, pictoY, PICTO_SIZE, PICTO_SIZE, Assets.GetRabbitPicto());
            tigerPicto = new PictoButton(gauge.GetValueX(9) - PICTO_SIZE/2, pictoY, PICTO_SIZE, PICTO_SIZE, Assets.GetTigerPicto());

            setCarYToCenter();
            setCarX(-getCarWidth());

            this.setCarColor(initialColor);
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
            //RelativeLayout test = (RelativeLayout)((SettingsActivity) getActivity()).findViewById(R.id.settingsChange);
           // int settingsChanged = test.getVisibility();

            if (newColor != oldColor) {
                this.setCarColor(newColor);
                newColor = oldColor;
                /* if (settingsChanged == View.INVISIBLE){
                    test.setVisibility(View.VISIBLE);
                }*/
            }

            if (snailPicto.IsPressed(touchEvents, deltaTime))
                gauge.SetSpeed(1.0f);
            if (rabbitPicto.IsPressed(touchEvents, deltaTime))
                gauge.SetSpeed(5.0f);
            if (tigerPicto.IsPressed(touchEvents, deltaTime))
                gauge.SetSpeed(9.0f);

            /*if(speed != gauge.GetSpeed()){
                if(settingsChanged == View.INVISIBLE){
                    test.setVisibility(View.VISIBLE);
                }
            }*/
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
