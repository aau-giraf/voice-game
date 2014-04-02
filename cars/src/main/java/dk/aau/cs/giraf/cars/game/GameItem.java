package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.game.Interfaces.GameObject;

public abstract class GameItem implements GameObject {
    float x, y;
    final float width, height;

    public GameItem(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public android.graphics.Rect GetBounds() {
        return new android.graphics.Rect((int) x, (int) y, (int) (x + width), (int) (y + height));
    }

    public boolean CollidesWith(GameItem item) {
        return this.x < item.x + item.width && this.x + this.width > item.x &&
               this.y < item.y + item.height && this.y + this.height > item.y;
    }

    public abstract void Draw(Graphics graphics, float deltaTime);

    public abstract void Update(float deltaTime);
}
