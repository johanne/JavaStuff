package com.google.challenges;

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
	static int test_cases = 5;
	public static void main(String[] args){
		int i = 1;
		for(String[] input : inputs){
			String ans = BombGeneration.answer(input[0], input[1]);
			System.out.println(String.format("#%d: M=%s, F=%s: Answer=%s", i, input[0], input[1], ans));
			i++;
		}
	}
}
