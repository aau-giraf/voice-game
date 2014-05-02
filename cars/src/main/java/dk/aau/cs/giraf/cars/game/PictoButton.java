package dk.aau.cs.giraf.cars.game;

import android.graphics.Rect;
import android.provider.ContactsContract;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.Drawable;

public class PictoButton implements Drawable {

    public int x, y, width, height;
    private Rect bounds;
    private Image image;

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
        graphics.drawScaledImage(image, x, y, width, height, 0, 0, image.getWidth(), image.getHeight());
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
