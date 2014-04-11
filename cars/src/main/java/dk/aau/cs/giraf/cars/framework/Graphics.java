package dk.aau.cs.giraf.cars.framework;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

public class Graphics {
    public static enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }

    private static final int DEFAULT_STROKE_WIDTH = 0;

    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    Graphics(AssetManager assets) {
        this.assets = assets;
        this.frameBuffer = null;
        this.canvas = null;
        this.paint = new Paint();
    }

    public Graphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    public Image newImage(String fileName, ImageFormat format) {
        Config config = null;
        if (format == ImageFormat.RGB565)
            config = Config.RGB_565;
        else if (format == ImageFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;


        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        format = getImageFormat(bitmap.getConfig());

        return new Image(bitmap, format);
    }

    public static Image recolorImage(Image image, int newColor) {
        int oldColor = Color.WHITE;
        float newR = (newColor >> 16) & 0xff;
        float newG = (newColor >> 8) & 0xff;
        float newB = (newColor) & 0xff;

        int[] pixels = new int[image.bitmap.getWidth() * image.bitmap.getHeight()];

        image.bitmap.getPixels(
                pixels,
                0,
                image.bitmap.getWidth(),
                0,
                0,
                image.bitmap.getWidth(),
                image.bitmap.getHeight()
        );

        for (int i = 0; i < image.bitmap.getWidth() * image.bitmap.getHeight(); i++){
            float gr = (pixels[i] & 0xff) / 255f;
            int r = (int)(gr * newR) << 16;
            int g = (int)(gr * newG) << 8;
            int b = (int)(gr * newB);
            pixels[i] = (pixels[i] & (0xff << 24)) | r | g | b;
        }

        Bitmap recoloredBitmap = Bitmap.createBitmap(
                pixels,
                image.bitmap.getWidth(),
                image.bitmap.getHeight(),
                image.bitmap.getConfig()
        );

        return new Image(recoloredBitmap, getImageFormat(recoloredBitmap.getConfig()));
    }

    private static ImageFormat getImageFormat(Config config) {
        if (config == Config.RGB_565)
            return ImageFormat.RGB565;
        else if (config == Config.ARGB_4444)
            return ImageFormat.ARGB4444;
        else
            return ImageFormat.ARGB8888;
    }

    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    public void drawLine(int x, int y, int x2, int y2, int color, float width) {
        paint.setColor(color);
        paint.setStrokeWidth(width);
        canvas.drawLine(x, y, x2, y2, paint);
        paint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    public void drawARGB(int a, int r, int g, int b) {
        paint.setStyle(Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

    public void drawString(String text, int x, int y, Paint paint) {
        canvas.drawText(text, x, y, paint);
    }


    public void drawImage(Image Image, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.drawBitmap(Image.bitmap, srcRect, dstRect, null);
    }

    public void drawImage(Image Image, int x, int y) {
        canvas.drawBitmap(Image.bitmap, x, y, null);
    }

    public void drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + width;
        dstRect.bottom = y + height;

        canvas.drawBitmap(Image.bitmap, srcRect, dstRect, null);
    }

    public void drawScaledImage(Image image, Rect dstRect, Rect srcRect) {
        canvas.drawBitmap(image.bitmap, srcRect, dstRect, null);
    }

    public void fillImageTexture(Image image, int x, int y, int width, int height) {
        int imgw = image.getWidth();
        int imgh = image.getHeight();

        Rect src = new Rect();
        Rect dst = new Rect();

        for (int h = 0; h < height; h += imgh) {
            src.bottom = imgh;
            if (h + imgh > height)
                src.bottom = height - h;

            dst.top = y + h;
            dst.bottom = dst.top + src.bottom;
            for (int w = 0; w < width; w += imgw) {
                src.right = imgw;
                if (w + imgw > width)
                    src.right = width - w;

                dst.left = x + w;
                dst.right = dst.left + src.right;
                canvas.drawBitmap(image.bitmap, src, dst, null);
            }
        }
    }


    public int getWidth() {
        return frameBuffer.getWidth();
    }

    public int getHeight() {
        return frameBuffer.getHeight();
    }
}