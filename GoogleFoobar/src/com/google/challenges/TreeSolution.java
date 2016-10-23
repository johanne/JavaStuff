package com.google.challenges;

import java.util.HashSet;
import java.util.LinkedList;

public class TreeSolution {

	// bst
	public class SimpleTree {
		
		Node left;
		Node right;
		// median would depend on the location
		public Node insert(Node root, double[] key, double value){
			if(root == null)
				root = new Node(key, value);
			else if (key[0] <root.location[0] || key[1] < root.location[1])
			{
				root.leftChild = insert(root.leftChild, key, value);
			}
			else if(key[0] > root.location[0] || key[1] < root.location[1])
			{
				// go right
				root.rightChild = insert(root.rightChild, key, value);
			}
			return root;
		}
		
		public Node GetNode(Node root, double[] key){
			if(root == null)
				return null;
			
			if(key[0] == root.location[0] && key[1] == root.location[1])
				return root;
			if (key[0] <root.location[0] || key[1] < root.location[1])
			{
				return GetNode(root.leftChild, key);
			}
			 else if(key[0] > root.location[0] || key[1] < root.location[1])
			{
				// go right
				return GetNode(root.rightChild, key);
			}
			 
			 return null;
			 
		}
		// return the length from the key to the value
		public double GetLength(Node root){
			if(root == null)
				return 0.0;
			double retVal = 0.0;
			retVal += GetLength(root.leftChild);
			retVal += GetLength(root.rightChild);
			return root.distance + retVal;
		}
		
		public double GetLength(Node root, double[] key){
			if(root == null)
				return 0.0;
			
			if(key[0] == root.location[0] && key[1] == root.location[1]) // found!
				return GetLength(root);
			
			if(key[0] < root.location[0]){
				return GetLength(root.leftChild, key);
			}
			else{
				return GetLength(root.rightChild, key);
			}
		}
		public SimpleTree(){
			
		}
	}

	
	// key is the origin
	// value is the destination
	public final class Node{
		
		double[] location; // this is the node's origin
		double distance; // distance to endpoint
		Node leftChild;
		Node rightChild;
		
		public Node(double[] key, double distance){
			this.location = new double[]{key[0], key[1]};
			this.distance = distance;
		}
		
		
	}
	
	static double referenceLength = 0; //getLength(newOrigin, endPoint);
	static double cndx = 0;
	static double cndy = 0;
	static double slope = 0;
	static double margin = .0000000000001;
	
    public static int answer(int[] dimensions, int[] captain_position, int[] badguy_position, int distance) { 
    	TreeSolution instance = new TreeSolution();
    	
    	int possiblePaths = 0;
		double[] endPoint = {badguy_position[0], badguy_position[1]};
		double[] captainPos = {captain_position[0], captain_position[1]};
		int bigger = Math.max(dimensions[0], dimensions[1]);
		// generate different vectors that do not have the same slope.
		// store the slopes in a hash map
		// do four different iterations
		
		
		SimpleTree tree = instance.new SimpleTree();
    	Node n = instance.new Node(new double[]{-1.0, -1.0}, 100000.0);
		for(int x = -bigger; x <= bigger; x++){
			for (int y = -bigger; y <= bigger; y++){
				
				if(x==1 && y==2)
					System.out.println("Test");
				if(x==0 || y==0)
					continue;
				// disregard straight lines, they just bounce back to the origin 
				possiblePaths += rayTrace(dimensions, captainPos, new double[] {x,y},endPoint, distance, tree, n);
			}
		}    	
    	return possiblePaths;
    }
    
    public static int rayTrace(int[] dimensions, double[] originP, double[] vectorP, double[] endPoint, int distance, SimpleTree tree, Node n){
    	if(dimensions == null || originP == null || vectorP == null)
    		return 0;
    	
		double workingDistance = 0;
		double tempDistance = 0;
    	double[] origin = new double[]{originP[0], originP[1]};
    	double[] vector = new double[]{vectorP[0], vectorP[1]};
    	// detection of the collision		
    	// This is the new origin
    	double[] newOrigin = new double[2];

    	while(workingDistance < distance){
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
			newOrigin[0] = topX;
			newOrigin[1] = dimensions[1];
		}
		else if(vector[1] < 0 && 0<= bottomX && bottomX <= dimensions[0]){ // goes to bottom
			vector[1] *= -1.0;
			newOrigin[0] = bottomX;
			newOrigin[1] = 0;
		}
		else if(vector[0] < 0 && 0<= yLeft && yLeft <= dimensions[1]){ // goes to left
			vector[0] *= - 1;
			newOrigin[0] = 0;
			newOrigin[1] = yLeft;
		}
		else if(vector[0] > 0 &&  0<= yRight && yRight <= dimensions[1]){ // goes right
			vector[0] *= - 1;
			newOrigin[0] = dimensions[0];
			newOrigin[1] = yRight;
		}
		else
			return 0;
		
		Node temp = tree.GetNode(n, newOrigin);
		if(temp != null){
			// check if temp contains 
			Node ntemp = tree.GetNode(temp, endPoint);
			if(ntemp!=null){ // there's a path
				tempDistance += tree.GetLength(temp, endPoint);
				workingDistance += tempDistance;
				return workingDistance <= distance ? 1: 0;
			}
			
		}
			
			
			double dx = newOrigin[0] - origin[0];
			double dy = newOrigin[1] - origin[1];
			
			slope = dy / dx;
			
			// our line can be set to the form of y = slope (x) + (y0 - slope(x0));
			double resultY = (slope * endPoint[0]) + (newOrigin[1] - (slope*newOrigin[0]));
			
			if(endPoint[1] - margin <= resultY && resultY <= endPoint[1] + margin){
				dx = endPoint[0] - origin[0];
				dy = endPoint[1] - origin[1];
				tempDistance = Math.sqrt((dx*dx) + (dy*dy));
				
				n = tree.insert(n, endPoint, tempDistance);
				workingDistance += tempDistance;
				if(workingDistance <= distance)
					return 1;
			}
			else{
				
				
				
				// length
				tempDistance = Math.sqrt((dx*dx) + (dy*dy));
				n = tree.insert(n, newOrigin, tempDistance);
				
				origin[0] = newOrigin[0];
				origin[1] = newOrigin[1];
			}

		workingDistance += tempDistance;
    	}
		if(workingDistance > distance)
			return 0;
		return 1;
    	
    
		
		// get the point of intersection
    	
    	
    	// save the points and their distances
    	/*
    	double z = Math.abs(origin[0] - origin[1]);
    	double w = Math.abs(origin[0] - origin[1]);
    	for(int i = 0; i < 15; i++){
    		if(origin[0] == origin[1]){
    			double x =0;
    		}
    		else{
    			double y = 0;
    		}
    		// unbox these two
    		//isPointOnSlope(origin, origin, vector, z);
    		//getLength(origin, vector);
    		
    		double dx = vector[0] - origin[0];
    		double dy = vector[1] - origin[1];
    		
    		//if(dx == 0 || dy == 0)
    		//	return false;
    		if(1 == 2){
        		double slopez = dy / dx;
        		double slopeq = dy / dx;

    		}
    		else{
        		double slopez = dy / dx;
        		double slopeq = dy / dx;

    		}
    		if(1 == 2){
        		double slopez = dy / dx;
        		double slopeq = dy / dx;

    		}
    		else{
        		double slopez = dy / dx;
        		double slopeq = dy / dx;

    		} 
    		if(1 == 2){
        		double slopez = dy / dx;
        		double slopeq = dy / dx;

    		}
    		else{
        		double slopez = dy / dx;
        		double slopeq = dy / dx;

    		}
    		 slope = dy / dx;
    		
    		// our line can be set to the form of y = slope (x) + (y0 - slope(x0));
    		double resultY = (slope * vector[0]) + (origin[1] - (slope*origin[0]));
    		boolean isPoint = vector[1] - z <= resultY && resultY <= vector[1] + z;
    		double check = Math.sqrt(((dx)*(dx)) + ((dy)*(dy)));
    		double slopez = dy / dx;
    		double slopeq = dy / dx;
    		double slopew = dy / dx;
    		double slopee = dy / dx;
    		double slopwe = dy / dx;
    		double sloqpe = dy / dx;
    		double sloepe = dy / dx;
    		double slyope = dy / dx;
    		double slorpe = dy / dx;
    		double slyorpe = dy / dx;
    	}
    	
    	return 1;
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
		 */
	}
	
	public static double getLength(double[] origin, double[] destination){
		return Math.sqrt(((destination[0] - origin[0])*(destination[0] - origin[0])) + ((destination[1] - origin[1])*(destination[1] - origin[1])));
	}

}
