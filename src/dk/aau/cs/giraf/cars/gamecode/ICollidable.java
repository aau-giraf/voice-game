package dk.aau.cs.giraf.cars.gamecode;

import android.graphics.Point;

public interface ICollidable {
	public boolean collisionDetection();
	public Point[] calculateCollisionBox(); //ÆNDRE TIL PASSENDE FORM
}
