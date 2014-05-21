package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Rect;
import android.provider.ContactsContract;

import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Image;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.Drawable;

public class PictoButton implements Drawable {

    public int x, y, width, height;
    private Rect bounds;
    private Image image;
    private final int BORDER_SIZE = 2;

    public PictoButton(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.bounds = new Rect(this.x, this.y, this.x + this.width, this.y + this.height);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        graphics.drawRect(x, y, width, height, Color.BLACK);
        graphics.drawScaledImage(image, x + BORDER_SIZE, y + BORDER_SIZE,
                width - 1 - 2 * BORDER_SIZE, height - 1 - 2 * BORDER_SIZE,
                0, 0, image.getWidth(), image.getHeight());
    }

    public boolean IsPressed(Input.TouchEvent touchEvents[], float deltaTime) {
        for (int i = 0; i < touchEvents.length; i++) {
            Input.TouchEvent event = touchEvents[i];

            if (event.inBounds(bounds)) {
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    return true;
                }
            }
        }
        return false;
    }
}
