package dk.aau.cs.giraf.cars.gamecode.GameObjects;

import android.graphics.Rect;
import dk.aau.cs.giraf.cars.R;


public class Cat extends ObstacleObject {

    public Cat(int lane, int column)
    {
        super(lane,column);
        modelId = R.drawable.cat;
        srcRect = new Rect(0, 0, 219, 271);
    }
}
