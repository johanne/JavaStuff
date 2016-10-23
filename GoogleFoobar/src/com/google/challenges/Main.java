package com.google.challenges;

import java.util.Timer;

public class Main {

	static String[][] inputs ={
		{"100000000000000000000000000000000000000000000000000", "3"},
		{"1234567890","123456789"},
		{"1", "2"},
		{"4", "3"},
		{"4", "7"},
		{"7", "3"},
		{"3", "5"},
		{"5", "3"},
		{"8", "3"},
		{"5", "7"},
		{"5", "8"},
		{"3", "10"},
		
		{"3", "7"}};
	static int test_cases = 3;
	static int dimensions[][] = {{3,2}, {300, 275}, {1000, 1000}};
	static int captain_position[][] = {{1,1}, {150,150}, {500,500}};
	static int badguy_position[][] = {{2,1}, {185, 100}, {185, 100}};
	static int distance[] = {4, 500, 10000};
	
	public static void main(String[] args){
		int i = 1;
		
		for(i = 0; i< test_cases; i++){
			System.out.println(String.format("#%d", i+1));
			System.out.println(String.format("dimensions=[%d, %d]", dimensions[i][0], dimensions[i][1]));
			System.out.println(String.format("captain_position = [%d, %d]", captain_position[i][0], captain_position[i][1]));
			System.out.println(String.format("badguy_position = [%d, %d]", badguy_position[i][0], badguy_position[i][1]));
			System.out.println(String.format("distance = %d", distance[i]));
			
			long millis = System.currentTimeMillis();
			int answer1 = Answer.answer(dimensions[i], captain_position[i], badguy_position[i], distance[i]);
			long endMillis = System.currentTimeMillis() - millis;

			System.out.println(String.format("answer = %d - execution time = %d\n", answer1, endMillis));
			/*
			millis = System.currentTimeMillis();
			int answer2 = TreeSolution.answer(dimensions[i], captain_position[i], badguy_position[i], distance[i]);
			endMillis = System.currentTimeMillis() - millis;
			System.out.println(String.format("answer = %d - exectime = %d\n", answer2, endMillis));
			*/
		}
		/*
		for(String[] input : inputs){
			String ans = BombGeneration.answer(input[0], input[1]);
			System.out.println(String.format("#%d: M=%s, F=%s: Answer=%s", i, input[0], input[1], ans));
			i++;
		}*/
	}
}
