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
    private Bitmap framebuffer;
    private Thread renderThread = null;
    private Graphics graphics;
    private SurfaceHolder holder;
    volatile boolean running = false;

    public FastRenderView(Context context, Game game, Bitmap framebuffer) {
        super(context);
        this.game = game;
        this.framebuffer = framebuffer;
        this.graphics = new Graphics(context.getAssets(), framebuffer);
        this.holder = getHolder();
    }

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


            if (game instanceof GameActivity)
                ((GameActivity) game).setTouchEvents();
            else if (game instanceof GameFragment)
                ((GameFragment) game).setTouchEvents();

            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().paint(graphics, deltaTime);


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
