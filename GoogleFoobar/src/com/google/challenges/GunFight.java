package com.google.challenges;

import java.util.LinkedList;

public class GunFight {
	

	// this involves lattice paths
	// the algorithm goes like
	// generate paths from the original position. set flipabble to false
	// each path can never change direction, unless from a wall
	
	// let's just do bfs again
	// and dp dynamic programming as well
	class Point
	{
		public int x;
		public int y;
		public int dx;
		public int dy;
	}
	
	
	
    public static int answer(int[] dimensions, int[] captain_position, int[] badguy_position, int distance) { 
    	
    	// generate a vector to hit the walls
    	// upon hitting a wall, change the sign of the vector
    	// the slope is the rise / run, as well as the x-intercept == length of side
    	// hit a wall using the vector signs
    	// generate the opposite using the tangent
    	// get the tangent to the destination, if same as current tangent, terminate
    	// get the length traveled. check if already exceeds.
    	// reverse the tangent value. repeat from the hit.
    	
    	int possiblePaths = 0;
    	
    	// need only to generate the possible angles, corresponding distances have been considered by getting the pythagorean
    	
    	// traversal of y-axis, left side
    	possiblePaths += CountPaths(captain_position, badguy_position, distance, 0, 0, 0, dimensions[1]);
    	// traversal of y-axis, right side
    	possiblePaths += CountPaths(captain_position, badguy_position, distance, dimensions[0], dimensions[0], 0, dimensions[1]);
    	//	traversal of x-axis, top side
    	possiblePaths += CountPaths(captain_position, badguy_position, distance, 1, dimensions[0] - 1, 0, 0);
    	// traversal of x-axis, bottom side
    	possiblePaths += CountPaths(captain_position, badguy_position, distance, 1, dimensions[0] - 1, dimensions[1], dimensions[1]);

    	
    	// generate the node that intersects with the generic path, if no such exist, there's a direct path
    	if((captain_position[0] == badguy_position[0] || captain_position[1] == badguy_position[1]) && GetDistance(captain_position[0] - badguy_position[0], captain_position[1] - badguy_position[1]) < distance)
    		possiblePaths++;
    	
    	return possiblePaths;
    } 
    
    public static int CountPaths(int[] captain_position, int[] badguy_position, int distance, int x, int endX, int y, int endY){
    	int x_badguy = 0;
    	int x_captain = 0;
    	int y_badguy = 0;
    	int y_captain = 0;
    	int pathCount = 0;
    	

    	// this will generate all the possible paths
    	for(int j = x; j<=endX; j++){
    		if(j==captain_position[0])
    			continue;
    		for(int i = y; i<=endY; i++){
    			if(i==captain_position[1])
    				continue;
    			// get the x and y distances of each path
    			x_badguy = Math.abs(badguy_position[0] - j);
    			x_captain = Math.abs(captain_position[0] - j);
    			y_badguy = Math.abs(badguy_position[1] - i);
    			y_captain = Math.abs(captain_position[1] - i);
    			
    			/*
    			if(GetDistance(x_captain, y_captain) +  GetDistance(x_badguy, y_badguy)<= distance){
    				pathCount++;
    			}
    			*/
    			// generate the angle and compute the two sides?
    			
    			// get the angles of incidences as well
    			if(GetDistance(x_captain + x_badguy, y_captain + y_badguy) <= distance){
    				pathCount++;
    			}
    		}
    	}
    	
    	return pathCount;
    }
    
    public static double GetDistance(int x, int y)
    {
    	return Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));
    }
    
    public static boolean IsReflectionPossible(int xFrame, int yFrame, int xSource, int xDestination, int ySource, int yDestination){
    	boolean retVal = false;
    	
    	// this would generate angles of reflection, and compute the distance traveled
    	
    	return retVal;
    }
}
