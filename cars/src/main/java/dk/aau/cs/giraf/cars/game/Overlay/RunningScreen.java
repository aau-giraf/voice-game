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


    public RunningScreen(GameActivity game, ObstacleGenerator obstacleGenerator, GameSettings gs) {
        super(game, obstacleGenerator, gs);
        driving=true;
        car.ResetCar(game.getHeight(), grassSize, verticalMover);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);


    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);


    }


}
