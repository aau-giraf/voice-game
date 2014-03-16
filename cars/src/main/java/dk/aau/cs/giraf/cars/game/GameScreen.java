package dk.aau.cs.giraf.cars.game;

import java.util.ArrayList;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.framework.Graphics;

public class GameScreen extends Screen {
    private CarControl carControl;
    private Car car;
    private float speed; //Pixels per second

    private ArrayList<Obstacle> obstacles;

    private final int grassSize = 70;

    public GameScreen(Game game) {
        super(game);
        this.car = new Car(0, 0, 200, 99);
        this.car.x = -car.width;
        this.car.y = (game.getHeight() - car.height) / 2f;

        this.carControl = new TouchCarControl(200);
        this.speed = 70;

        this.obstacles = new ArrayList<Obstacle>();
        this.obstacles.add(new Obstacle(100, 100, 100, 100));
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

        for (Obstacle o : obstacles) {
            o.Update(deltaTime);
            if (o.CollidesWith(car))
                obstacles.remove(o);
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics graphics = game.getGraphics();
        graphics.fillImageTexture(Assets.GetGrass(), 0, 0, game.getWidth(), game.getHeight());
        graphics.fillImageTexture(Assets.GetTarmac(), 0, grassSize, game.getWidth(), game.getHeight() - grassSize * 2);

        car.Paint(graphics, deltaTime);

        for (Obstacle o : obstacles)
            o.Paint(graphics, deltaTime);
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
