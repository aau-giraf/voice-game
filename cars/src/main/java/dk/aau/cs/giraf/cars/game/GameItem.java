package dk.aau.cs.giraf.cars.game;

import android.graphics.Point;
import android.graphics.Rect;

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
    public Point GetCollisionCenter(GameItem item) {
        Rect collidee = new Rect((int)x, (int)y, (int)(x + width), (int)(y + height));
        Rect collider = new Rect((int)item.x, (int)item.y, (int)(item.x + item.width), (int)(item.y + item.height));

        int xL = Math.max(collidee.left, collider.left);
        int xR = Math.min(collidee.right, collider.right);
        int yT = Math.min(collidee.top, collider.top);
        int yB = Math.max(collidee.bottom, collider.bottom);

        Rect collision = new Rect(xL, yT, xR, yB);

        return new Point(collision.centerX(), collision.centerY());
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }


    public abstract void Update(float deltaTime);
}
