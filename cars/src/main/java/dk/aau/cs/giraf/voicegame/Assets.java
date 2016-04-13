package dk.aau.cs.giraf.voicegame;

import dk.aau.cs.giraf.game_framework.Audio;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Image;
import dk.aau.cs.giraf.game_framework.Sound;

public class Assets {
    private static boolean loaded = false;

    public static void LoadAssets(Graphics graphics, Audio audio) {
        if (loaded)
            return;

        grass = graphics.newImage("grass.jpg", Graphics.ImageFormat.RGB565);
        tarmac = graphics.newImage("tarmac.jpg", Graphics.ImageFormat.RGB565);
        border = graphics.newImage("border.png", Graphics.ImageFormat.ARGB8888);
        car = graphics.newImage("car.png", Graphics.ImageFormat.ARGB8888);
        obstacle = graphics.newImage("obstacle.png", Graphics.ImageFormat.ARGB8888);
        star = graphics.newImage("star.png", Graphics.ImageFormat.ARGB8888);
        trophy = graphics.newImage("trophy.png", Graphics.ImageFormat.ARGB8888);
        explosion = graphics.newImage("explosion.png", Graphics.ImageFormat.ARGB8888);
        pauseButton = graphics.newImage("pauseButton.png", Graphics.ImageFormat.ARGB8888);
        playButton = graphics.newImage("playButton.png", Graphics.ImageFormat.ARGB8888);
        rabbit_picto = graphics.newImage("rabbit_picto.png", Graphics.ImageFormat.ARGB8888);
        snail_picto = graphics.newImage("snail_picto.png", Graphics.ImageFormat.ARGB8888);
        tiger_picto = graphics.newImage("tiger_picto.png", Graphics.ImageFormat.ARGB8888);

        carStart = audio.createSound(R.raw.car_start);
        pickup = audio.createSound(R.raw.double_honk);
        wellDone = audio.createSound(R.raw.vg_godt_gaaet);
        newTurn = audio.createSound(R.raw.vg_ny_tur);
        playAgain = audio.createSound(R.raw.vg_spil_igen);
        crash = audio.createSound(R.raw.crash);
        settings = audio.createSound(R.raw.vg_indstillinger);
        editTrack = audio.createSound(R.raw.vg_redig_bane);
        back = audio.createSound(R.raw.vg_tilbage);
        startGame = audio.createSound(R.raw.vg_start_spil);

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

    private static Sound carStart, pickup, done, wellDone, newTurn, playAgain, crash, settings, editTrack, startGame, back;

    public static Sound GetCarStart() {
        return carStart;
    }

    public static Sound GetPickup() {
        return pickup;
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

    public static Sound GetSettings() { return settings; }

    public static Sound GetStartGame() { return startGame; }

    public static Sound GetEditTrack() { return editTrack; }

    public static Sound GetBack() { return back; }
}
