package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;

public class Assets {
    private static boolean loaded = false;

    public static void LoadAssets(Graphics graphics) {
        if (loaded)
            return;

        grass = graphics.newImage("grass.jpg", Graphics.ImageFormat.RGB565);
        tarmac = graphics.newImage("tarmac.jpg", Graphics.ImageFormat.RGB565);
        border = graphics.newImage("border.png", Graphics.ImageFormat.ARGB8888);
        car = graphics.newImage("car.png", Graphics.ImageFormat.ARGB8888);
        obstacle = graphics.newImage("obstacle.jpg", Graphics.ImageFormat.RGB565);
        trophy = graphics.newImage("pokal.gif", Graphics.ImageFormat.ARGB8888);
        garage = graphics.newImage("garage.png", Graphics.ImageFormat.ARGB8888);

        loaded = true;
    }

    public static boolean GetLoaded() {
        return loaded;
    }

    private static Image grass;
    private static Image tarmac;
    private static Image border;
    private static Image car;
    private static Image obstacle;
    private static Image trophy;
    private static Image garage;

    public static Image GetGrass() {
        return grass;
    }

    public static Image GetTarmac() {
        return tarmac;
    }

    public static Image getBorder() {
        return border;
    }

    public static Image GetCar() {
        return car;
    }

    public static Image GetObstacle() {
        return obstacle;
    }

    public static Image GetGarage() {
        return garage;
    }

    public static Image GetTrophy() {
        return trophy;
    }
}
