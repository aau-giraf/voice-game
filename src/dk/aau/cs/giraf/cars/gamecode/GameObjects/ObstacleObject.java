package dk.aau.cs.giraf.cars.gamecode.GameObjects;

import android.graphics.Point;
import android.graphics.Rect;
import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.gamecode.*;

import javax.microedition.khronos.opengles.GL10;

public class ObstacleObject extends GameObject implements IDrawable, ICollidable{
    Rect rectangle;
    Point[] collisionBox;
    int modelId;

    public ObstacleObject(int lane, int column) {
        rectangle = MapDivider.CalculateObstacle(lane, column);
        collisionBox = new Point[4];
        int objectSideCollisionY = rectangle.top + (int) (rectangle.height() * 0.70);
        collisionBox[0] = new Point(rectangle.centerX(), rectangle.top);
        collisionBox[1] = new Point(rectangle.left, objectSideCollisionY);
        collisionBox[2] = new Point(rectangle.centerX(), rectangle.bottom);
        collisionBox[3] = new Point(rectangle.right, objectSideCollisionY);
    }

    @Override
    public void draw(GL10 gl, GameRenderer spriteBatcher) {
        // TODO Auto-generated method stub
        if (GameInfo.win == false) {
            spriteBatcher.draw(gl, modelId, new Rect(0, 0, 219, 271), rectangle);
        }
    }


    @Override
    public boolean collisionDetection() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Point[] calculateCollisionBox() {

        // TODO Auto-generated method stub
        return collisionBox;
    }

}
