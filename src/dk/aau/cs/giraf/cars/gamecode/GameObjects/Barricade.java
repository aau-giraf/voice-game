package dk.aau.cs.giraf.cars.gamecode.GameObjects;

import android.graphics.Point;
import android.graphics.Rect;
import dk.aau.cs.giraf.cars.R;

public class Barricade extends ObstacleObject  {
    public Barricade(int lane, int column)
    {
        super(lane,column);
        modelId = R.drawable.barricade;
        srcRect = new Rect(0, 0, 299, 306);
    }

    public Barricade(Point p)
    {
        this(p.x,p.y);
    }
}
