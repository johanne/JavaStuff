package com.google.challenges;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.tree.DefaultMutableTreeNode;

// we need to predict the angle

public class BeamFormer {

	
	public final class Tree{
		
		public ArrayList<Vector> childrenList;
		
		public void AddChild(BeamFormer.Vector v){
			
		}
	}
	
	// we'll need a tree here instead of a hash map
	public final class Vector implements Comparable<BeamFormer.Vector>{
		private double xDirection;
		private double yDirection;
		private double xOrigin;
		private double yOrigin;
		private double length;
		
		public Vector(double xOrigin, double yOrigin, double xDirection, double yDirection, double length)
		{
			this.xOrigin = xOrigin;
			this.yOrigin = yOrigin;
			this.xDirection = xDirection;
			this.yDirection = yDirection;
			this.length = length;
		}
		
		@Override
		public int compareTo(Vector e){
			return this == null ? e == null ? 0 : 1 : this.equals(e) ? 0 : 1;
		}
		
		@Override
		public boolean equals(Object o){
			return false;
		}
	}
	
	

	// for each insertion, we need to get the length from the end node
	
	
	 	static double referenceLength = 0; //getLength(newOrigin, endPoint);
		static double cndx = 0;
		static double cndy = 0;
		static double slope = 0;
		static double margin = .0000000000001;
		static double slopeRef = 0;
		public static int answer(int[] dimensions, int[] captain_position, int[] badguy_position, int distance) { 
			
			// this never changes
			final double[] endPoint = {badguy_position[0], badguy_position[1]};
			final double[] captainPos = {captain_position[0], captain_position[1]};
			int bigger = Math.max(dimensions[0], dimensions[1]);
			
			HashMap<Double, HashSet<Double>> endPoints = new HashMap<Double, HashSet<Double>>();
			HashMap<Double, Integer> slopes = new HashMap<>();
			
			
			
			
			// max iterations should be distance / direct distance
			double length = getLength(captainPos, endPoint);
			int maxIterations = (int)Math.ceil(distance/length);
			if(length > distance) // the direct distance is longer than available beam power
				return 0;

			// algorithm is to create lines again and again and keep on trying to intersect them
			int possiblePaths = 1; // account for the direct distance
			// get the slopes to the edges
			// generate different vectors that do not have the same slope.
			// store the slopes in a hash map
			// do four different iterations
			int i = 0;
			
			// no need to use end to end points;
			// it only makes sense to reflect our rays in one direction
			
			
			
			// set our captain_position to be the reference point
			
			//bigger = (bigger/10 == 0 ? bigger : bigger / 10);
			// generate our data
			int dataMap[][] = new int[bigger + 1][bigger + 1];
			int originMap[][] = new int[bigger + 1][bigger + 1];
			for (int x = 2; x <= bigger; x++){
				for(int y = x; y <= bigger; y++){
					/*
					if(x % y == 0 || y % x == 0){
						dataMap[x][y] = 1;
						dataMap[y][x] = 1;
					} // divisible
					else{
					*/
						int ref = x > y? y:x;
						for(int z = 2; z*z <= ref; z++){
							if(x%z == 0 && y%z==0){
								dataMap[x][y] = 1;
								dataMap[y][x] = 1;
								break;
							}
						}
					//}
				}
			}
			
			
			/*
			// print data map
			for(int y = 0; y <= bigger; y++){
				for (int x = 0; x <= bigger; x++){
					System.out.print(dataMap[x][y] + " ");
				}
				System.out.println("");
			}
			*/
			
			int skippedCount = 0;
			int slopeSkippedCount = 0;
			for(int x = -bigger; x <= bigger; x++){
				for (int y = -bigger; y <= bigger; y++){
					// disregard straight lines, they just bounce back to the origin 
					if (x == 0 || y == 0)
						continue;
					int iterations = 1;
				    if(dataMap[Math.abs(x)][Math.abs(y)]==1 ){//|| dataMap[Math.abs(y)][Math.abs(x)]== 1){
				    	skippedCount++;
				    	//System.out.println("Slope = " + (double)y/(double)x);
				    	continue;
				    }
					// count becomes 
					/*
				    slopeRef = (double)y/(double)x;
				    int count = 0;
				    int adder = x > 0? 1: -1;
				    if(slopes.containsKey(slopeRef)){
				    	count = slopes.get(slopeRef);
				    	if(count == 0 || count == adder){
				    		slopeSkippedCount++;
					    	//System.out.println(String.format("[%d, %d]", x, y));

				    		continue;
				    	}
				    		
				    }
				    slopes.put(slopeRef, count + adder);
					*/
					/*
					if(sl.contains(slopeRef)){
						index2 = sl.indexOf(slopeRef);
						count = counts[index2];
						
						if(count == 0 || count == adder){
							skippedCounter++;
							continue;
						}
					}
					
					
					sl.add(slopeRef);
					counts[index++] = count + adder;
					*/
					
					//if(slope > 1 || slope < 0)
	            	//   continue;
	                
					// check if the slope has already been used
					//if(slopes.contains(slope))
					//	continue; // no need for this beam
					double workingDistance = 0;
					double[] vector = {x, y};
					double[] currentOrigin = {captain_position[0], captain_position[1]};
					double[] newOrigin = getNewOrigin(dimensions, currentOrigin, vector);
					//referenceLength = getLength(currentOrigin, endPoint);
					cndx = currentOrigin[0] - endPoint[0];
					cndy = currentOrigin[1] - endPoint[1];


					referenceLength = Math.sqrt((cndx*cndx)+(cndy*cndy));
					while(iterations < maxIterations && workingDistance + referenceLength <= distance){
						// we have found the ray, do the necessary processing
						

						// try inlining everything
						cndx = currentOrigin[0] - newOrigin[0];
						cndy = currentOrigin[1] - newOrigin[1];
						slope = cndy / cndx;
						
						// our line can be set to the form of y = slope (x) + (y0 - slope(x0));
						double resultY = (slope * endPoint[0]) + (currentOrigin[1] - (slope*currentOrigin[0]));
						
						
						if(endPoint[1] - margin <= resultY && resultY <= endPoint[1] + margin){
							if(endPoints.containsKey(currentOrigin[0])){
								HashSet<Double> yValues = endPoints.get(currentOrigin[0]);
								if(!yValues.contains(currentOrigin[1])){
									yValues.add(currentOrigin[1]);
									endPoints.put(currentOrigin[0], yValues);
									possiblePaths += ((workingDistance += referenceLength) <= distance) ? 1 : 0;
									//System.out.println(String.format("[%f, %f]", currentOrigin[0], currentOrigin[1]));
								}
							}
							else{
								HashSet<Double> yValues = new HashSet<Double>();
								yValues.add(currentOrigin[1]);
								endPoints.put(currentOrigin[0], yValues);
								possiblePaths += ((workingDistance += referenceLength) <= distance) ? 1 : 0;
								//System.out.println(String.format("[%f, %f]", currentOrigin[0], currentOrigin[1]));

								//System.out.println(String.format("[%d, %d] - %f: count = %d", x, y, slope, slopes.get(slope)));
							}
							break;
						}
						referenceLength = Math.sqrt((cndx*cndx)+(cndy*cndy));
						
						workingDistance += referenceLength;
						
						// we now set our current to the new found origin
						currentOrigin = newOrigin;
						// get a new origin
						newOrigin = getNewOrigin(dimensions, currentOrigin, vector);
						if(isEdge(dimensions, newOrigin)){
							
							
							break;
						}
						// try inlining everything
						cndx = currentOrigin[0] - newOrigin[0];
						cndy = currentOrigin[1] - newOrigin[1];
						slope = cndy / cndx;
						
						// our line can be set to the form of y = slope (x) + (y0 - slope(x0));
						resultY = (slope * captainPos[0]) + (currentOrigin[1] - (slope*currentOrigin[0]));
						
						
						if(captainPos[1] - margin <= resultY && resultY <= captainPos[1] + margin){
							

							
							break;
						}
						
						cndx = currentOrigin[0] - endPoint[0];
						cndy = currentOrigin[1] - endPoint[1];


						referenceLength = Math.sqrt((cndx*cndx)+(cndy*cndy));
						
						iterations++;
					}
					
					
					
				}
			}
			
			return possiblePaths;
		}
		
		public static boolean isEdge(int[] dimensions, double origin[]) {
			return (origin == null) || (origin[0] == 0 && (origin[1] == 0 || origin[1] == dimensions[1])) ||
					(origin[0] == dimensions[0] && (origin[1] == 0 || origin[1] == dimensions[1]));
		}
		
		public static double[] getNewOrigin(int[] dimensions, double[] origin, double[] vector){
			// define the lines based on the origin
			
			
			
			// try to get the point of intersection that lies on the line
			
			// we need to define our line based on the origin and the vector
			// we set the origin as x0,y0, vector as x1, y1;
			
			// how to determine if the line is correct?
			
			//if(dx == 0 || dy == 0)
			//	return false;
			
			double slope = vector[1] / vector[0];
			double b = origin[1] - (slope*origin[0]);
			// our line can be set to the form of y = slope (x) + (y0 - slope(x0)); 
			// y = mx + b
			// check top
			// equation for horizontal line means y == 0 or y == dimension[1];
			// x of interest would be equal to
			// x = (y - b) / m
			double topX = (((double)dimensions[1]) - b) / slope;
			double bottomX = ((0) - b) / slope;
			
			// for vertical line, x== 0 or x == dimension[0]
			// y of interest would be equal to
			// y = mx + b
			double yLeft = (slope * (0)) + b;
			double yRight = (slope * (dimensions[0])) + b;

			
			if(vector[1] > 0 && 0<= topX && topX <= dimensions[0]){ // goes to top
				// change the vector here
				vector[1] *= -1.0;
				return new double[]{topX, dimensions[1]};
			}
			if(vector[1] < 0 && 0<= bottomX && bottomX <= dimensions[0]){ // goes to bottom
				vector[1] *= -1.0;
				return new double[]{bottomX, 0};
			}
			if(vector[0] < 0 && 0<= yLeft && yLeft <= dimensions[1]){ // goes to left
				vector[0] *= - 1;
				return new double[]{0, yLeft};
			}
			if(vector[0] > 0 &&  0<= yRight && yRight <= dimensions[1]){ // goes right
				vector[0] *= - 1;
				return new double[]{dimensions[0], yRight};
			}
			
			return null;
		}
		
		public static double getLength(double[] origin, double[] destination){
			return Math.sqrt(((destination[0] - origin[0])*(destination[0] - origin[0])) + ((destination[1] - origin[1])*(destination[1] - origin[1])));
		}
		
		public static boolean isPointOnSlope(double[] point, double[] origin, double[] destination, double margin){
			// We plug in the x value and check if the resulting y value is the same as the point
			double dx = destination[0] - origin[0];
			double dy = destination[1] - origin[1];
			
			//if(dx == 0 || dy == 0)
			//	return false;
			
			double slope = dy / dx;
			
			// our line can be set to the form of y = slope (x) + (y0 - slope(x0));
			double resultY = (slope * point[0]) + (origin[1] - (slope*origin[0]));
			
			return point[1] - margin <= resultY && resultY <= point[1] + margin;
			// alternatively
			// return point[1] == (slope * point[0]) + (origin[1] - (slope*origin[0]));
		}	
	}
