package dk.aau.cs.giraf.cars.game;

public class TestObstacles implements ObstacleGenerator {
    private final int TESTSCENARIO = 2;

    @Override
    public Obstacle[] CreateObstacles(int width, int height) {

        switch (TESTSCENARIO) {
            case 0:// top + mid
                return new Obstacle[]{
                        new Obstacle((float) 0.2 * width, (float) 0.15 * height, 100, 100),
                        new Obstacle((float) 0.5 * width, (float) 0.45 * height, 100, 100)
                };
            case 1: // top + bottom
                return new Obstacle[]{
                        new Obstacle((float) 0.5 * width, (float) 0.15 * height, 100, 100),
                        new Obstacle((float) 0.2 * width, (float) 0.7 * height, 100, 100)
                };
            case 2: // mid + bottom
                return new Obstacle[]{
                        new Obstacle((float) 0.2 * width, (float) 0.7 * height, 100, 100),
                        new Obstacle((float) 0.5 * width, (float) 0.45 * height, 100, 100)
                };
        }
        return null;
    }
}
