package dk.aau.cs.giraf.cars.game;

import java.util.ArrayList;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.framework.Graphics;

public class GameScreen extends Screen {
    private CarControl carControl;
    private Car car;
    private float speed; //Pixels per second

    private ObstacleGenerator obstacleGenerator;
    private ArrayList<Obstacle> obstacles;

    private Garage garage1, garage2, garage3;

    private final int grassSize = 70;
    private final float garageSize = 150;

    public GameScreen(Game game, ObstacleGenerator obstacleGenerator) {
        super(game);
        this.car = new Car(0, 0, 200, 99);
        this.car.x = -car.width;
        this.car.y = (game.getHeight() - car.height) / 2f;

        this.carControl = new TouchCarControl(200);
        this.speed = 70;

        this.obstacles = new ArrayList<Obstacle>();
        this.obstacleGenerator = obstacleGenerator;

        for (Obstacle o : obstacleGenerator.CreateObstacles(game.getWidth(), game.getHeight()))
            this.obstacles.add(o);

        float garageSpace = (game.getHeight() - 2 * grassSize - 3 * garageSize) / 4f;
        this.garage1 = new Garage(game.getWidth() - garageSize, grassSize + garageSpace, garageSize, garageSize);
        this.garage2 = new Garage(game.getWidth() - garageSize, grassSize + 2 * garageSpace + garageSize, garageSize, garageSize);
        this.garage3 = new Garage(game.getWidth() - garageSize, grassSize + 3 * garageSpace + 2 * garageSize, garageSize, garageSize);
    }

    @Override
    public void update(float deltaTime) {
        car.Update(deltaTime);
        car.x += speed * (deltaTime / 100.0f);
        if (car.x > game.getWidth())
            car.x = -car.width;

        car.y += carControl.getMove(game, deltaTime);
        if (car.y < grassSize) car.y = grassSize;
        if (car.y > game.getHeight() - car.height - grassSize)
            car.y = game.getHeight() - car.height - grassSize;

        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).Update(deltaTime);
            if (obstacles.get(i).CollidesWith(car))
                obstacles.remove(i--);
        }

        garage1.Update(deltaTime);
        garage2.Update(deltaTime);
        garage3.Update(deltaTime);
    }

    @Override
    public void paint(float deltaTime) {
        Graphics graphics = game.getGraphics();
        graphics.fillImageTexture(Assets.GetGrass(), 0, 0, game.getWidth(), game.getHeight());
        graphics.fillImageTexture(Assets.GetTarmac(), 0, grassSize, game.getWidth(), game.getHeight() - grassSize * 2);

        car.Paint(graphics, deltaTime);

        for (Obstacle o : obstacles)
            o.Paint(graphics, deltaTime);

        garage1.Paint(graphics, deltaTime);
        garage2.Paint(graphics, deltaTime);
        garage3.Paint(graphics, deltaTime);
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
