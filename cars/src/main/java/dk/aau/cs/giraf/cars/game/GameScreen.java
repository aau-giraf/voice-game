package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.LinkedList;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.framework.Graphics;

public class GameScreen extends Screen {
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

    private GameState state = GameState.Running;
    private Paint paint = new Paint();
    private int amountOfGarages = 3;

    private WinningOverlay winningOverlay;

    public GameScreen(Game game, ObstacleGenerator obstacleGenerator) {
        super(game);

        colors = new LinkedList<Integer>();
        colors.add(Color.MAGENTA);
        colors.add(Color.CYAN);
        colors.add(Color.GREEN);

        this.car = new Car(0, 0, 200, 99);
        this.car.x = -car.width;
        this.car.y = (game.getHeight() - car.height) / 2f;

        car.setColor(colors.getFirst());

        this.carControl = new TouchCarControl();
        this.speed = 70;

        this.obstacles = new ArrayList<Obstacle>();
        this.obstacleGenerator = obstacleGenerator;

        for (Obstacle o : obstacleGenerator.CreateObstacles(game.getWidth(), game.getHeight()))
            this.obstacles.add(o);

        this.garages = new ArrayList<Garage>();
        float garageSpace = (game.getHeight() - 2 * grassSize - 3 * garageSize) / 4f;
        for (int i = 0; i < amountOfGarages; i++) {
            Garage g = new Garage(game.getWidth() - garageSize, grassSize + (i + 1) * garageSpace + i * garageSize + garageSize/4, garageSize, garageSize/2);
            g.setColor(colors.get(i));
            garages.add(g);
        }
        initializePaint();
        winningOverlay = new WinningOverlay();
    }



    @Override
    public void update(float deltaTime) {
        if(state == GameState.Running)
            updateRunning(deltaTime);
        if(state == GameState.Won)
            updateWon();
    }

    private void updateRunning(float deltaTime)
    {
        if (allGaragesClosed())
            state = GameState.Won;

        car.Update(deltaTime);
        car.x += speed * (deltaTime / 100.0f);
        if (car.x > game.getWidth())
            car.x = -car.width;

        float move = carControl.getMove(game);
        move = Math.min(Math.max(move, -1), 1);
        move *= pixelsPerSecond * (deltaTime / 100.0f);
        car.y += move;
        if (car.y < grassSize) car.y = grassSize;
        if (car.y > game.getHeight() - car.height - grassSize)
            car.y = game.getHeight() - car.height - grassSize;

        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).Update(deltaTime);
            if (obstacles.get(i).CollidesWith(car)) {
                resetRound(false);
                break;
            }
        }

        boolean anyOpen = true;
        for (Garage garage : garages) {
            garage.Update(deltaTime);
            if (garage.CollidesWith(car)) {
                if (garage.getIsClosed())
                    resetRound(false);
                else if (car.color == garage.color) {
                    garage.Close();
                    resetRound(true);
                }
                else
                    resetRound(false);
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



    private void updateWon()
    {
        state = winningOverlay.ButtonPressed(game);
    }

    private void resetRound(boolean garageJustClosed) {
        if (garageJustClosed)
        {
            if (colors.size() > 1) {
                colors.removeFirst();
                car.setColor(colors.getFirst());
            }
        }
        this.obstacles.clear();
        for (Obstacle o : obstacleGenerator.CreateObstacles(game.getWidth(), game.getHeight()))
            this.obstacles.add(o);
        car.x = -car.width;
        this.car.y = (game.getHeight() - car.height) / 2f;
    }

    @Override
    public void paint(float deltaTime) {
        if(state == GameState.Running)
            drawRunning(deltaTime);
        if(state == GameState.Won)
            drawWon();
    }

    private void initializePaint()
    {
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
    }

    private void drawRunning(float deltaTime)
    {
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
    }

    private void drawWon()
    {
        winningOverlay.Draw(game, paint);
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
