package dk.aau.cs.giraf.voicegame.Settings;

import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.voicegame.game.Car;
import dk.aau.cs.giraf.game_framework.Game;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Screen;
import dk.aau.cs.giraf.gui.GComponent;

public abstract class SettingsScreen extends Screen {
    private Car car;
    private int grassSize;
    private int gameWidth;
    private int gameHeight;

    public SettingsScreen(Game game) {
        this(game, 70);
    }

    public SettingsScreen(Game game, int grassSize) {
        this(game, grassSize, game.getWidth(), game.getHeight());
    }

    public SettingsScreen(Game game, int grassSize, int gameWidth, int gameHeight) {
        super(game);
        this.car = new Car(0, 0);
        this.grassSize = grassSize;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
    }

    public void setCarPosition(float x, float y) {
        car.x = x;
        car.y = y;
    }
    public void setCarY(float y) {
        car.y = y;
    }
    public void setCarX(float x) {
        car.x = x;
    }

    public void setCarXToCenter(){
        car.x = (gameWidth - car.width) / 2f;
    }
    public void setCarYToCenter(){
        car.y = (gameHeight - car.height) / 2f;
    }

    public float getCarX() {
        return car.x;
    }
    public float getCarY() {
        return car.y;
    }

    public float getCarWidth() {
        return car.width;
    }
    public float getCarHeight() {
        return car.height;
    }

    // Method for getting the dimensions of the screen
    public int getScreenWidth() { return gameWidth; }
    public int getScreenHeight() { return gameHeight; }

    public void setCarColor(int color) { car.setColor(color); }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.drawColor(GComponent.GetBackgroundColor());
        graphics.fillImageTexture(Assets.GetGrass(), 0, 0, gameWidth, gameHeight);
        graphics.fillImageTexture(Assets.GetTarmac(), 0, grassSize, gameWidth, gameHeight - grassSize * 2);

        for (int i = 0; i < gameWidth; i += 10) {
            graphics.drawImage(Assets.getBorder(), i, grassSize - 19, 0, 0, 10, 25);
            graphics.drawImage(Assets.getBorder(), i, gameHeight - grassSize - 6, 0, 25, 10, 25);
        }

        car.Draw(graphics, deltaTime);
    }
}
