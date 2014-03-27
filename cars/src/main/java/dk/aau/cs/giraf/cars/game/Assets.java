package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;

public class Assets {
    private static boolean loaded = false;

    public static void LoadAssets(Graphics graphics) {
        grass = graphics.newImage("grass.jpg", Graphics.ImageFormat.RGB565);
        tarmac = graphics.newImage("tarmac.jpg", Graphics.ImageFormat.RGB565);
        border = graphics.newImage("border.png", Graphics.ImageFormat.ARGB8888);
        car = graphics.newImage("car.png", Graphics.ImageFormat.ARGB8888);
        obstacle = graphics.newImage("obstacle.jpg", Graphics.ImageFormat.RGB565);
        garage = graphics.newImage("garage.jpg", Graphics.ImageFormat.RGB565);
        trophy = graphics.newImage("pokal.gif", Graphics.ImageFormat.ARGB8888);
        garage_open =  graphics.newImage("garage_open.png", Graphics.ImageFormat.ARGB8888);
        garage_closing_1 =  graphics.newImage("garage_closing1.png", Graphics.ImageFormat.ARGB8888);
        garage_closing_2 =  graphics.newImage("garage_closing2", Graphics.ImageFormat.ARGB8888);
        garage_closed =  graphics.newImage("garage_closed.png", Graphics.ImageFormat.ARGB8888);

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
    private static Image garage;
    private static Image trophy;
    private static Image garage_open;
    private static Image garage_closing_1;
    private static Image garage_closing_2;
    private static Image garage_closed;

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

    public static Image getGarage() {
        return garage;
    }

    public static Image GetTrophy(){return trophy;}

    public static Image GetGarageOpen() {return garage_open;}
    public static Image GetGarageClosing1() {return garage_closing_1;}
    public static Image GetGarageClosing2() {return garage_closing_2;}
    public static Image GetGarageClosed() {return garage_closed;}
}
