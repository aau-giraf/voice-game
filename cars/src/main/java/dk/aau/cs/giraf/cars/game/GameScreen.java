package dk.aau.cs.giraf.cars.game;

import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.MoveSineLine;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.framework.mFloat;
import dk.aau.cs.giraf.cars.game.Controller.TouchCarControl;
import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.Overlay.CrashOverlay;
import dk.aau.cs.giraf.cars.game.Overlay.RunningScreen;

public abstract class GameScreen extends Screen {
    protected Car car;
    protected final mFloat verticalMover;
    protected final int grassSize = 70;
    protected CarControl carControl;
    protected float animationZoneX;
    private final float animationZoneSize = 100;

    private ArrayList<Garage> garages;
    private int amountOfGarages = 3;
    private final float garageSize = 250;

    private LinkedList<Integer> colors;
    private ArrayList<Obstacle> obstacles;

    private Rect pauseButtonRec = new Rect(20, 20, 100, 100);
    private Rect pauseButtonImageRec = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());

    private GameSettings gameSettings;
    private ObstacleGenerator obstacleGenerator;
    private GameActivity gameActivity;

    public GameActivity GetGameActivity() {
        return gameActivity;
    }

    public ObstacleGenerator GetObstacleGenerator() {
        return obstacleGenerator;
    }

    public GameSettings GetGameSettings() {
        return gameSettings;
    }

    public GameScreen(GameActivity game, ObstacleGenerator obstacleGenerator, GameSettings gs) {
        super(game);
        this.car = new Car(0, 0, 200, 99);
        this.gameActivity = game;
        this.obstacleGenerator = obstacleGenerator;
        this.gameSettings = gs;
        this.verticalMover = new mFloat(0, new MoveSineLine(0.5f, 200));
        this.animationZoneX = garages.get(0).x - animationZoneSize;

        this.carControl = new TouchCarControl(game.getHeight() - 2 * grassSize - (int)car.getHeight(), grassSize + (int)car.getHeight() / 2);
        //this.carControl = new VolumeCarControl(gs.GetMinVolume(), gs.GetMaxVolume());

        this.garages = new ArrayList<Garage>();
        float garageSpace = (game.getHeight() - 2 * grassSize - 3 * garageSize) / 4f;
        for (int i = 0; i < amountOfGarages; i++) {
            Garage g = new Garage(game.getWidth() - garageSize, grassSize + (i + 1) * garageSpace + i * garageSize + garageSize / 4, garageSize, garageSize / 2);
            Log.d("Settings", colors.toString());
            g.setColor(colors.get(i));
            garages.add(g);
        }

        colors = (LinkedList<Integer>) gs.GetColors().clone();
        Collections.shuffle(colors);

        Collections.shuffle(colors);
        car.setColor(colors.removeFirst());

        this.obstacles = new ArrayList<Obstacle>();

        for (Obstacle o : obstacleGenerator.CreateObstacles(game.getWidth(), game.getHeight()))
            this.obstacles.add(o);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).Update(touchEvents, deltaTime);
            if (obstacles.get(i).CollidesWith(car)) {
                CrashOverlay crashedOverlay = new CrashOverlay(gameActivity,obstacleGenerator,gameSettings);
                crashedOverlay.setLastCrash(obstacles.get(i).GetCollisionCenter(car));
                game.setScreen(crashedOverlay);
                return;
            }
        }

        for (Garage garage : garages) {
            garage.Update(touchEvents, deltaTime);
            if (garage.CollidesWith(car) && garage.color != car.color) {
                CrashOverlay crashedOverlay = new CrashOverlay(gameActivity,obstacleGenerator,gameSettings);
                crashedOverlay.setLastCrash(garage.GetCollisionCenter(car));
                game.setScreen(crashedOverlay);
                return;
            } else {
                if (car.getColor() == garage.getColor() && car.GetBounds().left > garage.GetBounds().left) {
                    garage.Close();
                    //state = GameState.Closing;
                }
            }
        }
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        for (Obstacle obstacle : obstacles)
            obstacle.Draw(graphics, deltaTime);

        for (Garage garage : garages)
            garage.Draw(graphics, deltaTime);
        if (car.x + car.width < animationZoneX)
            graphics.drawScaledImage(Assets.GetPauseButton(), pauseButtonRec, pauseButtonImageRec);
    }

    public boolean allGaragesClosed() {
        int counter = 0;
        for (Garage garage : garages) {
            if (garage.getIsClosed())
                counter++;
        }
        if (amountOfGarages == counter)
            return true;
        return false;
    }

    public float getGarageTargetY() {
        float minDist = Float.MAX_VALUE;
        Garage garage = null;

        for (Garage g : garages) {
            float dist = Math.abs(car.y - g.y);
            if (dist < minDist) {
                minDist = dist;
                garage = g;
            }
        }
        return garage.y + garage.height / 2f;
    }

    private void updateClosing(Input.TouchEvent[] touchEvents, float deltaTime) {
        for (Garage g : garages) {
            Log.d("garage", String.format("Car: %S Garage: %S CarLeft: %S GarageLeft: %S", car.getColor(), g.getColor(), car.GetBounds().left, g.GetBounds().left));
            g.Update(touchEvents, deltaTime);

            if (car.getColor() == g.getColor() && car.GetBounds().left > g.GetBounds().left && g.getIsClosed()) {
                resetRound();
                Log.d("garage", "reseT?");
                game.setScreen(new RunningScreen(gameActivity,obstacleGenerator,gameSettings));
            }
        }
    }

    public void resetRound() {
        boolean newColor = ResetCarColor();

        if (newColor)
            car.ResetCar(game.getHeight(), grassSize, verticalMover);
    }

    private boolean ResetCarColor() {
        if (colors.size() > 0) //This shouldn't be necessary, but game doesn't quite stop even though all 3 garages are closed
        {
            car.setColor(colors.removeFirst());
            return true;
        } else return false;
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
}
