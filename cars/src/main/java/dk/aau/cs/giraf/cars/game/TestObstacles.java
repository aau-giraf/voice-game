package dk.aau.cs.giraf.cars.game;

public class TestObstacles implements ObstacleGenerator {
    @Override
    public Obstacle[] CreateObstacles(int width, int height) {
        return new Obstacle[]{
                //new Obstacle(100, 100, 100, 100),
                //new Obstacle(500, 300, 100, 100)
        };
    }
}
