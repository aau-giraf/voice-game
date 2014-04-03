package dk.aau.cs.giraf.cars.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.graphics.Point;
import android.util.Log;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.game.Overlay.CrashOverlay;
import dk.aau.cs.giraf.cars.game.Overlay.StartOverlay;
import dk.aau.cs.giraf.cars.game.Overlay.WinningOverlay;

public class GameScreen extends Screen {
    private GameSettings gameSettings;
    private CarControl carControl;
    private Car car;
    private float speed; //Pixels per second

    private LinkedList<Integer> colors;

    private ObstacleGenerator obstacleGenerator;
    private ArrayList<Obstacle> obstacles;

    private ArrayList<Garage> garages;

    private final int pixelsPerSecond = 200;
    private final int grassSize = 70;
    private final float garageSize = 250;
    private float animationZoneX;
    private final float animationZoneSize = 50;

    private GameState state = GameState.Starting;
    private int amountOfGarages = 3;
    private int startingSeconds = 3;

    private WinningOverlay winningOverlay;
    private StartOverlay startOverlay;
    private CrashOverlay crashedOverlay;

    private Garage closingGarage = null;
    public GameScreen(GameActivity game, ObstacleGenerator obstacleGenerator, GameSettings gs) {
        super(game);

        gameSettings = gs;
        colors = (LinkedList<Integer>) gs.GetColors().clone();
        Collections.shuffle(colors);

        this.car = new Car(0, 0, 200, 99);
        this.car.x = -car.width;
        this.car.y = (game.getHeight() - car.height) / 2f;

        this.carControl = new TouchCarControl();
        this.speed = gs.GetSpeed();

        this.obstacles = new ArrayList<Obstacle>();
        this.obstacleGenerator = obstacleGenerator;

        for (Obstacle o : obstacleGenerator.CreateObstacles(game.getWidth(), game.getHeight()))
            this.obstacles.add(o);

        this.garages = new ArrayList<Garage>();
        float garageSpace = (game.getHeight() - 2 * grassSize - 3 * garageSize) / 4f;
        for (int i = 0; i < amountOfGarages; i++) {
            Garage g = new Garage(game.getWidth() - garageSize, grassSize + (i + 1) * garageSpace + i * garageSize + garageSize / 4, garageSize, garageSize / 2);
            Log.d("Settings", colors.toString());
            g.setColor(colors.get(i));
            garages.add(g);
        }

        this.animationZoneX = game.getWidth() - garageSize - animationZoneSize;

        Collections.shuffle(colors);
        car.setColor(colors.removeFirst());

        winningOverlay = new WinningOverlay(game);
        startOverlay = new StartOverlay(game, startingSeconds);
        crashedOverlay = new CrashOverlay(game);
    }


    @Override
    public void update(float deltaTime) {
        if (state == GameState.Starting)
            updateStarting(deltaTime);
        if (state == GameState.Running)
            updateRunning(deltaTime);
        if (state == GameState.Crashed)
            updateCrashed(deltaTime);
        if (state == GameState.Won)
            updateWon(deltaTime);
        if(state == GameState.Closing) {
            updateClosing(deltaTime);
            updateRunning(deltaTime);
        }
    }

    private void updateStarting(float deltaTime) {
        if (startOverlay.IsTimerDone(deltaTime))
            state = GameState.Running;
    }

    private void updateClosing(float deltaTime) {
        for (Garage g : garages) {
            g.Update(deltaTime);
            if (g.isClosing())
                return;
        }
        state = GameState.Running;
    }

    private void updateWon(float deltaTime)
    {
        carControl.Reset();
        winningOverlay.Update(deltaTime);
        if (winningOverlay.ResetButtonPressed(game.getTouchEvents())) {
            game.setScreen(new GameScreen((GameActivity) game, new TestObstacles(), gameSettings));
            state = GameState.Running;
        } else if (winningOverlay.MenuButtonPressed(game.getTouchEvents())) {
            ((GameActivity) game).finish();
        }
    }

    private void updateCrashed(float deltaTime)
    {
        carControl.Reset();
        crashedOverlay.Update(deltaTime);
        if (crashedOverlay.ContinueButtonPressed(game.getTouchEvents())) {
            resetRound(false);
            state = GameState.Running;
        }
    }

    private float animationMove()
    {
        Map<Double,Point> distances = new HashMap<Double, Point>();

        for(Garage garage : garages)
        {
            distances.put(Math.abs((double)car.y-garage.y),new Point((int)(garage.x - garage.height/2),(int)garage.y));
        }
        double minDistance = Collections.min(distances.keySet());
        Point p = distances.get(minDistance);

        if (car.y+3 < p.y)
            return 1;
        else if (car.y-3 > p.y)
            return -1;
        else
            return 0;
    }
    private void updateRunning(float deltaTime)
    {
        if (allGaragesClosed())
        {
            state = GameState.Won;
            return;
        }

        car.Update(deltaTime);
        car.x += speed * (deltaTime / 1000.0f);
        if (car.x > game.getWidth())
            car.x = -car.width;

        float move = carControl.getMove(game);
        if (car.x + car.width >= animationZoneX)
            move = animationMove();
        move = Math.min(Math.max(move, -1), 1);
        move *= pixelsPerSecond * (deltaTime / 1000.0f);
        car.y += move;
        if (car.y < grassSize) car.y = grassSize;
        if (car.y > game.getHeight() - car.height - grassSize)
            car.y = game.getHeight() - car.height - grassSize;

        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).Update(deltaTime);
            if (obstacles.get(i).CollidesWith(car)) {
                crashedOverlay.setLastCrash(obstacles.get(i).GetCollisionCenter(car));
                state = GameState.Crashed;
                return ;
            }
        }

        for (Garage garage : garages) {
            garage.Update(deltaTime);
            if (garage.CollidesWith(car)) {
                if (car.color == garage.color && !garage.getIsClosed()) {
                    garage.Close();
                    state = GameState.Closing;
                }
                else if (car.color == garage.color && garage.getIsClosed())
                    resetRound(true);
                else
                {
                    crashedOverlay.setLastCrash( garage.GetCollisionCenter(car));
                    state = GameState.Crashed;
                    return;
                }
            }
        }
    }

    private boolean allGaragesClosed() {
        int counter = 0;
        for (Garage garage : garages) {
            if (garage.getIsClosed())
                counter++;
        }
        if (amountOfGarages == counter)
            return true;
        else
            return false;
    }


    private void resetRound(boolean garageJustClosed) {
        if (garageJustClosed)
            if (colors.size() > 0) //This shouldn't be necessary, but game doesn't quite stop even though all 3 garages are closed
                car.setColor(colors.removeFirst());
        this.obstacles.clear();
        for (Obstacle o : obstacleGenerator.CreateObstacles(game.getWidth(), game.getHeight()))
            this.obstacles.add(o);
        car.x = -car.width;
        this.car.y = (game.getHeight() - car.height) / 2f;
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.fillImageTexture(Assets.GetGrass(), 0, 0, game.getWidth(), game.getHeight());
        graphics.fillImageTexture(Assets.GetTarmac(), 0, grassSize, game.getWidth(), game.getHeight() - grassSize * 2);

        for (int i = 0; i < game.getWidth(); i += 10) {
            graphics.drawImage(Assets.getBorder(), i, grassSize - 19, 0, 0, 10, 25);
            graphics.drawImage(Assets.getBorder(), i, game.getHeight() - grassSize - 6, 0, 25, 10, 25);
        }

        car.Draw(graphics, deltaTime);

        for (Obstacle obstacle : obstacles)
            obstacle.Draw(graphics, deltaTime);

        for (Garage garage : garages)
            garage.Draw(graphics, deltaTime);

        if (state == GameState.Starting)
            startOverlay.Draw(graphics, deltaTime);
        if (state == GameState.Running)
            drawRunning(graphics, deltaTime);
        if (state == GameState.Crashed)
            crashedOverlay.Draw(graphics, deltaTime);
        if (state == GameState.Won)
            winningOverlay.Draw(graphics, deltaTime);
    }

    private void drawRunning(Graphics graphics, float deltaTime) {

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
