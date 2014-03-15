package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Graphics;

public abstract class GameItem {
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
        return (this.x + this.width < item.x || item.x + item.width < this.x || this.y + this.height < item.y || this.y + this.height < item.y);
    }

    public abstract void Paint(Graphics graphics, float deltaTime);

    public abstract void Update(float deltaTime);
}
