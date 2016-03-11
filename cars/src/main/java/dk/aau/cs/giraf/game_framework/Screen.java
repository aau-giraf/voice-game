package dk.aau.cs.giraf.game_framework;

import dk.aau.cs.giraf.voicegame.Track;

public abstract class Screen {
    protected final Game game;
    //Used for saving track. Not ideal solution
    //TODO: Find alternative solution
    protected static Track track = null;
    public Screen(Game game) {
        this.game = game;
    }

    public abstract void update(Input.TouchEvent[] touchEvents, float deltaTime);

    public abstract void paint(Graphics graphics, float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();

    public abstract void backButton();

    public void showScreen(){
    }
    public void hideScreen(){
    }

    public void setTrack(Track track){
        this.track = track;
    }

    public static Track getTrack() {
        return track;
    }
}