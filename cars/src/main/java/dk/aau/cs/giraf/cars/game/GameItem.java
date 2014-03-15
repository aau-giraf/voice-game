package dk.aau.cs.giraf.cars.game;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;

public abstract class GameItem {
    private Rect bounds;

    public GameItem(Rect bounds) {
        this.bounds = bounds;
    }

    public Rect GetBounds() {
        return bounds;
    }

    public void SetPosition(int x, int y) {
        this.bounds.right += (x - this.bounds.left);
        this.bounds.bottom += (y - this.bounds.top);
        this.bounds.left = x;
        this.bounds.top = y;
    }

    public void SetSize(int w, int h) {
        this.bounds.right = this.bounds.left + w;
        this.bounds.bottom = this.bounds.top + h;
    }

    public boolean CollidesWith(GameItem item) {
        return this.bounds.intersect(item.bounds);
    }

    public abstract void Paint(Graphics graphics, float deltaTime);

    public abstract void Update(float deltaTime);
}
