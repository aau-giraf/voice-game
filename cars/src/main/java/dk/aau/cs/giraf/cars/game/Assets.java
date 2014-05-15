package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Audio;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;
import dk.aau.cs.giraf.cars.framework.Sound;

public class Assets {
    private static boolean loaded = false;

    public static void LoadAssets(Graphics graphics, Audio audio) {
        if (loaded)
            return;

        grass = graphics.newImage(R.drawable.grass);
        tarmac = graphics.newImage(R.drawable.tarmac);
        border = graphics.newImage(R.drawable.border);
        car = graphics.newImage(R.drawable.ccar);
        obstacle = graphics.newImage(R.drawable.obstacle);
        star = graphics.newImage(R.drawable.star);
        trophy = graphics.newImage(R.drawable.trophy);
        explosion = graphics.newImage(R.drawable.explosion);
        pauseButton = graphics.newImage(R.drawable.pause);
        playButton = graphics.newImage(R.drawable.cplay);
        rabbit_picto = graphics.newImage(R.drawable.rabbit_picto);
        snail_picto = graphics.newImage(R.drawable.snail_picto);
        tiger_picto = graphics.newImage(R.drawable.tiger_picto);

        carStart = audio.createSound(R.raw.car_start);
        pickup = audio.createSound(R.raw.double_honk);
        done = audio.createSound(R.raw.faerdig);
        wellDone = audio.createSound(R.raw.godt_gaaet);
        newTurn = audio.createSound(R.raw.ny_tur);
        playAgain = audio.createSound(R.raw.spil_igen);
        crash = audio.createSound(R.raw.crash);

        loaded = true;
    }

    public static boolean GetLoaded() {
        return loaded;
    }

    private static Image grass, tarmac, border, car, obstacle, trophy, garage, explosion,
            pauseButton, playButton, rabbit_picto, snail_picto, tiger_picto, star;

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

    public static Image GetStar() {
        return star;
    }

    private static Sound carStart, pickup, done, wellDone, newTurn, playAgain, crash;

    public static Sound GetCarStart() {
        return carStart;
    }

    public static Sound GetPickup() {
        return pickup;
    }

    public static Sound GetDone() {
        return done;
    }

    public static Sound GetWellDone() {
        return wellDone;
    }

    public static Sound GetNewTurn() {
        return newTurn;
    }

    public static Sound GetPlayAgain() {
        return playAgain;
    }

    public static Sound GetCrash() { return crash; }
}
