package dk.aau.cs.giraf.cars.game;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.framework.Graphics;

public class GameScreen extends Screen {
    private CarControl carControl;
    private Car car;
    private float speed; //Pixels per second

    public GameScreen(Game game) {
        super(game);
        this.car = new Car(0, 0, 200, 99);
        this.car.x = -car.width;
        this.car.y = (game.getHeight() - car.height) / 2f;

        this.speed = 70;

        this.carControl = new TouchCarControl(200);
    }

    @Override
    public void update(float deltaTime) {
        car.Update(deltaTime);
        car.x += speed * (deltaTime / 100.0f);
        if (car.x > game.getWidth())
            car.x = -car.width;
        car.y += carControl.getMove(game, deltaTime);
        if (car.y < 50) car.y = 50;
        if (car.y > game.getHeight() - car.height - 50) car.y = game.getHeight() - car.height - 50;
    }

    @Override
    public void paint(float deltaTime) {
        Graphics graphics = game.getGraphics();
        graphics.drawARGB(255, 255, 255, 255);
        car.Paint(graphics, deltaTime);
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
