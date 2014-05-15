package dk.aau.cs.giraf.cars.game;

import java.util.ArrayList;

public class PreferencesObstacles implements RoadItemGenerator {
    private GameSettings gs;

    public PreferencesObstacles(GameSettings gs) {
        this.gs = gs;
    }

    @Override
    public Obstacle[] CreateRoadItems(int width, int height) {

        ArrayList<Obstacle> obstacleList =  gs.LoadObstacles();
        Obstacle[] obstacles = new Obstacle[obstacleList.size()];

        return obstacleList.toArray(obstacles);

    }
}
