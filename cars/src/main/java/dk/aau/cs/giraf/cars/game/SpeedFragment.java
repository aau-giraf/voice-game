package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.Screen;

public class SpeedFragment extends CarsFragment {
    @Override
    public Screen getFirstScreen() {
        return new Screen(this, 10);
    }

    private class Screen extends SettingsScreen {
        public Screen(Game game, int grassSize) {
            super(game, grassSize);
        }

        @Override
        public void paint(Graphics graphics, float deltaTime) {
            super.paint(graphics, deltaTime);
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
