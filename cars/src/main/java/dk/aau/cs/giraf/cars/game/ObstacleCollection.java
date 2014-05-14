package dk.aau.cs.giraf.cars.game;

import java.util.ArrayList;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.GameObject;

public class ObstacleCollection implements GameObject {
    private ArrayList<Obstacle> obstacles;
    private ObstacleGenerator generator;

    private static final int OBSTACLE_SIZE = 100;

    public ObstacleCollection(ObstacleGenerator generator) {
        this.generator = generator;
        this.obstacles = new ArrayList<Obstacle>();
    }

    public void resetObstacles() {
        this.obstacles.clear();
        for (Obstacle o : this.generator.CreateObstacles(OBSTACLE_SIZE, OBSTACLE_SIZE))
            this.obstacles.add(o);
    }

    public Obstacle findCollision(GameItem gameItem) {
        for (Obstacle o : obstacles)
            if (o.CollidesWith(gameItem))
                return o;
        return null;
    }

    public void removeObstacle(Obstacle obstacle){
        obstacles.remove(obstacle);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        for (Obstacle o : obstacles)
            o.Draw(graphics, deltaTime);
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        for (Obstacle o : obstacles)
            o.Update(touchEvents, deltaTime);
    }
}
