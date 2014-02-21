package dk.aau.cs.giraf.cars.gamecode.GameObjects;

import android.graphics.Rect;


public class Cat extends ObstacleObject {

    public Cat(int lane, int column)
    {
        super(lane,column);
        srcRect = new Rect(0, 0, 219, 271);
    }
}
