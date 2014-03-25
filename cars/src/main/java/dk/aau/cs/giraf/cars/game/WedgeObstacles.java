package dk.aau.cs.giraf.cars.game;


import android.graphics.Point;
import android.graphics.PointF;

import java.util.Random;
import java.util.Vector;

public class WedgeObstacles implements ObstacleGenerator{
    @Override
    public Obstacle[] CreateObstacles(int width, int height) {
        return new Obstacle[0];
    }

    private float carWidth;
    private float horizontalSpeed;
    private float verticalSpeed;
    private float bufferMultipler; // the buffer given to react to the next obstacle
    private int gameHeight;
    private int gameWidth;

    public WedgeObstacles(float carWidth, float horizontalSpeed,float verticalSpeed,float bufferMultipler, int gameHeight, int gameWidth)
    {
        this.carWidth = carWidth;
        this.horizontalSpeed = horizontalSpeed;
        this.verticalSpeed = verticalSpeed;
        this.bufferMultipler = bufferMultipler;
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
    }
    private PointF placeObstacleAfterWedge(PointF[] wedge )
    {

    }

    private PointF findObstaclePlacement(PointF[] wedge )
    {
        Random rand = new Random();
        PointF left;
        PointF right;

        Boolean r = rand.nextBoolean();
        if(r) {
            left = wedge[0];
            right = wedge[1];
        }
        else {
            left = wedge[2];
            right = wedge[3];
        }

        PointF vec = new PointF(right.x - left.x,right.y-left.y);
        float length = vec.length();

        float placement = rand.nextFloat()*length;

        PointF displacedPoint = displacePoint(left,right,length/placement);
    }

    private PointF displacePoint(PointF p, PointF x, float displacementFactor )
    {
        return new PointF( (p.x-x.x)*displacementFactor + x.x,(p.y-x.y)*displacementFactor + x.y);
    }

    private PointF[] calculateWedge(PointF carPosition, float carHeight)
    {
        float angle = wedgeAngle(horizontalSpeed,verticalSpeed);

        PointF topWedgeCarCoord = new PointF(carPosition.x, carPosition.y - carHeight/2);
        PointF bottomWedgeCarCoord = new PointF(carPosition.x, carPosition.y + carHeight/2);

        PointF bottomWedgeIntersectionCoord = findIntersection(topWedgeCarCoord,angle,gameHeight);
        PointF topWedgeIntersectionCoord = findIntersection(bottomWedgeCarCoord,-angle,0);

        PointF[] points={topWedgeCarCoord,topWedgeIntersectionCoord,bottomWedgeCarCoord,bottomWedgeIntersectionCoord};

        return points;
    }

    private PointF findIntersection(PointF p, float angle, float lineY)
    {
        float x = (angle * p.x - p.y + lineY) / angle;
        float y = angle * (x - p.x) + p.y;

        return new PointF(x,y);
    }

    private float wedgeAngle(float horizontal, float vertical)
    {
        float moveVector = pythagorean(horizontal, vertical);
            return (float)Math.acos(Math.pow(horizontal,2) + Math.pow(moveVector,2) - Math.pow(vertical,2))
                    / (2*horizontal *moveVector);
    }

    private float pythagorean(float horizontal, float vertical)
    {
            return (float)Math.sqrt(Math.pow( horizontal,2) + Math.pow(vertical,2));
    }
}
