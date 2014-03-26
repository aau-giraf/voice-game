package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;

import java.util.LinkedList;

public class GameSettings {
    private LinkedList<Integer> colors;
    private int speed;

    public GameSettings(LinkedList<Integer> colors, int speed)
    {
        this.colors = colors;
        this.speed = speed;
    }
}
