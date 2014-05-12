package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import dk.aau.cs.giraf.cars.DatabaseHelper;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.Screen;

public abstract class GameScreen extends Screen {
    private final int grassSize = 70;

    private ObstacleCollection obstacles;
    private Car car;
    private CarGame carGame;

    public GameScreen(CarGame carGame, Car car, ObstacleCollection obstacles) {
        super(carGame);
        this.carGame = carGame;

        this.car = car;
        this.obstacles = obstacles;
    }

    protected void resetObstacles() {
        this.obstacles.resetObstacles();
    }

    protected Obstacle findCollision() {
        return obstacles.findCollision(car);
    }

    protected void moveCarTo(float y){
        y *= (game.getHeight() - grassSize * 2 - car.height);
        y += grassSize;

        car.setVerticalTarget(y);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        obstacles.Update(touchEvents, deltaTime);
        car.Update(touchEvents, deltaTime);
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.fillImageTexture(Assets.GetGrass(), 0, 0, game.getWidth(), game.getHeight());
        graphics.fillImageTexture(Assets.GetTarmac(), 0, grassSize, game.getWidth(), game.getHeight() - grassSize * 2);

        for (int i = 0; i < game.getWidth(); i += 10) {
            graphics.drawImage(Assets.getBorder(), i, grassSize - 19, 0, 0, 10, 25);
            graphics.drawImage(Assets.getBorder(), i, game.getHeight() - grassSize - 6, 0, 25, 10, 25);
        }

        obstacles.Draw(graphics, deltaTime);
        car.Draw(graphics, deltaTime);
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

    protected void showStartScreen() {
        carGame.showStartScreen();
    }

    protected void showCrashScreen(GameItem gameItem) {
        carGame.showCrashScreen(gameItem);
    }

    protected void showPauseScreen() {
        carGame.showPauseScreen();
    }

    protected void showWinningScreen() {
        carGame.showWinningScreen();
    }

    protected void showRunningScreen() {
        carGame.showRunningScreen();
    }
}
