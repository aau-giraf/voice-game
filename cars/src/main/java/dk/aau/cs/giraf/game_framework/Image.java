package dk.aau.cs.giraf.game_framework;

import android.graphics.Bitmap;

import dk.aau.cs.giraf.game_framework.Graphics.ImageFormat;

public class Image {
    Bitmap bitmap;
    ImageFormat format;

    public Image(Bitmap bitmap, ImageFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public ImageFormat getFormat() {
        return format;
    }

    public void dispose() {
        bitmap.recycle();
    }
}
