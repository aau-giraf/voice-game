package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.*;

/**
 * Created by Stefan on 08-04-2014.
 */
public class CalibrationScreen extends Screen {
    private Car car;

    public CalibrationScreen(GameFragment game)
    {
        super(game);
        this.car = new Car(0, 0, 200, 99);
        this.car.x = -car.width;
        this.car.y = 250 - car.height/2;//(game.getHeight() - car.height) / 2f;
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        car.x+=1;
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.drawARGB(255,0,0,0);
        car.Draw(graphics,deltaTime);
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
