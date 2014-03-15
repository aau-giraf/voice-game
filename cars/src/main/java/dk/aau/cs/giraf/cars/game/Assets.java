package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;

public class Assets {
    private static boolean loaded = false;

    public static void LoadAssets(Graphics graphics) {
        Assets.grass = graphics.newImage("grass.jpg", Graphics.ImageFormat.RGB565);
        Assets.tarmac = graphics.newImage("tarmac.jpg", Graphics.ImageFormat.RGB565);

        loaded = true;
    }

    public static boolean GetLoaded() {
        return loaded;
    }

    private static Image grass;
    private static Image tarmac;

    public static Image GetGrass() {
        return grass;
    }

    public static Image GetTarmac() {
        return tarmac;
    }
}
