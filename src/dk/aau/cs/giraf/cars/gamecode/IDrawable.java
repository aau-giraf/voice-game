package dk.aau.cs.giraf.cars.gamecode;

import javax.microedition.khronos.opengles.GL10;

public interface IDrawable {
	public void draw(GL10 gl, GameRenderer spriteBatcher);
}
