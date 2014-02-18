package dk.aau.cs.giraf.cars.sound;

import java.util.Arrays;

import dk.aau.cs.giraf.cars.gamecode.GameInfo;

public class MicTestThread extends Thread {
	private boolean mRun = false;
	private int mTmpLowFreq = 0;
	private int mTmpHighFreq = 0;
	private SetupStates mTestType = SetupStates.Low;
	private int arrayIntervals = 50;
	private boolean restartHigh = false;
	private boolean restartLow = false;
	
	public MicTestThread() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run() {
		mRun = true;
		collectFrequencies();
	}
	
	public void setType(SetupStates testType) {
		mTestType = testType;
	}
	
	private void collectFrequencies() {
		while(mRun) {
			switch(mTestType) {
			case High: collectHighFreq();
				break;
			case Low: collectLowFreq();
				break;
			}
		}
	}
	
	public int getTmpHighFreq() {
		return mTmpHighFreq;
	}
	
	public int getTmpLowFreq() {
		return mTmpLowFreq;
	}
	
	private void collectHighFreq() {
		int firstDimension = (3400-50) / arrayIntervals + 1;
		int[] soundArray = new int[firstDimension];
		int tmpCurrFreq;
		int frequencyRange;
		
		while(mTestType == SetupStates.High) {
			if (restartHigh) {
				Arrays.fill(soundArray, 0);
				restartHigh = false;
			}
			
			tmpCurrFreq = GameInfo.getCurrFreq();
			
			if (tmpCurrFreq > 50) {
				frequencyRange = tmpCurrFreq / arrayIntervals;
			
				if (frequencyRange != 0) {
					soundArray[frequencyRange]++;
				}
			
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
			}
		}
		
		int highestValue = 0;
		for (int i = 1; i < firstDimension; i++) {
			if (soundArray[i] > soundArray[highestValue]) {
				highestValue = i;
			}
		}
		mTmpHighFreq = (highestValue + 1) * 50 - 100;
	}

	private void collectLowFreq() {
		int firstDimension = (3400-50) / arrayIntervals + 1;
		int[] soundArray = new int[firstDimension];
		int tmpCurrFreq;
		int frequencyRange;
		
		while(mTestType == SetupStates.Low) {
			if (restartLow) {
				Arrays.fill(soundArray, 0);
				restartLow = false;
			}
			
			tmpCurrFreq = GameInfo.getCurrFreq();
			
			if (tmpCurrFreq > 50) {
				frequencyRange = tmpCurrFreq / arrayIntervals;
			
				if (frequencyRange != 0) {
					soundArray[frequencyRange]++;
				}
			
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
			}
		}
		
		int highestValue = 0;
		for (int i = 1; i < firstDimension; i++) {
			if (soundArray[i] > soundArray[highestValue]) {
				highestValue = i;
			}
		}
		mTmpLowFreq = (highestValue + 1) * 50 + 100;
		
		System.out.println(mTmpLowFreq);
	}
	
	public void saveFrequencies() {
		GameInfo.setHighFreq(mTmpHighFreq);
		GameInfo.setLowFreq(mTmpLowFreq);
	}

	public void stopThread() {
		mRun = false;
	}
	
	public void restartLowFreq() {
		restartLow = true;
	}
	
	public void restartHighFreq() {
		restartHigh = true;
	}
	

}
