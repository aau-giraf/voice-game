package dk.aau.cs.giraf.cars.gamecode.GameObjects;

import android.graphics.Point;
import android.graphics.Rect;
import dk.aau.cs.giraf.cars.R;

public class Bump extends ObstacleObject {
   public Bump(int lane, int column)
   {
       super(lane,column);
       modelId=R.drawable.bump;
       srcRect = new Rect(0, 0, 411, 288);
   }

    public Bump(Point p)
    {
        this(p.x,p.y);
    }
}
