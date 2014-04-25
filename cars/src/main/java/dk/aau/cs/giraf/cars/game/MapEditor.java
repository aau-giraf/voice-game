package dk.aau.cs.giraf.cars.game;

import android.content.SharedPreferences;

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
        private final int grassSize = 70;
        private final float garageSize = 250;
        private final int amountOfGarages = 3;
        private final int OBSTACLE_SIZE = 100;
        private final float animationZoneSize = 100;
        private SharedPreferences mapPreferences;
        private float animationZoneX;

        private ArrayList<Obstacle> obstacles;
        private ArrayList<Garage> garages;

        public MapScreen(Game game) {
            super(game);
            setCarX(-getCarWidth());

            obstacles = new ArrayList<Obstacle>();
            mapPreferences = getSharedPreferences("map", 0);
            int count = mapPreferences.getInt("count", 0);
            for (int i = 0; i < count; i++) {
                float x = mapPreferences.getFloat("x" + i, 0);
                float y = mapPreferences.getFloat("y" + i, 0);
                obstacles.add(new Obstacle(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE));
            }

            this.garages = new ArrayList<Garage>();
            float garageSpace = (game.getHeight() - 2 * grassSize - 3 * garageSize) / 4f;
            for (int i = 0; i < amountOfGarages; i++) {
                Garage g = new Garage(game.getWidth() - garageSize, grassSize + (i + 1) * garageSpace + i * garageSize + garageSize / 4, garageSize, garageSize / 2);
                garages.add(g);
            }

            this.animationZoneX = garages.get(0).x - animationZoneSize;
        }

        @Override
        public void paint(Graphics graphics, float deltaTime) {
            super.paint(graphics, deltaTime);
            for (Obstacle o : obstacles)
                o.Draw(graphics, deltaTime);
            for (Garage g : garages)
                g.Draw(graphics, deltaTime);
        }

        @Override
        public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
            for (Input.TouchEvent e : touchEvents) {
                if (e.type == Input.TouchEvent.TOUCH_DOWN && e.x < animationZoneX && e.y > grassSize && e.y < game.getHeight()-grassSize) {
                    Obstacle rem = null;
                    for (Obstacle o : obstacles)
                        if (o.GetBounds().contains(e.x, e.y)) {
                            rem = o;
                            break;
                        }

                    if (rem == null)
                        Add(e.x - OBSTACLE_SIZE / 2, e.y - OBSTACLE_SIZE / 2);
                    else
                        Remove(rem);
                }
            }
        }

        private void Add(float x, float y) {
            int index = obstacles.size();
            obstacles.add(new Obstacle(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE));
            mapPreferences.edit().putFloat("x" + index, x).putFloat("y" + index, y).putInt("count", index + 1).commit();
        }

        private void Remove(Obstacle obstacle) {
            int index = obstacles.indexOf(obstacle);
            int size = obstacles.size();

            SharedPreferences.Editor editor = mapPreferences.edit();

            for (int i = index; i < size - 1; i++) {
                Obstacle o = obstacles.get(i + 1);
                editor.putFloat("x" + i, o.x).putFloat("y" + i, o.y);
            }
            editor.remove("x" + (size - 1)).remove("y" + (size - 1));
            editor.putInt("count", size - 1);
            editor.commit();
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
