package com.google.challenges.banana;

import java.util.ArrayDeque;
import java.util.Arrays;

public class Answer {   

	// print the paths first
	public static void main(String[] args){
		//int [] example = new int[]{1, 1};
		//int [] example = new int[]{1, 2, 3, 7,3,21,13,19};
		
		//int [] example = new int[]{1,1,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
		int [] example = new int[]{1,1};
		long millis = System.currentTimeMillis();

		int answer = answer(example);
		long endMillis = System.currentTimeMillis() - millis;

		System.out.println(String.format("Answer = %d, execution time = %d\n", answer, endMillis));

	}
	
	/*
	 * this is a graph problem, i think
	 * general algo goes like:
	 * Create a map for all each number with those they can pair with
	 * After creating the map, traverse the map using DFS (Not sure if BFS can work, but we need
	 * to backtrack our way to count the numbers that can be paired
	*/
    public static int answer(int[] banana_list) { 
        // Your code goes here.
    	
    	// the maximum count of guards
    	int guardCount = banana_list.length;
    	
    	// the routes state whether numbers can be paired
    	int routes[][] = new int[banana_list.length][banana_list.length];
    	
    	// declare an array for our visit nodes
    	int visitNodes[] = new int[banana_list.length];
    	
    	// declare an array for our paired items
    	int pairedItem[] = new int[banana_list.length];
    	
    	// declare a queue for our paired list
    	ArrayDeque<Integer> pairedQueue = new ArrayDeque<Integer>();

    	// declare a stack for our DFS
    	// I don't think we'll need coordinates for this
    	ArrayDeque<Integer> dfsStack = new ArrayDeque<Integer>();
    	
    	// delcare our integers for comparison
    	int larger, smaller, sum, totalConsecutiveCount = 0;
    	// generate the map of items that can be connected
    	    	
    	// sort the list so that we no longer compare inside the loop
    	Arrays.sort(banana_list);
    	
    	for(int i = 0; i<banana_list.length; i++){
    		for(int j = banana_list.length-1; j>i; j--){
    			smaller = banana_list[i];
    			larger = banana_list[j];
    			
    			// divide and module
    			if(larger % smaller == 0){
    				larger = larger/smaller;
    				smaller = 1;
    			}
    			
    			// add larger and smaller, check if they are
    			sum = larger + smaller;
    			
    			if((sum & (sum-1)) != 0 ){
    	    		// this is a candidate for non termination
    				routes[i][j] = 1;
    				routes[j][i] = 1;
    	    	}
    		}
    	}
    	/*
    	// debug code
    	// print the array first
    	for(int i = 0; i < banana_list.length; i++){
    		System.out.print(banana_list[i] + " ");
    	}
    	System.out.println(" ");
    	printMap(routes);
    	*/
    	// get a first node to visit
    	boolean consecutive = true;
    	int current = 0;
    	// declare our boolean checker for consecutive items that are not paired
    	boolean hasUnvisited = true;
    	while(hasUnvisited){
	    	
	    	
	    	// now we do depth first search
	    	
	    	dfsStack.push(current);
	    	
	    	int adjacent = -1;
	    	
	    	while(!dfsStack.isEmpty()){
	    		// check if there are any adjacent items
	    		adjacent = checkAdjacent(current, routes, visitNodes);
	    		if(adjacent > -1 ){
	    			totalConsecutiveCount = pairedQueue.size();
	    			totalConsecutiveCount += (pairedItem[current] == 0 && consecutive) ? 1 : 0;
	    			guardCount -= (totalConsecutiveCount - (totalConsecutiveCount % 2)); // need to remove the excess
	    			
	    			// cleanup our queue, twice per dequeue cycle
	    			int dequeueCount = pairedQueue.size() / 2;
	    			
	    			while(dequeueCount > 0){
	    				int item1 = pairedQueue.remove();
	    				int item2 = pairedQueue.remove();
	    				
	    				pairedItem[item1] = 1;
	    				pairedItem[item2] = 1;
	    				dequeueCount--;
	    			}
	    			
	    			// this is the last dequeue
	    			
	    			if(consecutive && pairedItem[current] == 0 && pairedQueue.size()> 0){
	    				int item1 = pairedQueue.remove();

	    				pairedItem[current] = 1;
	    				pairedItem[item1] = 1;
	    			}
	    			
	    			pairedQueue.clear();
	
	    			
	    			dfsStack.push(current);
	    			current = adjacent;
	    			
	    			// cleanup our variables
	    			totalConsecutiveCount = 0;
	    			consecutive = true;
	    			
	    		}
	    		else{
	    			// need a good condition here
					adjacent = dfsStack.pop();
					
					if(current == adjacent){
						visitNodes[current] = 1;
						break;
					}
	    			if((!pairedQueue.contains(current) && !pairedQueue.contains(adjacent)) &&((pairedItem[current] == 0 && pairedItem[adjacent] == 0) || 
	    					(pairedItem[current] == 0 && dfsStack.size() <= 1))){
	    				pairedQueue.add(current);
	    				pairedQueue.add(adjacent);
	    				visitNodes[current] = 1;
	    				visitNodes[adjacent] = 1;
	    				consecutive = true;
	    			}
	    			else{
	    				consecutive = false;
	    			}
	    			
	    			current = adjacent;
	    		}
	    	}
	    		
	    	// last iteration over the guard count
	    	totalConsecutiveCount = pairedQueue.size();
			totalConsecutiveCount += (pairedItem[current] == 0 && consecutive) ? 1 : 0;
			guardCount -= (totalConsecutiveCount - (totalConsecutiveCount % 2)); // need to remove the excess
			
			// cleanup our queue, twice per dequeue cycle
			int dequeueCount = pairedQueue.size() / 2;
			
			while(dequeueCount > 0){
				int item1 = pairedQueue.remove();
				int item2 = pairedQueue.remove();
				
				pairedItem[item1] = 1;
				pairedItem[item2] = 1;
				dequeueCount--;
			}
			
			// this is the last dequeue
			
			if(pairedItem[current] == 0 && pairedQueue.size()> 0 && consecutive){
				int item1 = pairedQueue.remove();

				pairedItem[current] = 1;
				pairedItem[item1] = 1;
			}
			
			pairedQueue.clear();
			
			// reinitialize unvisit nodes to unpaired,
			visitNodes = Arrays.copyOf(pairedItem, pairedItem.length);
			// will terminate once unpaired has no nodes to visit
			hasUnvisited = false;
			for(int i = 0; i < visitNodes.length; i++){
				if(visitNodes[i] == 0){
					adjacent = checkAdjacent(current, routes, visitNodes);
					// if this node has no adjacent, set this to paired
					if(adjacent <= -1){
						pairedItem[i] = 1;
						visitNodes[i] = 1;
					}
					else{
						hasUnvisited = true;
						current = i; // initialize our first node to the first item
						break;
					}
					
				}
			}
    	}
    	
    	return guardCount;
    } 
    
    // return the adjacent node
    public static int checkAdjacent(int current, int[][] routes, int[] visitNodes){
    	
    	for(int i = 0; i < routes[0].length; i++){
    		if(routes[current][i] == 1 && visitNodes[i] == 0){
    			visitNodes[i] = 1;
    			return i;
    		}
    	}
    	
    	return -1;
    }
    
    static void printMap(int[][] map){
    	for(int i = 0; i < map[0].length; i++){
        	for(int j = 0; j < map[0].length; j++){
        		System.out.print(map[i][j] + " ");
        	}
    		System.out.println(" ");

    	}
    }
    
}
