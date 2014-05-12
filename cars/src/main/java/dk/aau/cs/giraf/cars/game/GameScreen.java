package dk.aau.cs.giraf.cars.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.Controller.TouchCarControl;
import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.Overlay.CrashOverlay;

public abstract class GameScreen extends Screen {
    private Car car;
    private final int grassSize = 70;
    private CarControl carControl;
    private float speed; //Pixels per second

    private ArrayList<Obstacle> obstacles;

    private GameSettings gameSettings;
    private ObstacleGenerator obstacleGenerator;
    private CarGame gameActivity;

    public GameScreen(CarGame game, ObstacleGenerator obstacleGenerator, GameSettings gs) {
        super(game);
        this.car = new Car(0, 0, 200, 99);
        this.gameActivity = game;
        this.obstacleGenerator = obstacleGenerator;
        this.gameSettings = gs;
        this.car.setShowValue(true);
        car.ResetCar();
        this.speed = gs.GetSpeed() * (Car.MAX_PIXELSPERSECOND / Car.MAX_SCALE);

        this.carControl = new TouchCarControl(game.getHeight() - 2 * grassSize - (int) car.getHeight(), grassSize + (int) car.getHeight() / 2);
        //this.carControl = new VolumeCarControl(gs.GetMinVolume(), gs.GetMaxVolume());

        car.setColor(gs.GetColors().getFirst());

        this.obstacles = new ArrayList<Obstacle>();

        for (Obstacle o : obstacleGenerator.CreateObstacles(game.getWidth(), game.getHeight()))
            this.obstacles.add(o);
    }

    public Car getCar() {
        return car;
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        car.Update(touchEvents, deltaTime);
        car.x += speed * (deltaTime / 1000.0f);

        float moveTo = 1f - carControl.getMove(touchEvents);
        moveTo *= (game.getHeight() - grassSize * 2 - car.height);
        moveTo += grassSize;

        car.setVerticalTarget(moveTo);

        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).Update(touchEvents, deltaTime);
            if (obstacles.get(i).CollidesWith(car)) {
                showCrashScreen(obstacles.get(i));
                return;
            }
        }
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.fillImageTexture(Assets.GetGrass(), 0, 0, game.getWidth(), game.getHeight());
        graphics.fillImageTexture(Assets.GetTarmac(), 0, grassSize, game.getWidth(), game.getHeight() - grassSize * 2);

        for (int i = 0; i < game.getWidth(); i += 10) {
            graphics.drawImage(Assets.getBorder(), i, grassSize - 19, 0, 0, 10, 25);
            graphics.drawImage(Assets.getBorder(), i, game.getHeight() - grassSize - 6, 0, 25, 10, 25);
        }

        for (Obstacle obstacle : obstacles)
            obstacle.Draw(graphics, deltaTime);

        //if (driving)
        Log.d("car", car + "");
        car.Draw(graphics, deltaTime);
    }

    public void resetRound() {
        car.ResetCar();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        if (carControl instanceof VolumeCarControl)
            ((VolumeCarControl) carControl).Stop();
    }

    @Override
    public void backButton() {

    }

    public void showStartScreen() {
        gameActivity.showStartScreen();
    }

    public void showCrashScreen(GameItem gameItem) {
        gameActivity.showCrashScreen(gameItem);
    }

    public void showPauseScreen() {
        gameActivity.showPauseScreen();
    }

    public void showWinningScreen() {
        gameActivity.showWinningScreen();
    }

    public void showRunningScreen() {
        gameActivity.showRunningScreen();
    }
}
