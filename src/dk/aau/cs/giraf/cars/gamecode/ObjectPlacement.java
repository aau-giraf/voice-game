package dk.aau.cs.giraf.cars.gamecode;

import android.graphics.Point;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectPlacement {

    private enum Objects {NONE, OBJECT}

    public enum Pos {
        UPPER(1), MIDDLE(2), LOWER(4);

        public final int val;

        Pos(int val) {
            this.val = val;
        }
    }

    /**
     * Determines where to place objects on the field
     *
     * @param columns    The number of columns on the field
     * @param difficulty Difficulty
     * @param start      Where the cars needs to start
     * @param end        Where the cars needs to end - not implemented :o
     * @return A list of the placed obstacles
     */
    public static List<Point> objectPlacement(int columns, int difficulty, Pos start, Pos end) {
        List<Point> obstacles = new ArrayList<Point>();

        Random rand = new Random();

        int column;

        column = rand.nextInt(2) == 1 ? 0 : 1;

        Log.d("Object", "Start");

        Pos endLane = insertObstaclesOnLane(start, column, obstacles);
        Log.d("Object", "endLane: " + endLane + " column: " + column);
        column +=2;

        for (; column < columns; column+=2) {
            //if (difficulty > 0) {
            Log.d("Object", "endLane: " + endLane + " column: " + column);
            endLane = insertObstaclesOnLane(endLane, column, obstacles);

            //difficulty--;
        }
        //}
        return obstacles;
    }

    /**
     * Inserts a pair of obstacles in the obstacles array in the specified column
     *
     * @param lane      The lane to necessarily place an obstacle
     * @param column    The column to insert the obstacle at
     * @param obstacles The array of obstacles
     * @return
     */
    private static Pos insertObstaclesOnLane(Pos lane, int column, List<Point> obstacles) {
        Pos other = getOtherLane(lane);

        obstacles.add(new Point( lane.ordinal(),column));
        obstacles.add(new Point(other.ordinal(), column));
        Log.d("Object", "insetObstaclesOnLane, lane: " + lane + " other: " + other);
        return getLastLane(lane, other);
    }

    private static Pos getOtherLane(Pos thisLane) throws ArrayIndexOutOfBoundsException {
        Random rand = new Random();
        Pos res;

        switch (thisLane) {
            case UPPER:
                res = rand.nextInt(2) == 1 ? Pos.MIDDLE : Pos.LOWER;
                break;
            case MIDDLE:
                res = rand.nextInt(2) == 1 ? Pos.UPPER : Pos.LOWER;
                break;
            case LOWER:
                res = rand.nextInt(2) == 1 ? Pos.UPPER : Pos.MIDDLE;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
        Log.d("Object", "thisLane: " + thisLane.toString() + " res: " + res.toString());
        return res;
    }
    /**
     * Finds and returns the remaining lane
     *
     * @param first  The first lane not to return
     * @param second The second lane not to return
     * @return
     */
    private static Pos getLastLane(Pos first, Pos second) {

        Pos res;
        int val = 7 ^ first.val ^ second.val;

        Log.d("Object", "getLastLane - " + first.val + " " + second.val + " val: " + val);

        switch (val) {
            case 4:
                res = Pos.LOWER;
                break;
            case 2:
                res = Pos.MIDDLE;
                break;
            case 1:
                res = Pos.UPPER;
                break;
            default:
                throw new IllegalArgumentException();
        }
        Log.d("Object", "res: " + res.toString());
        return res;
    }

    public static void path(int row, int column, int depth, Objects[][] roadObstacles, int[] garagesReached) {
        if (column == 0 && depth < 14) {
            recursivePath(row, column, depth, roadObstacles, garagesReached);
        } else if (column >= 6) {
            garagesReached[row] = 1;
        } else if (roadObstacles[row][column] == Objects.OBJECT || depth == 14) {
        } else {

            recursivePath(row, column, depth, roadObstacles, garagesReached);
        }
    }

    private static void recursivePath(int row, int column, int depth, Objects[][] roadObstacles, int[] garagesReached) {
        depth++;
        switch (row) {
            case 1:
                path(1, column + 1, depth, roadObstacles, garagesReached);
                path(2, column, depth, roadObstacles, garagesReached);
                break;
            case 2:
                path(1, column, depth, roadObstacles, garagesReached);
                path(2, column + 1, depth, roadObstacles, garagesReached);
                path(3, column, depth, roadObstacles, garagesReached);
                break;
            case 3:
                path(2, column, depth, roadObstacles, garagesReached);
                path(3, column + 1, depth, roadObstacles, garagesReached);
                break;
        }
    }
}