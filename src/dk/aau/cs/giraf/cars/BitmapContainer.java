package dk.aau.cs.giraf.cars;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class BitmapContainer {
	public static ArrayList<Integer> bitmapIds = new ArrayList<Integer>(); 
	public static ArrayList<Integer> resourceList = new ArrayList<Integer>();
	public static ArrayList<BitmapReference> recoloredList = new ArrayList<BitmapReference>();
	private static Resources resources;
	
	public static void add(int bitmapId) {
		bitmapIds.add(bitmapId);
		resourceList.add(bitmapId);
	}
	public static int add(int bitmapId, int newColor) {
		int largestId = 0x7f020011 + 99;
		for (Integer id : bitmapIds) {
			if (id > largestId) {
				largestId = id;
			}
		}
		
		int newBitmapId = largestId + 1;
		Bitmap newBitmap = recolorBitmap(bitmapId, newColor);
		bitmapIds.add(newBitmapId);
		recoloredList.add(new BitmapReference(newBitmapId, newBitmap));
		return newBitmapId;
	}
	private static Bitmap recolorBitmap(int bitmapId, int color) {
		int whiteValue = 0xFFFFFFFF;
		int recolorValue = color;
		
		Bitmap newBitmap = get(bitmapId).copy(Bitmap.Config.ARGB_8888, true);
		for (int x = 0; x < newBitmap.getWidth(); x++) {
			for (int y = 0; y < newBitmap.getHeight(); y++) {
				if (newBitmap.getPixel(x, y) == whiteValue) {
					newBitmap.setPixel(x, y, recolorValue);
				}
			}
		}
		
		return newBitmap;
	}
	
	public static Bitmap get(int bitmapId) {
		for (Integer id : resourceList) {
			if (id == bitmapId) {
				return BitmapFactory.decodeResource(resources, bitmapId);
			}
		}
		for (BitmapReference reference : recoloredList) {
			if (reference.id == bitmapId) {
				return reference.bitmap;
			}
		}
		
		System.out.println("Did not find bitmap");
		return null;
	}

	public static int[] getBitmapIds() {
		int[] arrayBitmapIds = new int[size()];
		
		int i = 0;
		for (Integer id : bitmapIds) {
			arrayBitmapIds[i] = id;
			i++;
		}
		
		return arrayBitmapIds;
	}
	
	public static int size() {
		return bitmapIds.size();
	}
	
	public static void setResources(Resources resources) {
		BitmapContainer.resources = resources; 
	}
	
	public static void clear() {
		bitmapIds.clear();
		resourceList.clear();
		recoloredList.clear();
	}
	
}
