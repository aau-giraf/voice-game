package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;

public class Assets {
    private static boolean loaded = false;

    public static void LoadAssets(Graphics graphics) {
        grass = graphics.newImage("grass.jpg", Graphics.ImageFormat.RGB565);
        tarmac = graphics.newImage("tarmac.jpg", Graphics.ImageFormat.RGB565);
        car = graphics.newImage("car.png", Graphics.ImageFormat.ARGB8888);
        obstacle = graphics.newImage("obstacle.jpg", Graphics.ImageFormat.RGB565);

        loaded = true;
    }

    public static boolean GetLoaded() {
        return loaded;
    }

    private static Image grass;
    private static Image tarmac;
    private static Image car;
    private static Image obstacle;

    public static Image GetGrass() {
        return grass;
    }

    public static Image GetTarmac() {
        return tarmac;
    }

    public static Image GetCar() {
        return car;
    }

    public static Image GetObstacle() {
        return obstacle;
    }
}
