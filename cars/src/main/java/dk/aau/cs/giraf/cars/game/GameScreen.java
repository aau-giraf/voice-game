package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import dk.aau.cs.giraf.cars.framework.GameActivity;
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
    private float animationZoneX;
    private final float animationZoneSize = 100;
    private float speed; //Pixels per second

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

    public GameScreen(GameActivity game, ObstacleGenerator obstacleGenerator, GameSettings gs) {
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

        colors = (LinkedList<Integer>) gs.GetColors().clone();
        Collections.shuffle(colors);
        this.garages = new ArrayList<Garage>();
        float garageSpace = (game.getHeight() - 2 * grassSize - 3 * garageSize) / 4f;
        for (int i = 0; i < amountOfGarages; i++) {
            Garage g = new Garage(game.getWidth() - garageSize, grassSize + (i + 1) * garageSpace + i * garageSize + garageSize / 4, garageSize, garageSize / 2);
            Log.d("Settings", colors.toString());
            g.setColor(colors.get(i));
            garages.add(g);
        }

        this.animationZoneX = garages.get(0).x - animationZoneSize;

        Collections.shuffle(colors);
        car.setColor(colors.removeFirst());

        this.obstacles = new ArrayList<Obstacle>();

        for (Obstacle o : obstacleGenerator.CreateObstacles(game.getWidth(), game.getHeight()))
            this.obstacles.add(o);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        if (allGaragesClosed()) {
            //game.setScreen(new WinningOverlay());
        }

        car.Update(touchEvents, deltaTime);
        car.x += speed * (deltaTime / 1000.0f);

        float moveTo = 1f - carControl.getMove(touchEvents);
        moveTo *= (game.getHeight() - grassSize * 2 - car.height);
        moveTo += grassSize;

        if (car.x + car.width >= animationZoneX)
            moveTo = getGarageTargetY() - car.height / 2;

        car.setVerticalTarget(moveTo);

        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).Update(touchEvents, deltaTime);
            if (obstacles.get(i).CollidesWith(car)) {
                CrashOverlay crashedOverlay = new CrashOverlay(gameActivity, obstacleGenerator, gameSettings);
                crashedOverlay.setLastCrash(obstacles.get(i).GetCollisionCenter(car));

                game.setScreen(crashedOverlay);
                return;
            }
        }

        for (Garage garage : garages) {
            garage.Update(touchEvents, deltaTime);
            if (garage.CollidesWith(car) && garage.color != car.getColor()) {
                CrashOverlay crashedOverlay = new CrashOverlay(gameActivity, obstacleGenerator, gameSettings);
                crashedOverlay.setLastCrash(garage.GetCollisionCenter(car));
                game.setScreen(crashedOverlay);
                return;
            } else {
                if (car.getColor() == garage.getColor() && car.GetBounds().left > garage.GetBounds().left) {
                    garage.Close();
                }
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
                ShowRunningScreen();
            }
        }
    }

    public void resetRound() {
        boolean newColor = ResetCarColor();

        if (newColor)
            car.ResetCar();
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

    public void ShowRunningScreen() {
        ((CarGame) gameActivity).ShowRunningScreen();
    }
}
