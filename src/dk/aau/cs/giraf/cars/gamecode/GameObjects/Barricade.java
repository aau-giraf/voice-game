package dk.aau.cs.giraf.cars.gamecode.GameObjects;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Point;
import android.graphics.Rect;
import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.gamecode.GameInfo;
import dk.aau.cs.giraf.cars.gamecode.GameObject;
import dk.aau.cs.giraf.cars.gamecode.GameRenderer;
import dk.aau.cs.giraf.cars.gamecode.ICollidable;
import dk.aau.cs.giraf.cars.gamecode.IDrawable;
import dk.aau.cs.giraf.cars.gamecode.MapDivider;

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
