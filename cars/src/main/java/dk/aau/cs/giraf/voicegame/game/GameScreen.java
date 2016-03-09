package dk.aau.cs.giraf.voicegame.game;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.game_framework.Screen;
import dk.aau.cs.giraf.voicegame.Interfaces.Drawable;
import dk.aau.cs.giraf.voicegame.Interfaces.GameObject;
import dk.aau.cs.giraf.voicegame.Interfaces.Updatable;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;

public abstract class GameScreen extends Screen {
    private final int grassSize = 70;
    private final int finishLineScale = 15;
    private final int finishLineWidth = 80;
    private int finishLineX;

    private GameItemCollection roadItems;
    private Car car;
    private CarGame carGame;

    private ArrayList<Drawable> drawables;
    private ArrayList<Updatable> updatables;



    public GameScreen(CarGame carGame, Car car, GameItemCollection roadItems) {
        super(carGame);
        this.carGame = carGame;

        this.car = car;
        this.roadItems = roadItems;

        drawables = new ArrayList<Drawable>();
        updatables = new ArrayList<Updatable>();

        this.finishLineX = game.getWidth() - finishLineWidth;
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
        this.roadItems.resetRoadItems();
    }

    protected RoadItem getCollisionRoadItem() {
        return roadItems.findCollision(car);
    }

    protected void removeObstacle(RoadItem roadItem) {
        roadItems.removeRoadItem(roadItem);
    }

    protected boolean isRoadItemsEmpty(){
        return roadItems.isEmpty();
    }

    protected Point getCollisionPoint(GameItem obstacle) {
        return obstacle.GetCollisionCenter(car);
    }

    protected void moveCarTo(float y) {
        y *= (game.getHeight() - grassSize * 2 - car.height);
        y += grassSize;

        car.setVerticalTarget(y);
    }

    protected void freezeCar() {
        car.setVerticalPosition(car.getY());
    }

    protected void setCarSpeed(float speed) {
        car.setSpeed(speed);
    }

    protected void resetCar() {
        car.reset();
    }

    protected float getCarLocationX() {
        return car.x;
    }

    protected float getCarWidth() {
        return car.width;
    }

    protected float getFinishLineWidth() {
        return finishLineWidth;
    }

    protected float getFinishLineX() {
        return finishLineX;
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        roadItems.Update(touchEvents, deltaTime);
        car.Update(touchEvents, deltaTime);

        for (Updatable updatable : updatables)
            updatable.Update(touchEvents, deltaTime);
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.fillImageTexture(Assets.GetGrass(), 0, 0, game.getWidth(), game.getHeight());
        graphics.fillImageTexture(Assets.GetTarmac(), 0, grassSize, game.getWidth(), game.getHeight() - grassSize * 2);

        drawFinishLine(graphics);

        for (int i = 0; i < game.getWidth(); i += 10) {
            graphics.drawImage(Assets.getBorder(), i, grassSize - 19, 0, 0, 10, 25);
            graphics.drawImage(Assets.getBorder(), i, game.getHeight() - grassSize - 6, 0, 25, 10, 25);
        }

        roadItems.Draw(graphics, deltaTime);
        car.Draw(graphics, deltaTime);

        for (Drawable drawable : drawables)
            drawable.Draw(graphics, deltaTime);
    }

    private void drawFinishLine(Graphics graphics) {
        int height = game.getHeight() - 2 * grassSize, squareHeight = height / finishLineScale, squareWidth = 40, width = 120;
        graphics.drawRect(finishLineX, grassSize, width, height, Color.WHITE);
        for (int i = squareHeight; i < height; i += 2 * squareHeight)
            graphics.drawRect(finishLineX, grassSize + i, 41, squareHeight + 1, Color.BLACK);
        for (int i = 0; i < height; i += 2 * squareHeight)
            graphics.drawRect(finishLineX + squareWidth, grassSize + i, 41, squareHeight + 1, Color.BLACK);
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

    protected  void showFailureScreen(){carGame.showFailureScreen();}

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
