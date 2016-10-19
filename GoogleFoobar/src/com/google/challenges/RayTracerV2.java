package com.google.challenges;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
public class RayTracerV2 {




	
		static Point bottomLeftCorner = new Point();
		static Point bottomRightCorner = new Point();
		static Point topLeftCorner = new Point();
		static Point topRightCorner = new Point();

		
	    public static int answer(int[] dimensions, int[] captain_position, int[] badguy_position, int distance) { 
	    	bottomLeftCorner = new Point(0, 0);
	    	bottomRightCorner = new Point(dimensions[0], 0);
	    	topLeftCorner = new Point(0, dimensions[1]);
	    	topRightCorner = new Point(dimensions[0], dimensions[1]);
	    	// generate a vector to hit the walls
	    	// upon hitting a wall, change the sign of the vector
	    	// the slope is the rise / run, as well as the x-intercept == length of side
	    	// hit a wall using the vector signs
	    	// generate the opposite using the tangent
	    	// get the tangent to the destination, if same as current tangent, terminate
	    	// get the length traveled. check if already exceeds.
	    	// reverse the tangent value. repeat from the hit.

	    	
	    	
			/*
			 * Algorithm:
			 * Generate the rays - do not use rays that have the same slope
			 * while (workingDistance < distance)
			 *  Hit a wall - determine the wall
			 *  determine the tangent
			 *  If(hit point, origin, and destination are on the same line
			 *  	add working distance
			 *  	
			 * 	Change ray direction
			 * 	compute tangent lines
			 * 	Add total distance
			 */
	    	int i = 1;

	    	HashMap<Double, Point> slopes = new HashMap<Double, Point>();
	    	int possiblePaths = 0;
	    	Point captain = new Point(captain_position[0], captain_position[1]);
	    	Point badguy = new Point(badguy_position[0], badguy_position[1]);
	    	// generate all rays    	
	    	for(int x = (int)-dimensions[0]; x <= (int)dimensions[0]; x++){
	    		for(int y = (int)-dimensions[0]; y<= (int)dimensions[0]; y++){
	    			
	    			//double slope = y == 0 ? x < 0 ? -100000 : 10000 :x == 0 ? Double.MAX_VALUE : Math.round(((double)y/(double)x) * 100000.00)/100000.00 ;
	    			double slope = y == 0 ? x < 0 ? -100000 : 10000 :x == 0 ? Double.MAX_VALUE : (double)y/(double)x;
	    			// get gcf
	    			
	    			//double slope = x == 0 ? Double.MAX_VALUE : (double)y/(double)x;
	    			if(x == 2 && y == 3){
	    				System.out.println("gumana");
	    			}
	    			if(slopes.containsKey(slope)){
	    				// check if the point is really the same
	    				Point test = slopes.get(slope);
	    				double largerX = test.x > x? test.x:x;
	    				double smallerX =test.x > x? x: test.x;
	    				double largerY = test.y > y ? test.y : y;
	    				double smallerY = test.y > y ? y : test.y;
	    				if(Math.abs(smallerX) > 1 && largerX %smallerX == 0 && largerX/smallerX > 0 && Math.abs(smallerY) > 1 && largerY % smallerY == 0 && largerY/smallerY > 0){
	        				continue;
	    					
	    				}
	    			}
	    			
	    			//if(i > 30000)
	    			//	return possiblePaths;
	    			
	    			// need to check if edge hit
	    			
	    			
	    			boolean hit = false;
	    			double workingDistance = 0;
	    			// what is our tangent?
	    			
	    			// it will initially be defined by our first hit
	    			Point initialBeam = new Point(x, y);
	    			if(initialBeam.x == 2 && initialBeam.y == 3){
	    				System.out.println("gumana");
	    			}
	    			slopes.put(Double.valueOf(slope), initialBeam);
	    			
	    			if(Point.isPointInTheLine(initialBeam, captain, badguy))
	    				continue;
	    			Vector ray = new Vector(captain, initialBeam);
	    			
	    			
	    			// we already have the initial point of intersection
					Point intersection = GetPointOfIntersection(ray, dimensions);
					if(intersection == null)
						continue;
					
	    			while(workingDistance <= distance && !hit )
	    			{
	    				
	    				// hit a wall
	    				intersection = GetPointOfIntersection(ray, dimensions);
	    				// predict the distance by computing the next intersection
	    				if(intersection == null)
	    					break;
	    				
	    				if(Point.isPointInTheLine(badguy, ray.origin, intersection)){
	    	   				workingDistance += Line.getLength(ray.origin, badguy);
	    	   				hit = true;
	    	   				System.out.println("Hit! x = " + x + " y = " + y);
	    	   				if(workingDistance <=distance)
	    		   				possiblePaths++;
	    				}
	    				else{
	    					if(IsEdgeHit(intersection)){
	    						double tempDistance = Line.getLength(ray.origin, badguy);
	    						ray = ChangeDirection(ray, intersection, dimensions);
	    						intersection = GetPointOfIntersection(ray, dimensions);
	    						
	    						if(intersection == null)
	    							break;
	    						if(Point.isPointInTheLine(badguy, ray.origin, intersection)){
	    							System.out.println("Hit! x = " + x + " y = " + y);
	    	    	   				workingDistance += tempDistance;
	    	    	   				hit = true;
	    	    	   				if(workingDistance <=distance)
	    	    		   				possiblePaths++;
	    	    				}
	        					break;
	    						//
	    					}
	    					// Add total distance
	    					double testDistance = Line.getLength(ray.origin, intersection);
	    					if(testDistance < .1)
	    						workingDistance = distance;
	    	   				workingDistance += testDistance;
	        				// change the ray direction
	        				ray = ChangeDirection(ray, intersection, dimensions);
	    				}
	    				
	    			}
	    		}
	    	}

	    	
	    	
	    	return possiblePaths + 1;
	    }
	    
		/**
		 * This class will be used for determining the vectors, as well as the coordinates of intersections
		 * @author Johanne Demetria
		 *
		 */
	    static class Point{
	    	public double x;
	    	public double y;
	    	
	    	public Point(){
	    		this.x = 0;
	    		this.y = 0;
	    	}
	    	
	    	public Point(double x, double y){
	    		this.x = x;
	    		this.y = y;
	    	}
	    	
	    	public static boolean isPointInTheLine(Point interest, Point origin, Point destination){
	    	
	    		boolean retVal = false;
	    		double dxInterest = interest.x - origin.x;
	    		double dyInterest = interest.y - origin.y;
	    		double dxLine = destination.x - origin.x;
	    		double dyLine = destination.y - origin.y;
	    		
	    		double longLength = Line.getLength(origin, destination);
	    		double shortcutLength = Line.getLength(origin, interest) + Line.getLength(interest, destination);
	    		
	    		// within +-.5
	    		retVal = ((longLength + .2 >= shortcutLength) && (longLength-.2 <= shortcutLength));
	    		
	    		//double cross = (dxInterest * dyLine )- (dyInterest * dxLine);
	    		
	    		//retVal = (Math.round(Math.abs(cross)*100.00) / 100.00) == 0 || Math.abs(cross) == 1;
	    		
	    		if(Math.abs(dxLine) >= Math.abs(dyLine)){
	    			retVal = retVal && (dxLine > 0 ? origin.x <= interest.x && interest.x <= destination.x :
	    				destination.x <= interest.x && interest.x <= origin.x);
	    			retVal = retVal && (dyLine > 0 ? origin.y <= interest.y && interest.y <= destination.y :
	    				destination.y <= interest.y && interest.y <= origin.y);
	    		}
	    		else{
	    			retVal = retVal && (dxLine > 0 ? origin.x <= interest.x && interest.x <= destination.x :
	    				destination.x <= interest.x && interest.x <= origin.x);
	    			retVal = retVal && (dyLine > 0 ? origin.y <= interest.y && interest.y <= destination.y :
	    				destination.y <= interest.y && interest.y <= origin.y);
	    		}
	    		return retVal;
	    	}
	    }
	    
	    static class Line{
	    	public Point origin;
	    	public Point end;
	    	
	    	public Line(Point origin, Point end){
	    		this.origin = new Point(origin.x, origin.y);
	    		this.end = new Point(end.x, end.y);
	    	}
	    	
	    	public double getLength(){
	    		return Math.sqrt(Math.pow(origin.x - end.x, 2) + Math.pow(origin.y-end.y, 2));
	    	}
	    	
	    	public static double getLength(Point o, Point e){
	    		return Math.sqrt(Math.pow(o.x - e.x, 2) + Math.pow(o.y-e.y, 2));
	    		
	    	}
	    }
	    
	    static class Vector{
	    	public Point origin;
	    	public Point direction;
	    	
	    	public Vector(Point origin, Point direction){
	    		this.origin = new Point(origin.x, origin.y);
	    		this.direction = new Point(direction.x, direction.y);
	    	}
	    }
	    
	    /**
	     * Checks whether a vector collides with a specified side
	     * @param ray
	     * @param side
	     * @return null if there is no intersection
	     */
	    public static Point Intersect(Vector ray, Line side, int dimensions[]){
	    	
	    	// try a different approach
	    	Line referenceLine = new Line(ray.origin, new Point(ray.direction.x, ray.direction.y));
	    	
	    	// try the algo first
	    	double a1, a2, b1, b2, c1, c2;
	    	double det = 0;
	    	a1 = referenceLine.end.y - referenceLine.origin.y;
		    b1 = referenceLine.origin.x - referenceLine.end.x;
		    c1 = a1 * referenceLine.origin.x + b1 * referenceLine.origin.y;

	    	a2 = side.end.y - side.origin.y;
		    b2 = side.origin.x - side.end.x;
		    c2 = a2 * side.origin.x + b2 * side.origin.y;

		    det = a1 * b2 - a2 * b1;
//		    det = Math.round(10.00*(a1 * b2 - a2 * b1))/10.00;
		    if(det == 0){
		    	return null;
		    }
		    else{
		    	// NEED TO CHECK IF POINT IS IN RAY LINE
		    	 double roundedX = (((b2*c1) - (b1*c2))/det);
		    	 double roundedY = (((a1*c2)-(a2*c1))/det);
		    	//double roundedX = Math.round((((b2*c1) - (b1*c2))/det) * 10000.00)/10000.00;
		    	//double roundedY = Math.round((((a1*c2)-(a2*c1))/det) * 10000.00)/10000.00;
		    	
		    	if(roundedX < 0 || roundedY < 0 || roundedX > dimensions[0] || roundedY > dimensions[1])
		    		return null;
			    Point retVal = new Point(roundedX, roundedY);
			    // CHECK THAT THE RETVAL IS WITHIN BOUNDS
			    if(Point.isPointInTheLine(retVal, ray.origin, ray.direction))
		    		return retVal;
		    	return null;
		    }
		    /*
		    
	    	Point retVal = null;
	    	
	    	// check only if ray would hit the line on the direction stated
	    	// check the set of values, based on the origin and the direction
	    	// get the equation of both lines
	    	
	    	
	    	// what is the slope?
	    	// y = mx+b
	    	
	    	// create a new line from the vector, intersect the two lines
	    	Line referenceLine = new Line(ray.origin, new Point(ray.direction.x*1000, ray.direction.y*1000));
	    	
	    	// try the algo first
	    	double a1, a2, b1, b2, c1, c2;
	    	double r1, r2, r3, r4;
	    	double denom, offset, num;
	    	
	    	a1 = referenceLine.end.y - referenceLine.origin.y;
		    b1 = referenceLine.origin.x - referenceLine.end.x;
		    c1 = referenceLine.end.x * referenceLine.origin.y - referenceLine.origin.x * referenceLine.end.y;
		    
		    
		    
		    r3 = a1 * side.origin.x + b1 * side.origin.y + c1;
		    r4 = a1 * side.end.x + b1 * side.end.y + c1;
		    
		    if(r3 != 0 && r4 !=0 && ((r3 > 0 && r4 > 0) || (r3 < 0 && r4<0)))
		    		return retVal;
		    
		    // do the same with a2, b2, c2
	    	a2 = side.end.y - side.origin.y;
		    b2 = side.origin.x - side.end.x;
		    c2 = side.end.x * side.origin.y - side.origin.x * side.end.y;
		    
		    r1 = a2 * referenceLine.origin.x + b2 * referenceLine.origin.y + c2;
		    r2 = a2 * referenceLine.end.x + b2 * referenceLine.end.y + c2;
		    
		    if(r1 != 0 && r2 !=0 && ((r1 > 0 && r2 > 0) || (r1 < 0 && r2<0)))
	    		return retVal;
		    
		    denom = a1 * b2 - a2 * b1;
		    
	    	// lies on the same line
		    if(denom == 0)
		    	return retVal;

		    offset = denom < 0 ? - denom /2 : denom / 2;
	    	
	    	
	    	retVal = new Point();
	    	num = b1 * c2 - b2 * c1;
	        retVal.x= ( num < 0 ? num - offset : num + offset ) / denom;
	        

	        num = a2 * c1 - a1 * c2;
	        retVal.y = ( num < 0 ? num - offset : num + offset ) / denom;
	        
	        // the point should be inside the origin and the destination
	        if(Point.isPointInTheLine(retVal, ray.origin, ray.direction))
	        	return retVal;
	        
	        return null;
	        */
	    }
	    
	    public static Vector ChangeDirection(Vector ray, Point intersection, int[] dimensions){
	    	// intersected left or right wall
	    	if(intersection.x == 0 || intersection.x == dimensions[0]){
	    		ray.direction.x *= -1;
	    		
	    	}
	    	if(intersection.y == 0 || intersection.y == dimensions[1]){
	    		ray.direction.y *= -1;
	    		
	    	}
	    	
	    	ray.origin = intersection;
	    	return ray;
	    }
	    
	    /**
	     * Checks the point of intersection of a Vector and a bounding rectangle
	     * @param ray
	     * @param dimenions
	     * @return
	     */
	    public static Point GetPointOfIntersection(Vector ray, int [] dimensions){
	    	Point retVal = null;
	    	

	    	// define the sides
	    	Line top = new Line(topLeftCorner, topRightCorner);
	    	Line bottom = new Line(bottomLeftCorner, bottomRightCorner);
	    	Line left = new Line(bottomLeftCorner, topLeftCorner);
	    	Line right = new Line(bottomRightCorner, topRightCorner);
	    	Line[] sides = {left, right, top, bottom};

	    	for(int i = 0; i <4 && retVal == null; i++){
	    		// do not intersect if on the same side
	    		if(!Point.isPointInTheLine(ray.origin, sides[i].origin, sides[i].end))
	    		retVal = Intersect(ray, sides[i], dimensions);
	    	}
	    	
	    	return retVal;
	    }
	    
	    public static boolean IsOnSameSide(Line reference, Point originPoint){
	    	return (reference.origin.x <= originPoint.x && originPoint.x <=reference.end.x) ||
	    			(reference.origin.y <= originPoint.y && originPoint.y <=reference.end.y);
	    			
	    }
	    
	    public static boolean IsEdgeHit(Point intersection){
	    	return (intersection.x == topLeftCorner.x && intersection.y == topLeftCorner.y) ||
	    			(intersection.x == topRightCorner.x && intersection.y == topRightCorner.y) ||
	    			(intersection.x == bottomLeftCorner.x && intersection.y == bottomLeftCorner.y) ||
	    			(intersection.x == bottomRightCorner.x && intersection.y == bottomRightCorner.y);
	    			
	    }
	    	
}
