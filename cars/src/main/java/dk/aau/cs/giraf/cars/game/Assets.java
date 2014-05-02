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
        obstacle = graphics.newImage("obstacle.png", Graphics.ImageFormat.ARGB8888);
        trophy = graphics.newImage("trophy.png", Graphics.ImageFormat.ARGB8888);
        garage = graphics.newImage("garage.png", Graphics.ImageFormat.ARGB8888);
        explosion = graphics.newImage("explosion.png", Graphics.ImageFormat.ARGB8888);
        pauseButton = graphics.newImage("pauseButton.png", Graphics.ImageFormat.ARGB8888);
        playButton = graphics.newImage("playButton.png", Graphics.ImageFormat.ARGB8888);
        rabbit_picto = graphics.newImage("rabbit_picto.png", Graphics.ImageFormat.ARGB8888);
        snail_picto = graphics.newImage("snail_picto.png", Graphics.ImageFormat.ARGB8888);
        tiger_picto = graphics.newImage("tiger_picto.png", Graphics.ImageFormat.ARGB8888);


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
    private static Image explosion;
    private static Image pauseButton;
    private static Image playButton;
    private static Image rabbit_picto;
    private static Image snail_picto;
    private static Image tiger_picto;

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

    public static Image GetExplosion() {
        return explosion;
    }

    public static Image GetPauseButton() {
        return pauseButton;
    }

    public static Image GetPlayButton() {
        return playButton;
    }

    public static Image GetRabbitPicto() {
        return rabbit_picto;
    }

    public static Image GetSnailPicto() {
        return snail_picto;
    }

    public static Image GetTigerPicto() {
        return tiger_picto;
    }

}
