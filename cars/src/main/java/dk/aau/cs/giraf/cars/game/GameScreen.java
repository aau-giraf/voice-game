package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import android.util.Log;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.framework.Graphics;

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
    private final float garageSize = 150;

    private GameState state = GameState.Starting;
    private int amountOfGarages = 3;
    private int startingSeconds = 3;

    private WinningOverlay winningOverlay;
    private StartOverlay startOverlay;
    private CrashOverlay crashedOverlay;

    public GameScreen(Game game, ObstacleGenerator obstacleGenerator, GameSettings gs) {
        super(game);

        gameSettings = gs;
        colors = (LinkedList<Integer>)gs.GetColors().clone();
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
            Garage g = new Garage(game.getWidth() - garageSize, grassSize + (i + 1) * garageSpace + i * garageSize + garageSize/4, garageSize, garageSize/2);
            Log.d("Settings",colors.toString());
            g.setColor(colors.get(i));
            garages.add(g);
        }

        Collections.shuffle(colors);
        car.setColor(colors.removeFirst());

        winningOverlay = new WinningOverlay(gs);
        startOverlay = new StartOverlay(startingSeconds);
        crashedOverlay = new CrashOverlay();
    }



    @Override
    public void update(float deltaTime) {
        if (state == GameState.Starting)
            state = startOverlay.UpdateTime(deltaTime);
        if(state == GameState.Running)
            updateRunning(deltaTime);
        if(state == GameState.Crashed)
            updateCrashed();
        if(state == GameState.Won)
            updateWon();
    }

    private void updateWon()
    {
        carControl.Reset();
        state = winningOverlay.ButtonPressed(game);
    }

    private void updateCrashed()
    {
        carControl.Reset();
        state = crashedOverlay.ButtonPressed(game);
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
        move = Math.min(Math.max(move, -1), 1);
        move *= pixelsPerSecond * (deltaTime / 1000.0f);
        car.y += move;
        if (car.y < grassSize) car.y = grassSize;
        if (car.y > game.getHeight() - car.height - grassSize)
            car.y = game.getHeight() - car.height - grassSize;

        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).Update(deltaTime);
            if (obstacles.get(i).CollidesWith(car)) {
                resetRound(false);
                state = GameState.Crashed;
                return;
            }
        }

        boolean anyOpen = true;
        for (Garage garage : garages) {
            garage.Update(deltaTime);
            if (garage.CollidesWith(car)) {
                if (car.color == garage.color && !garage.getIsClosed())
                {
                    garage.Close();
                    resetRound(true);
                }
                else
                {
                    resetRound(false);
                    state = GameState.Crashed;
                    return;
                }
            }
            if (garage.getIsClosed())
                anyOpen = false;
        }
    }

    private boolean allGaragesClosed()
    {
        int counter=0;
        for (Garage garage : garages)
        {
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
    public void paint(float deltaTime) {
        Graphics graphics = game.getGraphics();
        graphics.fillImageTexture(Assets.GetGrass(), 0, 0, game.getWidth(), game.getHeight());
        graphics.fillImageTexture(Assets.GetTarmac(), 0, grassSize, game.getWidth(), game.getHeight() - grassSize * 2);

        for (int i = 0; i < game.getWidth(); i += 10) {
            graphics.drawImage(Assets.getBorder(), i, grassSize - 19, 0, 0, 10, 25);
            graphics.drawImage(Assets.getBorder(), i, game.getHeight() - grassSize - 6, 0, 25, 10, 25);
        }

        car.Paint(graphics, deltaTime);

        for (Obstacle obstacle : obstacles)
            obstacle.Paint(graphics, deltaTime);

        for (Garage garage : garages)
            garage.Paint(graphics, deltaTime);

        if (state == GameState.Starting)
            startOverlay.Draw(game);
        if(state == GameState.Running)
            drawRunning(deltaTime);
        if (state == GameState.Crashed)
            crashedOverlay.Draw(game);
        if(state == GameState.Won)
            winningOverlay.Draw(game);
    }

    private void drawRunning(float deltaTime)
    {

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
