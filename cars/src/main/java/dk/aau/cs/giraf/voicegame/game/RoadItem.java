package dk.aau.cs.giraf.voicegame.game;

import android.graphics.Paint;
import android.graphics.Rect;

import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Image;
import dk.aau.cs.giraf.game_framework.Input;
// inheritence order: GameObject -> GameItem -> RoadItem -> Obstacle
public class RoadItem extends GameItem {
    private Paint paint;
    private int value; // the number
    private GameMode gameMode;

    //TODO test code, can be removed
    private int id;

    public RoadItem(float x, float y, float width, float height, int id) {
        super(x, y, width, height);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(46);
        paint.setTextAlign(Paint.Align.CENTER);

        // The magic value below is the height of the game 800 minus the total height of the grass 2x70
        // TODO hard coded value for the screen size! This needs to be changed.
        setBarometerNumber();

        this.id = id;
    }

    public RoadItem(float x, float y, float width, float height, GameMode gameMode, int id) {
        this(x,y,width,height, id);
        this.gameMode = gameMode;
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        if (gameMode == GameMode.pickup)
            drawRoadItem(graphics, Assets.GetStar());
        if (gameMode == GameMode.avoid)
            drawRoadItem(graphics, Assets.GetObstacle());
    }

    protected void drawRoadItem(Graphics graphics, Image image) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(image,
                bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                0, 0, image.getWidth(), image.getHeight());
        graphics.drawString(String.valueOf(value), bounds.centerX(), bounds.centerY() + 17, paint);
    }

    private int getBarometerNumber(float y, float height) {
        return Math.round(10 - (y / (height / 10)));
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        // TODO this can be removed
        System.out.print("RoadItem id: " + id);
    }

    // Method that can be called to update the number written on the obstacle
    public void setBarometerNumber() {
        value = getBarometerNumber(GetBounds().centerY() - 100, 600);
    }
}

