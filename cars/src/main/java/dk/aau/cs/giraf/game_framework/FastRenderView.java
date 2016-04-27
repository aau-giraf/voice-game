package dk.aau.cs.giraf.game_framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.List;

public class FastRenderView extends SurfaceView implements Runnable {
    Game game;
    private Bitmap framebuffer;
    private Thread renderThread = null;
    private Graphics graphics;
    private Input input;
    private SurfaceHolder holder;
    volatile boolean running = false;
    private int skipFrames = 2;

    private Bitmap screenshot;
    private boolean screenshotTaken = true;

    public FastRenderView(Context context, Game game, Bitmap framebuffer, float scaleX, float scaleY) {
        super(context);
        this.game = game;
        this.framebuffer = framebuffer;
        this.graphics = this.framebuffer == null ?
                new Graphics(context.getAssets()) :
                new Graphics(context.getAssets(), framebuffer);
        this.input = new Input(context, this, scaleX, scaleY);
        this.holder = getHolder();

        WindowManager wm =  (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenshot = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.RGB_565);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (graphics.frameBuffer != null)
            return;
        // Pause rendering
        pause();

        // Create new bitmap for rendering
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        game.getMessenger().setSize(bitmap.getWidth(), bitmap.getHeight());
        // Set the bitmap in Graphics and create a new canvas for it
        graphics.canvas = new Canvas(bitmap);
        graphics.frameBuffer = bitmap;

        // Update the bitmap reference in this FastRenderView
        if (this.framebuffer != null)
            this.framebuffer.recycle();
        this.framebuffer = bitmap;

        // Resume rendering
        resume();
    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();

    }

    // the game loop, contains an update and a draw method call
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while (running) {
            if (!holder.getSurface().isValid())
                continue;

            if(skipFrames > 0)
            {
                skipFrames--;
                startTime = System.nanoTime();
            }

            // Calculate deltaTime as milliseconds
            float deltaTime = (System.nanoTime() - startTime) / 1000000.000f;
            startTime = System.nanoTime();

            List<Input.TouchEvent> events = input.getTouchEvents();
            Input.TouchEvent[] array = new Input.TouchEvent[events.size()];
            events.toArray(array);

            if (graphics != null && graphics.frameBuffer != null) {

                // update and paint methods here.
                game.getCurrentScreen().update(array, deltaTime);
                game.getCurrentScreen().paint(graphics, deltaTime);

                // this parts only runs when the user requests a screenshot
                if(!screenshotTaken) {

                    Canvas tempCanvas = new Canvas(screenshot);
                    Canvas originialCanvas = graphics.canvas;

                    graphics.canvas = tempCanvas;

                    graphics.frameBuffer = screenshot;
                    game.getCurrentScreen().paint(graphics, deltaTime);



                    tempCanvas.getClipBounds(dstRect);

                    tempCanvas.drawBitmap(screenshot, null, dstRect, null);


                    graphics.frameBuffer = framebuffer;
                    graphics.canvas = originialCanvas;
                    screenshotTaken = true;
                }

                Canvas canvas = holder.lockCanvas();
                canvas.getClipBounds(dstRect);
                canvas.drawBitmap(framebuffer, null, dstRect, null);
                holder.unlockCanvasAndPost(canvas);
            }
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

    public Bitmap getScreenshot() {
        // user requests a screenshot here
        screenshotTaken = false;

        // busy wait loop while we wait for the next frame to be drawn
        while(!screenshotTaken) {
        }

        return screenshot;
    }
}
