package dk.aau.cs.giraf.cars.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import dk.aau.cs.giraf.cars.framework.Game;

public class FastRenderView extends SurfaceView implements Runnable {
    Game game;
    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;

    public FastRenderView(Context context, Game game, Bitmap framebuffer) {
        super(context);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();

    }

    public void replaceBuffer(Bitmap framebuffer){
        //TODO this method should implement a thread-safe method of replacing the framebuffer field
        throw new UnsupportedOperationException("This method has not been implemented yet.");
    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();

    }

    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while (running) {
            if (!holder.getSurface().isValid())
                continue;


            // Calculate deltaTime as milliseconds
            float deltaTime = (System.nanoTime() - startTime) / 1000000.000f;
            startTime = System.nanoTime();


            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().paint(deltaTime);


            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);


        }
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }

        }
    }
}
