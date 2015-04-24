package dk.aau.cs.giraf.game_framework;

public abstract class Screen {
    protected final Game game;

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
}