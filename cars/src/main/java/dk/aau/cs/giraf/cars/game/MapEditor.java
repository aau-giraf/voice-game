package dk.aau.cs.giraf.cars.game;

import java.util.ArrayList;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsActivity;

public class MapEditor extends CarsActivity {
    @Override
    public Screen getFirstScreen() {
        return new MapScreen(this);
    }

    private class MapScreen extends SettingsScreen {
        private final int OBSTACLE_SIZE = 100;

        private ArrayList<Obstacle> obstacles;

        public MapScreen(Game game) {
            super(game);
            setCarX(-getCarWidth());

            obstacles = new ArrayList<Obstacle>();
        }

        @Override
        public void paint(Graphics graphics, float deltaTime) {
            super.paint(graphics, deltaTime);
            for (Obstacle o : obstacles)
                o.Draw(graphics, deltaTime);
        }

        @Override
        public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
            for (Input.TouchEvent e : touchEvents) {
                if (e.type == Input.TouchEvent.TOUCH_UP) {
                    Obstacle rem = null;
                    for (Obstacle o : obstacles)
                        if (o.GetBounds().contains(e.x, e.y)) {
                            rem = o;
                            break;
                        }

                    if(rem == null)
                        Add(e.x - OBSTACLE_SIZE / 2, e.y - OBSTACLE_SIZE / 2);
                    else
                        Remove(rem);
                }
            }
        }

        private void Add(float x, float y){
            obstacles.add(new Obstacle(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE));
        }
        private void Remove(Obstacle obstacle){
            obstacles.remove(obstacle);
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
