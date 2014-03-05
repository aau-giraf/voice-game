package dk.aau.cs.giraf.cars.gamecode.GameObjects;

import android.graphics.Point;
import android.graphics.Rect;
import dk.aau.cs.giraf.cars.R;

public class Rock extends ObstacleObject{

    public Rock(int lane, int column) {
        super(lane, column);
        modelId=R.drawable.rock;
        srcRect = new Rect(0, 0, 383, 278);
    }

    public Rock(Point p)
    {
        this(p.x,p.y);
    }
}
