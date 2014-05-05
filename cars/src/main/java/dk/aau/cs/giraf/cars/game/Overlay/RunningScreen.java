package dk.aau.cs.giraf.cars.game.Overlay;

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
import dk.aau.cs.giraf.cars.framework.MoveSineLine;
import dk.aau.cs.giraf.cars.framework.mFloat;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.Controller.TouchCarControl;
import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.GameState;
import dk.aau.cs.giraf.cars.game.Garage;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.Obstacle;
import dk.aau.cs.giraf.cars.game.ObstacleGenerator;

public class RunningScreen extends GameScreen {
    private final boolean debug = false;



    private float speed; //Pixels per second





    public RunningScreen(GameActivity game, ObstacleGenerator obstacleGenerator, GameSettings gs) {
        super(game, obstacleGenerator, gs);

        this.car.SetShowValue(true);
        car.ResetCar(game.getHeight(),grassSize,verticalMover);

        this.speed = gs.GetSpeed() * (Car.MAX_PIXELSPERSECOND / Car.MAX_SCALE);






    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);

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

        if (moveTo != verticalMover.getTargetValue())
            verticalMover.setTargetValue(moveTo);

        verticalMover.Update();
        car.y = verticalMover.getCurrentValue();
        Log.d("position", moveTo + "p " + car.y);

        if (car.y < grassSize) car.y = grassSize;
        if (car.y > game.getHeight() - car.height - grassSize)
            car.y = game.getHeight() - car.height - grassSize;


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


    }




}
