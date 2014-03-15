package dk.aau.cs.giraf.cars.game;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.framework.Graphics;

public class GameScreen extends Screen {
    private Car car;

    public GameScreen(Game game) {
        super(game);
        this.car = new Car(0, 0, 200, 99);
        this.car.x = 50;
        this.car.y = (game.getGraphics().getHeight() - car.height) / 2f;
    }

    @Override
    public void update(float deltaTime) {
        car.Update(deltaTime);
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
