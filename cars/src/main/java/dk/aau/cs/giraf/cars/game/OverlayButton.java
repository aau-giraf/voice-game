package dk.aau.cs.giraf.cars.game;

public class OverlayButton {

    int TouchX, TouchY, TouchWidth, TouchHeight;
    int DrawX, DrawY;
    boolean Pressed;

    public OverlayButton(int touchX, int touchY, int touchWidth, int touchHeight, int drawX, int drawY) {
        this.TouchX = touchX;
        this.TouchY = touchY;
        this.TouchWidth = touchWidth;
        this.TouchHeight = touchHeight;

        this.DrawX = drawX;
        this.DrawY = drawY;

        this.Pressed = false;
    }
}
