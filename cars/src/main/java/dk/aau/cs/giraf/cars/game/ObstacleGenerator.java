package dk.aau.cs.giraf.cars.game;

public interface ObstacleGenerator {
    public Obstacle[] CreateObstacles(int width, int height);
}
