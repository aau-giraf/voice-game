package dk.aau.cs.giraf.cars.gamecode;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import dk.aau.cs.giraf.cars.GameActivity;
import dk.aau.cs.giraf.cars.R;

@SuppressLint("ViewConstructor")
public class GameView extends GLSurfaceView implements Drawer {
	List<IDrawable> drawableObjects;
	GameActivity parent;
	private boolean mSettingsView = false;
	
	public GameView(Context context, Resources resources) {
		super(context);
		parent = (GameActivity) context;
		
		drawableObjects = new ArrayList<IDrawable>();
		setRenderer(new GameRenderer(resources, this));
	}
	
	public GameView(Context context, Resources resources, boolean settingsView) {
		super(context);
		mSettingsView = settingsView;
		drawableObjects = new ArrayList<IDrawable>();
		setRenderer(new GameRenderer(resources, this));
	}

	public void SetObjects(List<GameObject> gameObjects) {
		drawableObjects.clear();

		for (GameObject object : gameObjects) {
			if (object instanceof IDrawable) {
				drawableObjects.add((IDrawable) object);
			}
		}
	}


	public void SetObjects(GameObject gameObject) {
		drawableObjects.clear();

		if (gameObject instanceof IDrawable) {
			drawableObjects.add((IDrawable) gameObject);
		}

	}

	@Override
	public void onDrawFrame(GL10 gl, GameRenderer spriteBatcher) {
		//spriteBatcher.draw(gl, R.drawable.ic_launcher, new Rect(0, 0, 100, 100), new Rect(150, 150, 250, 250));
		if (!mSettingsView) {
			spriteBatcher.draw(gl, R.drawable.map, new Rect(0, 0, 1947, 1122), new Rect(0, MapDivider.mapYStart, spriteBatcher.getViewWidth(), MapDivider.mapYEnd));
			spriteBatcher.draw(gl, R.drawable.grass, new Rect(0, 0, 100, 100), new Rect(0, 0, spriteBatcher.getViewWidth(), MapDivider.mapYStart));
			spriteBatcher.draw(gl, R.drawable.grass, new Rect(0, 0, 100, 100), new Rect(0, MapDivider.mapYEnd, spriteBatcher.getViewWidth(), spriteBatcher.getViewHeight()));
			if (GameInfo.win){
				spriteBatcher.draw(gl, R.drawable.trophy, new Rect(0, 0, 1090, 951), new Rect((int)(MapDivider.obstacleWidth*3), MapDivider.mapYStart+(int)(MapDivider.obstacleHeight*0.25), (int)(MapDivider.obstacleWidth*3)+(int)(MapDivider.obstacleWidth*2), MapDivider.mapYEnd-(int)(MapDivider.obstacleHeight*0.25)));
			}
		} else {
			gl.glClearColor(255, 255, 255, 0);
		}



		for (IDrawable object : drawableObjects) {
			object.draw(gl, spriteBatcher);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		if(!mSettingsView) {
			MapDivider.CalculateConstants(this);
			parent.AddObjects();
		}
	}
}
