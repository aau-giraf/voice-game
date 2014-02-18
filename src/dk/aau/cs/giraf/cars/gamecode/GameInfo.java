package dk.aau.cs.giraf.cars.gamecode;

import android.graphics.Color;


//Static class containing game information
public class GameInfo {
	private static int mLowFreq = 500;
	private static int mHighFreq = 1500;
	private static int mCurrFreq = -1;
	public static float carSpeed = 0.75F;    // TODO Indsæt hastigheden på bilen her som en float
	public static int numberOfObstacles = 2; // TODO Indsæt nummer for objekter
	public static boolean garageClosing = false;
	public static boolean win = false;
	public static boolean pause = false;
	public static int color1 = Color.WHITE;
	public static int color2 = Color.WHITE;
	public static int color3 = Color.WHITE;
	
	private GameInfo() { /*empty */ }
	
	public static void setHighFreq(int highFreq) {
		mHighFreq = highFreq;
	}
	
	public static void setLowFreq(int lowFreq) {
		mLowFreq = lowFreq;
	}
	
	public static void setCurrFreq(int currFreq) {
		mCurrFreq = currFreq;
	}
	
	public static void setFrequencies(int lowFreq, int highFreq) {
		mLowFreq = lowFreq;
		mHighFreq = highFreq;
	}
	
	public static int getHighFreq() {
		return mHighFreq;
	}
	
	public static int getLowFreq() {
		return mLowFreq;
	}
	
	public static int getCurrFreq() {
		return mCurrFreq;
	}
	
}
