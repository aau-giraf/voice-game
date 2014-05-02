package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.*;

import dk.aau.cs.giraf.cars.R;
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
import dk.aau.cs.giraf.cars.game.Overlay.PauseOverlay;
import dk.aau.cs.giraf.cars.game.Overlay.StartOverlay;
import dk.aau.cs.giraf.cars.game.Overlay.WinningOverlay;

public abstract class GameScreen extends Screen {
    private final boolean debug = false;

    private final mFloat verticalMover;
    private final int grassSize = 70;
    private final float garageSize = 250;
    private final float animationZoneSize = 100;
    private GameSettings gameSettings;
    private CarControl carControl;
    private Car car;
    private float speed; //Pixels per second
    private LinkedList<Integer> colors;
    private ObstacleGenerator obstacleGenerator;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Garage> garages;
    private float animationZoneX;
    private GameState state = GameState.Starting;
    private int amountOfGarages = 3;
    private Rect pauseButtonRec = new Rect(20, 20, 100, 100);
    private Rect pauseButtonImageRec = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());

    private WinningOverlay winningOverlay;
    private StartOverlay startOverlay;
    private CrashOverlay crashedOverlay;
    private PauseOverlay pauseOverlay;

    private GameActivity gameActivity;
    public GameActivity GetGameActivity()
    {
        return gameActivity;
    }
    public ObstacleGenerator GetObstacleGenerator()
    {
        return obstacleGenerator;
    }
    public GameSettings GetGameSettings()
    {
        return gameSettings;
    }

    public GameScreen(GameActivity game, ObstacleGenerator obstacleGenerator, GameSettings gs) {
        super(game);

        verticalMover = new mFloat(0, new MoveSineLine(0.5f, 200));

        gameSettings = gs;
        colors = (LinkedList<Integer>) gs.GetColors().clone();
        Collections.shuffle(colors);

        this.car = new Car(0, 0, 200, 99);
        this.car.showValue = true;
        crashedOverlay = new CrashOverlay(game,carControl,car,grassSize,verticalMover);
        car.ResetCar(game.getHeight(),grassSize,verticalMover);

        //this.carControl = new VolumeCarControl(gs.GetMinVolume(), gs.GetMaxVolume());
        this.carControl = new TouchCarControl(game.getHeight() - 2 * grassSize - (int)car.height, grassSize + (int)car.height / 2);
        this.speed = gs.GetSpeed() * (Car.MAX_PIXELSPERSECOND / Car.MAX_SCALE);

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


        this.animationZoneX = garages.get(0).x - animationZoneSize;

        Collections.shuffle(colors);
        car.setColor(colors.removeFirst());

        winningOverlay = new WinningOverlay(game,
                game.getResources().getString(R.string.play_again_button_text),
                game.getResources().getString(R.string.menu_button_text),carControl,gameSettings);

        pauseOverlay = new PauseOverlay((int) car.x, grassSize, game.getHeight() - 2 * grassSize, game.getWidth(),car);
    }


    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        if (allGaragesClosed()) {
            state = GameState.Won;
            return;
        }

        car.Update(touchEvents, deltaTime);
        car.x += speed * (deltaTime / 1000.0f);

        float moveTo = 1f - carControl.getMove(touchEvents);
        moveTo *= (game.getHeight() - grassSize * 2 - car.height);
        moveTo += grassSize;

        if (car.x + car.width >= animationZoneX)
            moveTo = getGarageTargetY() - car.height / 2;

        if (moveTo != verticalMover.getTargetValue())
            verticalMover.setTargetValue(moveTo);

        verticalMover.Update();
        car.y = verticalMover.getCurrentValue();
        Log.d("position", moveTo + "p " + car.y);

        if (car.y < grassSize) car.y = grassSize;
        if (car.y > game.getHeight() - car.height - grassSize)
            car.y = game.getHeight() - car.height - grassSize;

        for (int i = 0; i < obstacles.size(); i++) {
            obstacles.get(i).Update(touchEvents, deltaTime);
            if (obstacles.get(i).CollidesWith(car)) {
                crashedOverlay.setLastCrash(obstacles.get(i).GetCollisionCenter(car));
                state = GameState.Crashed;
                return;
            }
        }

        for (Garage garage : garages) {
            garage.Update(touchEvents, deltaTime);
            if (garage.CollidesWith(car) && garage.color != car.color) {
                crashedOverlay.setLastCrash(garage.GetCollisionCenter(car));
                state = GameState.Crashed;
                return;
            } else {
                if (car.color == garage.color && car.GetBounds().left > garage.GetBounds().left) {
                    garage.Close();
                    state = GameState.Closing;
                }
            }
        }

        if (car.x+car.width<animationZoneX)
            state = pauseOverlay.Update(touchEvents,deltaTime);
    }

    private void updateClosing(Input.TouchEvent[] touchEvents, float deltaTime) {
        for (Garage g : garages) {
            Log.d("garage", String.format("Car: %S Garage: %S CarLeft: %S GarageLeft: %S", car.color, g.color, car.GetBounds().left, g.GetBounds().left));
            g.Update(touchEvents, deltaTime);

            if (car.color == g.color && car.GetBounds().left > g.GetBounds().left && g.getIsClosed()) {
                resetRound();
                Log.d("garage", "reseT?");
                state = GameState.Running;
            }
        }


    }

    private float getGarageTargetY() {
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


    private void resetRound() {
        boolean newColor = ResetCarColor();

        if (newColor)
            car.ResetCar(game.getHeight(),grassSize,verticalMover);
    }

    private boolean ResetCarColor() {
        if (colors.size() > 0) //This shouldn't be necessary, but game doesn't quite stop even though all 3 garages are closed
        {
            car.setColor(colors.removeFirst());
            return true;
        } else return false;
    }

    private void ResetObstacles() {
        this.obstacles.clear();
        for (Obstacle o : obstacleGenerator.CreateObstacles(game.getWidth(), game.getHeight()))
            this.obstacles.add(o);
    }

    private void ResetCar() {
        car.x = -car.width;
        this.car.y = game.getHeight() - grassSize - car.height / 2;
        this.verticalMover.setCurrentValue(car.y);
    }


    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.fillImageTexture(Assets.GetGrass(), 0, 0, game.getWidth(), game.getHeight());
        graphics.fillImageTexture(Assets.GetTarmac(), 0, grassSize, game.getWidth(), game.getHeight() - grassSize * 2);

        for (int i = 0; i < game.getWidth(); i += 10) {
            graphics.drawImage(Assets.getBorder(), i, grassSize - 19, 0, 0, 10, 25);
            graphics.drawImage(Assets.getBorder(), i, game.getHeight() - grassSize - 6, 0, 25, 10, 25);
        }


        if (debug) {
            Paint debug = new Paint();
            debug.setTextSize(50);
            debug.setTextAlign(Paint.Align.LEFT);
            debug.setAntiAlias(true);
            debug.setColor(Color.RED);

            graphics.drawString(((VolumeCarControl) carControl).GetMinAmplitude() + "", 100, 100, debug);
            graphics.drawString(((VolumeCarControl) carControl).GetMaxAmplitude() + "", 100, 200, debug);
        }

        car.Draw(graphics, deltaTime);

        for (Obstacle obstacle : obstacles)
            obstacle.Draw(graphics, deltaTime);

        for (Garage garage : garages)
            garage.Draw(graphics, deltaTime);

        graphics.drawScaledImage(Assets.GetPauseButton(), pauseButtonRec, pauseButtonImageRec);
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
        if (carControl instanceof VolumeCarControl)
            ((VolumeCarControl) carControl).Stop();
    }

    @Override
    public void backButton() {

    }
}
