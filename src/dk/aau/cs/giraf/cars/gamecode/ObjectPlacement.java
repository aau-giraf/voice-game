package dk.aau.cs.giraf.cars.gamecode;

import java.util.Random;

public class ObjectPlacement {

	// 0 = ingen objekt 1 = ikke nåbart område  2 = objekt
	public static int[][] objectPlacement(int numberOfObjects){
		int i, j;
		boolean availablePathToEnd = false;

		int[][] roadObstacles;
		roadObstacles = new int [5][6];

		int[][] ObstacleArray;
		ObstacleArray = new int [numberOfObjects][2];


		while (availablePathToEnd==false){
			Random rand = new Random();
			int randomRow = 0;
			int randomColoum= 0;
			for (i=0;i<5;i++){
				for (j=0;j<6;j++){
					roadObstacles[i][j] = 0;
				}
			}


			for (i=0;i<numberOfObjects;i++){

				boolean collision = true;

				while(collision == true){
					collision = false;
					randomRow = Math.abs((rand.nextInt()%3)) + 1;
					randomColoum = Math.abs((rand.nextInt()%4)) + 1;
					for (j=0;j<i;j++)
						if (ObstacleArray[j][0] == randomRow && ObstacleArray[j][1] == randomColoum ){
							collision = true;
						}
				}

				ObstacleArray[i][0] = randomRow;
				ObstacleArray[i][1] = randomColoum;
				roadObstacles[randomRow][randomColoum] = 2;
			}


			//find dead ends
			int numberOfGarage= 3;
			numberOfGarage= numberOfGarage+1;
			int[] garageBool = new int [numberOfGarage];
			path(2, 0, 0, roadObstacles, garageBool);

			int GarageGoal = 0;

			for (i=1;i<numberOfGarage;i++){
				if(garageBool[i] == 1){
					GarageGoal = GarageGoal + garageBool[i];
				}
			}

			if(GarageGoal >= (numberOfGarage-1)){
				availablePathToEnd = true;
			}
		}

		return ObstacleArray;
	}
	public static void path(int row, int column, int depth , int[][] roadObstacles, int[] garagesReached){
		//	System.out.println("Column = " + column);
		if (column == 0 && depth <14) {
			recursivePath(row, column, depth, roadObstacles, garagesReached);
		} else if (column >= 6) {
			garagesReached[row] = 1;
		} else if(roadObstacles[row][column] == 2 || depth == 14) {
		} else {

			recursivePath(row, column, depth, roadObstacles, garagesReached);
		}
	}
	private static void recursivePath(int row, int column, int depth, int[][] roadObstacles, int[] garagesReached) {
		//	System.out.println("recursivePath");
		depth++;
		switch(row) {
		case 1:
			path(1, column + 1, depth, roadObstacles, garagesReached);
			path(2, column, depth, roadObstacles, garagesReached);
			break;
		case 2:
			path(1, column, depth, roadObstacles, garagesReached);
			path(2, column + 1, depth, roadObstacles, garagesReached);
			path(3, column, depth, roadObstacles, garagesReached);
			break;
		case 3:
			path(2, column, depth, roadObstacles, garagesReached);
			path(3, column + 1, depth, roadObstacles, garagesReached);
			break;
		}
	}
}