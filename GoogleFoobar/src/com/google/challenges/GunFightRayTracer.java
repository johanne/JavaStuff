package com.google.challenges;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;
import java.util.TreeMap;



public class GunFightRayTracer {
	public final class Vector implements Comparable<GunFightRayTracer.Vector>{
    	public Point origin;
    	public Point direction;
    	
    	public Vector(){
    		this.origin = null;
    		this.direction = null;
    	}
    	public Vector(Point origin, Point direction){
    		this.origin = origin;
    		this.direction = direction;
    	}
    	
    	public Vector ChangeDirection(Vector ray, Point intersection, int[] dimensions){
        	// intersected left or right wall
    		BigDecimal xDim = new BigDecimal(dimensions[0]);
        	if(intersection.x.compareTo(BigDecimal.ZERO) == 0 || intersection.x.compareTo(xDim) == 0){
        		ray.direction.x = ray.direction.x.negate();
        		
        	}
        	// otherwise, intersected top or bottom wall
        	else{
        		ray.direction.y = ray.direction.y.negate();
        		
        	}
        	
        	ray.origin = intersection;
        	BigDecimal origX = ray.origin.x;
        	BigDecimal origY = ray.origin.y;
        	BigDecimal directX = ray.direction.x;
        	BigDecimal directY = ray.direction.y;
        	Point orig = new Point(origX, origY);
        	Point direction = new Point(directX, directY);
        	return new Vector(orig, direction);
        }

		@Override
		public int compareTo(Vector o) {
			if(((direction.y.divide(direction.x,RoundingMode.HALF_UP)).subtract(o.direction.y.divide(o.direction.x,RoundingMode.HALF_UP))).compareTo(BigDecimal.ZERO) != 0)
				return 1;
			return this.origin.compareTo(o.origin);
		}
		
		@Override
		public boolean equals(Object o){
			Vector x = (Vector)o;
			return x.origin.compareTo(this.origin) == 0 && x.direction.compareTo(this.direction) == 0;
		}
    }
	static GunFightRayTracer instance = new GunFightRayTracer();
	static Point bottomLeftCorner = instance.new Point();
	static Point bottomRightCorner = instance.new Point();
	static Point topLeftCorner = instance.new Point();
	static Point topRightCorner = instance.new Point();
	static Vector ray;
	
    public static int answer(int[] dimensions, int[] captain_position, int[] badguy_position, int distance) { 
    	
    	bottomLeftCorner = instance.new Point(BigDecimal.ZERO, BigDecimal.ZERO);
    	bottomRightCorner = instance.new Point(new BigDecimal(dimensions[0]), BigDecimal.ZERO);
    	topLeftCorner = instance.new Point(BigDecimal.ZERO, new BigDecimal(dimensions[1]));
    	topRightCorner = instance.new Point(new BigDecimal(dimensions[0]), new BigDecimal(dimensions[1]));
    	BigDecimal distanceBig = new BigDecimal(distance);

    	
    	
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
    	// create an array of vector paths
    	TreeMap<GunFightRayTracer.Vector, GunFightRayTracer.Point> vectors = new TreeMap<GunFightRayTracer.Vector, GunFightRayTracer.Point>();
    	ArrayList<BigDecimal> slopes = new ArrayList<>();
    	int possiblePaths = 0;
    	Point captain = instance.new Point(new BigDecimal(captain_position[0]), new BigDecimal(captain_position[1]));
    	Point badguy = instance.new Point(new BigDecimal(badguy_position[0]), new BigDecimal(badguy_position[1]));
    	// generate all rays    	
    	
    	for(int x = -dimensions[0]; x <=  dimensions[0]; x++){
    		for(int y = dimensions[1]; y<= dimensions[1]; y++){
    			
    			BigDecimal refX = new BigDecimal(x);
    			BigDecimal refY = new BigDecimal(y);
    			BigDecimal slope = y == 0 ? x < 0 ? new BigDecimal(-100000) : new BigDecimal(100000) : x == 0 ? new BigDecimal(Double.MAX_VALUE) : refY.divide(refX, 10, RoundingMode.HALF_UP); //) * 100000.00)/100000.00;
    			//System.out.println("iteration: " + i + "x = " + x + "y = " + y);
    			
    			//BigDecimal slope = x == 0 ? BigDecimal.MAX_VALUE : (BigDecimal)y/(BigDecimal)x;
    			if(slopes.contains(slope)){
    				//System.out.println("Skipped");
    				continue;
    			}
    			
    			// need to check if edge hit
    			
    			//slopes.add(slope);
    			boolean hit = false;
    			BigDecimal workingDistance = BigDecimal.ZERO;
    			// what is our tangent?
    			
    			// it will initially be defined by our first hit
    			Point initialBeam = instance.new Point(new BigDecimal(x), new BigDecimal(y));
    			if(initialBeam.isPointInTheLine(initialBeam, captain, badguy)){
    				//System.out.println(String.format("Invalid: %d, %d", x, y));
    				continue;
    			}
    			ray = instance.new Vector();
    			ray.direction = initialBeam;
    			ray.origin = captain;
    			
    			
    			
    			// we already have the initial point of intersection
				Point intersection = GetPointOfIntersection(ray, dimensions);
				if(intersection == null)
    				continue;
					
    				
    			// while loop here, distance should always be greater
    			// also, there should not be any hits
    			while(workingDistance.compareTo(distanceBig) <= 0 && !hit )
    			{
    				
    				intersection = GetPointOfIntersection(ray, dimensions);
    				// hit a wall
    				/*
    				if(vectors.containsKey(ray)){
    					intersection = vectors.get(ray);
    				}
    				else{
    					intersection = GetPointOfIntersection(ray, dimensions);
    					BigDecimal origX = ray.origin.x;
    					BigDecimal origY = ray.origin.y;
    					BigDecimal directX = ray.direction.x;
    					BigDecimal directY = ray.direction.y;
    					vectors.put(instance.new Vector(instance.new Point(origX, origY), instance.new Point(directX, directY)), intersection);
    				}
    				*/
    				
    				// predict the distance by computing the next intersection
    				if(intersection == null){
    					
    					break;
    				}
    					
    				if(ray.origin.compareTo(captain) != 0 && captain.isPointInTheLine(captain, ray.origin, intersection))
    				{
    					break;
    				}
    				
    				
    				if(badguy.isPointInTheLine(badguy, ray.origin, intersection)){
    	   				workingDistance.add(Line.getLength(ray.origin, badguy));
    	   				hit = true;
    	   				if(workingDistance.compareTo(distanceBig) <= 0){
    	   					System.out.println(String.format("Vector that caused collision:destination: %d, %d", x, y));
    		   				System.out.println("count = "  + possiblePaths++);
    	   				}
    				}
    				else{
    					if(IsEdgeHit(intersection)){
    						// check the other tangent as well
        					break;
    						//
    					}
    					// Add total distance
    					if(Line.getLength(ray.origin, intersection).doubleValue() < .1)
    						break;
    					workingDistance = workingDistance.add(Line.getLength(ray.origin, intersection));
        				// change the ray direction
        				ray = ray.ChangeDirection(ray, intersection, dimensions);
    				}
    				
    				i++;
    				
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
    public final class Point  implements Comparable<Point>{
    	private BigDecimal x;
    	private BigDecimal y;
    	
    	public Point(){
    		this.x = BigDecimal.ZERO;
    		this.y = BigDecimal.ZERO;
    	}
    	
    	public Point(BigDecimal x, BigDecimal y){
    		this.x = x;
    		this.y = y;
    	}
    	
    	public boolean isPointInTheLine(Point interest, Point origin, Point destination){
    		boolean retVal = false;
/*
    		// just create the line
    		BigDecimal y = (destination.y.subtract(origin.y));
    		BigDecimal x = (destination.x.subtract(origin.x));
    		if(x.compareTo(BigDecimal.ZERO)==0){
    			// therefore, x should be equal to origin
    			retVal = interest.x.compareTo(x) == 0;
    		}
    		else if (y.compareTo(BigDecimal.ZERO) == 0){
    			retVal = interest.y.compareTo(y) == 0;
    			// we have a horizontal line
    		}
    		else{
    			// test if the point is within the line
    			BigDecimal slope = (y.divide(x, 10, RoundingMode.HALF_EVEN));
    			BigDecimal yValue = slope.multiply((interest.x.subtract(origin.x))).add(origin.y);
    			if(interest.y.compareTo(yValue) == 0)
    				retVal = true;
    		}
    		*/
    		BigDecimal dxInterest = interest.x.subtract(origin.x);
    		BigDecimal dyInterest = interest.y.subtract(origin.y);
    		BigDecimal dxLine = destination.x.subtract(origin.x);
    		BigDecimal dyLine = destination.y.subtract(origin.y);
    		BigDecimal cross = (dxInterest.multiply(dyLine )).subtract((dyInterest.multiply(dxLine)));
    		
    		cross = cross.setScale(-1, RoundingMode.HALF_EVEN);
    		retVal = cross.abs().compareTo(BigDecimal.ZERO) == 0;
    		
    		if(dxLine.abs().compareTo(dyLine.abs()) >= 0){
    			retVal = retVal && (dxLine.compareTo(BigDecimal.ZERO) > 0 ? origin.x.compareTo(interest.x) <= 0 && interest.x.compareTo(destination.x) <= 0 :
    				destination.x.compareTo(interest.x) <= 0 && interest.x.compareTo(origin.x) <= 0);
    		}
    		else{
    			retVal = retVal && (dyLine.compareTo(BigDecimal.ZERO) > 0 ? origin.y.compareTo(interest.y) <= 0 && interest.y.compareTo(destination.y) <= 0 :
    				destination.y.compareTo(interest.y) <= 0 && interest.y.compareTo(origin.y) <= 0);
    		}
    		return retVal;
    	}

		@Override
		public int compareTo(Point o) {
			// TODO Auto-generated method stub
			return (int)Math.abs((this.x.subtract(o.x).intValue()) + (this.y.subtract(o.y).intValue()));
		}
    }
    
    static class Line{
    	public Point origin;
    	public Point end;
    	
    	public Line(Point origin, Point end){
    		this.origin = origin;
    		this.end = end;
    	}
    	
    	public BigDecimal getLength(){
    		return BigDecimal.valueOf(Math.sqrt(((origin.x.subtract(end.x)).pow(2)).add((origin.y.subtract(end.y)).pow(2)).doubleValue()));
    	}
    	
    	public static BigDecimal getLength(Point o, Point e){
    		return BigDecimal.valueOf(Math.sqrt(((o.x.subtract(e.x)).pow(2)).add((o.y.subtract(e.y)).pow(2)).doubleValue()));
    		
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
    	Line referenceLine = new Line(ray.origin, instance.new Point(ray.direction.x, ray.direction.y));
    	
    	// try the algo first
    	BigDecimal a1, a2, b1, b2, c1, c2;
    	BigDecimal det = BigDecimal.ZERO;
    	a1 = referenceLine.end.y.subtract(referenceLine.origin.y);
	    b1 = referenceLine.origin.x.subtract(referenceLine.end.x);
	    c1 = (a1.multiply(referenceLine.origin.x)).add((b1.multiply(referenceLine.origin.y)));

    	a2 = side.end.y.subtract(side.origin.y);
	    b2 = side.origin.x.subtract(side.end.x);
	    c2 = (a2.multiply(side.origin.x)).add((b2.multiply(side.origin.y)));

	    det = (a1.multiply(b2)).subtract((a2.multiply(b1)));
	    if(det.compareTo(BigDecimal.ZERO) == 0){
	    	return null;
	    }
	    else{
	    	// NEED TO CHECK IF POINT IS IN RAY LINE
	    	BigDecimal roundedX = ((b2.multiply(c1)).subtract((b1.multiply(c2)))).divide(det, 10, RoundingMode.HALF_UP);
	    	 BigDecimal roundedY = ((a1.multiply(c2)).subtract((a2.multiply(c1)))).divide(det, 10, RoundingMode.HALF_UP);
	    	//BigDecimal roundedX = Math.round((((b2*c1) - (b1*c2))/det) * 10000.00)/10000.00;
	    	//BigDecimal roundedY = Math.round((((a1*c2)-(a2*c1))/det) * 10000.00)/10000.00;
	    	
	    	if(roundedX.compareTo(BigDecimal.ZERO) < 0 || roundedY.compareTo(BigDecimal.ZERO) < 0 )//|| roundedX > dimensions[0] || roundedY > dimensions[1])
	    	{
	    		
	    		return null;
	    	}
		    Point retVal = instance.new Point(roundedX, roundedY);
		    // CHECK THAT THE RETVAL IS WITHIN BOUNDS
		    if(retVal.isPointInTheLine(retVal, ray.origin, ray.direction))
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
    	Line referenceLine = new Line(ray.origin, instance.new Point(ray.direction.x * 1000, ray.direction.y * 1000));
    	
    	// try the algo first
    	BigDecimal a1, a2, b1, b2, c1, c2;
    	BigDecimal r1, r2, r3, r4;
    	BigDecimal denom, offset, num;
    	
    	a1 = referenceLine.end.y - referenceLine.origin.y;
	    b1 = referenceLine.origin.x - referenceLine.end.x;
	    c1 = (referenceLine.end.x * referenceLine.origin.y) - (referenceLine.origin.x * referenceLine.end.y);
	    
	    
	    
	    r3 = (a1 * side.origin.x) + (b1 * side.origin.y) + c1;
	    r4 = (a1 * side.end.x) + (b1 * side.end.y) + c1;
	    
	    if(r3 != 0 && r4 !=0 && ((r3 > 0 && r4 > 0) || (r3 < 0 && r4<0)))
	    		return retVal;
	    
	    // do the same with a2, b2, c2
    	a2 = side.end.y - side.origin.y;
	    b2 = side.origin.x - side.end.x;
	    c2 = (side.end.x * side.origin.y) - (side.origin.x * side.end.y);
	    
	    r1 = (a2 * referenceLine.origin.x) + (b2 * referenceLine.origin.y) + c2;
	    r2 = a2 * referenceLine.end.x + b2 * referenceLine.end.y + c2;
	    
	    if(r1 != 0 && r2 !=0 && ((r1 > 0 && r2 > 0) || (r1 < 0 && r2<0)))
    		return retVal;
	    
	    denom = (a1 * b2) - (a2 * b1);
	    
    	// lies on the same line
	    if(denom == 0)
	    	return retVal;

	    offset = denom < 0 ? - denom /2 : denom / 2;
    	
    	
    	retVal = instance.new Point();
    	num = b1 * c2 - b2 * c1;
        retVal.x= ( num < 0 ? num - offset : num + offset ) / denom;
        

        num = a2 * c1 - a1 * c2;
        retVal.y = ( num < 0 ? num - offset : num + offset ) / denom;
        
        // the point should be inside the origin and the destination
        if(retVal.isPointInTheLine(retVal, ray.origin, ray.direction))
        	return retVal;
        
        return null;
        */
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
    	Line[] sides = {top, bottom, left, right};

    	for(int i = 0; i <4 && retVal == null; i++){
    		// do not intersect if on the same side
    		if(!ray.origin.isPointInTheLine(ray.origin, sides[i].origin, sides[i].end))
    		retVal = Intersect(ray, sides[i], dimensions);
    	}
    	
    	return retVal;
    }
    
    public static boolean IsOnSameSide(Line reference, Point originPoint){
    	return (reference.origin.x.compareTo(originPoint.x) <=0 && originPoint.x.compareTo(reference.end.x) <=0) ||
    			(reference.origin.y.compareTo(originPoint.y) <= 0 && originPoint.y.compareTo(reference.end.y) <= 0);
    			
    }
    
    public static boolean IsEdgeHit(Point intersection){
    	return (intersection.x == topLeftCorner.x && intersection.y == topLeftCorner.y) ||
    			(intersection.x == topRightCorner.x && intersection.y == topRightCorner.y) ||
    			(intersection.x == bottomLeftCorner.x && intersection.y == bottomLeftCorner.y) ||
    			(intersection.x == bottomRightCorner.x && intersection.y == bottomRightCorner.y);
    			
    }
}
