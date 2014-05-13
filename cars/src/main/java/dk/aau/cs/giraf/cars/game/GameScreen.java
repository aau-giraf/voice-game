package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.Interfaces.Drawable;
import dk.aau.cs.giraf.cars.game.Interfaces.GameObject;
import dk.aau.cs.giraf.cars.game.Interfaces.Updatable;

public abstract class GameScreen extends Screen {
    private final int grassSize = 70;
    private final int finishLineScale = 15;
    private int finishLineX;

    private ObstacleCollection obstacles;
    private Car car;
    private CarGame carGame;

    private ArrayList<Drawable> drawables;
    private ArrayList<Updatable> updatables;

    public GameScreen(CarGame carGame, Car car, ObstacleCollection obstacles) {
        super(carGame);
        this.carGame = carGame;

        this.car = car;
        this.obstacles = obstacles;

        drawables = new ArrayList<Drawable>();
        updatables = new ArrayList<Updatable>();

        this.finishLineX = game.getWidth()-100;
    }

    protected void Add(Drawable d) {
        drawables.add(d);
    }

    protected void Add(Updatable u) {
        updatables.add(u);
    }

    protected void Add(GameObject g) {
        drawables.add(g);
        updatables.add(g);
    }

    protected void resetObstacles() {
        this.obstacles.resetObstacles();
    }

    protected Obstacle getCollisionObstacle() {
        return obstacles.findCollision(car);
    }

    protected Point getCollisionPoint(GameItem obstacle) {
        return obstacle.GetCollisionCenter(car);
    }

    protected void moveCarTo(float y) {
        y *= (game.getHeight() - grassSize * 2 - car.height);
        y += grassSize;

        car.setVerticalTarget(y);
    }

    protected void freezeCar(){
        car.setVerticalPosition(car.getY());
    }

    protected void setCarSpeed(float speed) {
        car.setSpeed(speed);
    }

    protected void resetCar() {
        car.reset();
    }

    protected float getCarLocation() {
        return car.x;
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        obstacles.Update(touchEvents, deltaTime);
        car.Update(touchEvents, deltaTime);

        for (Updatable updatable : updatables)
            updatable.Update(touchEvents, deltaTime);
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.fillImageTexture(Assets.GetGrass(), 0, 0, game.getWidth(), game.getHeight());
        graphics.fillImageTexture(Assets.GetTarmac(), 0, grassSize, game.getWidth(), game.getHeight() - grassSize * 2);

        drawFinishLine(graphics,deltaTime);

        for (int i = 0; i < game.getWidth(); i += 10) {
            graphics.drawImage(Assets.getBorder(), i, grassSize - 19, 0, 0, 10, 25);
            graphics.drawImage(Assets.getBorder(), i, game.getHeight() - grassSize - 6, 0, 25, 10, 25);
        }

        obstacles.Draw(graphics, deltaTime);
        car.Draw(graphics, deltaTime);

        for (Drawable drawable : drawables)
            drawable.Draw(graphics, deltaTime);
    }

    private void drawFinishLine(Graphics graphics, float deltaTime)
    {
        int height = game.getHeight()-2*grassSize, squareHeight = height/finishLineScale, squareWidth = 40, width = 80;
        graphics.drawRect(finishLineX,grassSize,width,height, Color.WHITE);
        for (int i=squareHeight;i<height;i+=2*squareHeight)
            graphics.drawRect(finishLineX,grassSize+i,41,squareHeight+1,Color.BLACK);
        for (int i=0;i<height;i+=2*squareHeight)
            graphics.drawRect(finishLineX+squareWidth,grassSize+i,41,squareHeight+1,Color.BLACK);
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
