package dk.aau.cs.giraf.cars.framework;

public class MoveSineLine implements MoveMethod {

    private MovementInfo info;

    private static final float pi = (float)Math.PI;
    private final float accTime;
    private final float maxspeed;

    private float b;//
    private float m;//
    private float d;// distance
    private float d2;// = d / 2
    private float t;

    private float h;//
    private float h2;// = h / 2
    private float s;//startSpeed

    private float mx;
    private float my;
    private float ax;
    private float ay;

    private float c;

    private boolean decreaseSize;
    private float t1;
    private float t2;

    private float add;

    public MoveSineLine(float accelerationTime, float maximumSpeed)
    {
        this.accTime = accelerationTime;
        this.maxspeed = Math.abs(maximumSpeed);
    }

    private static float asin(float value)
    {
        //Tilpasset pga manglende præcision i float
        if (value > 1 || value < -1)
            return pi / 2;
        return (float)(Math.asin(value));
    }
    private static float acos(float value)
    {
        //Tilpasset pga manglende præcision i float
        if (value > 1)
            return 0;
        else if (value < -1)
            return pi;
        return (float)(Math.acos(value));
    }
    private static float sin(float value)
    {
        return (float)(Math.sin(value));
    }
    private static float cos(float value)
    {
        return (float)(Math.cos(value));
    }

    private float pos(float value)
    {
        return h2 - cos(value * pi / (2 * b)) * h2;
    }
    private float has(float value)
    {
        return m * sin(value * pi / (2 * b));
    }

    public float Position(float time)
    {
        if (decreaseSize) //Decrease size
        {
            if (time < t1)
                return pos(time + ax) + add;
            else
                return pos(time - mx + ax) + my + add;
        }
        else //Increase (or maintain) size
        {
            if (time < t1)
                return pos(time + ax) + add;
            else if (time > t2)
                return pos(time - mx + ax) + my + add;
            else
                return h2 + m * (time - b + ax) + add;
        }
    }

    public float Speed(float time)
    {
        if (decreaseSize) //Decrease size
        {
            if (time < t1)
                return has(time + ax);
            else
                return sin((time - mx + ax) * pi / (2 * b)) * m;
        }
        else //Increase (or maintain) size
        {
            if (time < t1)
                return has(time + ax);
            else if (time > t2)
                return sin((time - mx + ax) * pi / (2 * b)) * m;
            else
                return m;
        }
    }

    public float getTime(){
        return  t;
    }
    public MovementInfo getInfo(){
        return this.info;}
    public void setInfo(MovementInfo value){
            this.info = value;
            b = accTime;
            d = info.getPointEnd() - info.getPointStart();
            s = this.info.getSpeedStart();
            if (d < 0)
                m = -maxspeed;
            else
                m = maxspeed;
            d2 = d / 2;
            h = (4 * b * m) / pi;
            h2 = h / 2;
            ax = asin(s / m) * 2 * b / pi;
            ay = pos(ax);

            c = h - ay - d;
            decreaseSize = Math.abs(d) < Math.abs(h - ay);

            if (decreaseSize) //Decrease size
            {
                mx = -2 * (b - acos(c / h) * 2 * b / pi);
                my = -c;
                t = 2 * b + mx - ax;
                t1 = t2 = b - ax + mx / 2;

                if (acos(c / h) * 2 * b / pi - ax < 0)
                {
                    m = -m;
                    {
                        h = (4 * b * m) / pi;
                        h2 = h / 2;
                        ax = asin(s / m) * 2 * b / pi;
                        ay = pos(ax);

                        c = h - ay - d;
                        mx = -2 * (b - acos(c / h) * 2 * b / pi);
                        my = -c;
                        t = 2 * b + mx - ax;
                        t1 = t2 = b - ax + mx / 2;
                    }
                }
            }
            else //Increase (or maintain) size
            {
                mx = (d - h + ay) / m;
                my = d - h + ay;
                t = 2 * b + mx - ax;
                t1 = b - ax;
                t2 = b + mx - ax;
            }
            add = -ay + info.getPointStart();
        }

    public MoveSineLine Clone()
    {
        MoveSineLine move = new MoveSineLine(this.accTime, this.maxspeed);
        move.b = this.b;
        move.m = this.m;
        move.d = this.d;
        move.d2 = this.d2;
        move.t = this.t;
        move.h = this.h;
        move.h2 = this.h2;
        move.s = this.s;
        move.mx = this.mx;
        move.my = this.my;
        move.ax = this.ax;
        move.ay = this.ay;
        move.c = this.c;

        return move;
    }
}
