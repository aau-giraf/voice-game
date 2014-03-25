package dk.aau.cs.giraf.cars.game;


import android.graphics.PointF;

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

    public WedgeObstacles(float carWidth, float horizontalSpeed,float verticalSpeed,float bufferMultipler, int gameHeight)
    {
        this.carWidth = carWidth;
        this.horizontalSpeed = horizontalSpeed;
        this.verticalSpeed = verticalSpeed;
        this.bufferMultipler = bufferMultipler;
        this.gameHeight = gameHeight;
    }

    private PointF[] calculateWedge(PointF carPosition)
    {
        float angle = wedgeAngle(horizontalSpeed,verticalSpeed);
        PointF up = findIntersection(carPosition,angle,gameHeight);
        PointF down = findIntersection(carPosition,-angle,0);

        PointF[] points={carPosition, up, down};

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
        float moveVector = moveVectorLength(horizontal,vertical);
            return (float)Math.acos(Math.pow(horizontal,2) + Math.pow(moveVector,2) - Math.pow(vertical,2))
                    / (2*horizontal *moveVector);
    }

    private float moveVectorLength(float horizontal, float vertical)
    {
            return (float)Math.sqrt(Math.pow( horizontal,2) + Math.pow(vertical,2));
    }
}
