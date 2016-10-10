package com.google.challenges;

import java.math.*;

public class BombGeneration {
	
	
	
	
	public static int size = 51*4;
	
	public static class BigNumber 
	{
		// ensure sizes first for this implementation
		
		//
		// try bitwise operation
		private int[] _mRepresentation;
		// public int signum = 1;
		
		
		
		
		public BigNumber()
		{
			_mRepresentation = new int[size + 1]; // for overflow
		}
		
		// no need for merge type
		public BigNumber(String number)
		{
			char [] representation = number.toCharArray();
			int repSize = representation.length;
			
			_mRepresentation = new int[size + 1]; //for overflow
			
			int i = size;

			for(int j = repSize - 1; j >= 0; j--)
			{
				int a = (representation[j]^0x30);
				
				for(int q = 0; q< 4; q++)
				{
					_mRepresentation[i-q] = a>>q&1;
				}
				i-=4;
			}
		}
		
		
		// all we need to do now is figure out a way to determine if our number is greater than 1
		public boolean greater(BigNumber b)
		{
			return this.subtract(b)._mRepresentation[0]==0 && !this.equal(b);
		}
		
		public boolean equal(BigNumber b)
		{
			boolean returnValue = true;
			
			for(int i = 0; i< size + 1&&returnValue; i++){
				returnValue = this._mRepresentation[i]==b._mRepresentation[i];
			}
			
			return returnValue;
		}
		
		public BigNumber subtract(BigNumber b)
		{
			// get the two's complement
			BigNumber subtrahend = new BigNumber();
			BigNumber one = new BigNumber();
			one._mRepresentation[size] = 1;
			
			// flip first
			for(int i = 0; i < size + 1; i++){
				subtrahend._mRepresentation[size-i] = b._mRepresentation[size - i]^1; 
			}
			
			// add 1 to the flipped value
			subtrahend = subtrahend.add(one);
			
			// then add this to the subtrahend
			return this.add(subtrahend);
		}
		
		private BigNumber add(BigNumber b)
		{
			BigNumber returnValue = new BigNumber();
			
			
			int cIn = 0;
			int i = 0;
			for(i = 0; i < size + 1; i++){
				returnValue._mRepresentation[size - i] = this._mRepresentation[size - i] ^ b._mRepresentation[size-i] ^ cIn;
				// cIn will be added to the next iteration
				cIn = (cIn & (this._mRepresentation[size - i] | b._mRepresentation[size-i])) |(this._mRepresentation[size - i] & b._mRepresentation[size-i]);
			}
			
			
			
			return returnValue;
		}
	}
	
	// technically, BigInteger is slow
	// so we'll have to implement our own
	// method to subtract items
	public static String answer2(String M, String F)
	{
		// Flow of events
		// start proc
		// while both are greater than 0
		// look at who is larger
		// subtract the smaller from the larger
		// increment the loop
		
		// if (at least one is one) and there was an increment
		// return increment - 1

		// generic return
		String retVal = "impossible";
		
		BigNumber a = new BigNumber(M);
		BigNumber b = new BigNumber(F);
		BigNumber ZERO = new BigNumber();
		BigNumber ONE = new BigNumber();
		ONE._mRepresentation[size] = 1;
		BigNumber larger;
		BigNumber smaller;
		BigNumber placeholder;
		if(a.greater(b)){
        	larger = a;
        	smaller = b;
    	}
    	else{
        	larger = b;
        	smaller = a;
    	}
		int generationNumber = 0;

		while(larger.greater(ZERO)&&smaller.greater(ZERO))
		{
			larger = larger.subtract(smaller);
    		
    		if(smaller.greater(larger)){
    			placeholder = smaller;
	    		smaller = larger;
	    		larger = placeholder;
    		}
			// get the bigger number
			generationNumber++;
			
		}

		
		if((larger.equal(ONE) || smaller.equal(ONE)) && generationNumber > 0)
			retVal = String.valueOf(generationNumber - 1);

		return retVal;
	}
	
	// create our own class
	
	
	
	// the input is a string, so probably we can use
	// BigInt
	// we can also do a string operation, in case
	// that one is better
    public static String answer(String M, String F) { 
    	// Big integer is slow, we'll use a different approach
    	String retVal = "impossible";
    	

    	// mod, multiply to the smaller
    	// then subtract from the larger. add to the iteration
    	BigInteger mach = new BigInteger(M);
    	BigInteger facula = new BigInteger(F);
    	BigInteger iterations = new BigInteger("0");
    	BigInteger larger, smaller, placeholder;

    	if(mach.compareTo(facula) > 0){
        	larger = new BigInteger(M);
        	smaller = new BigInteger(F);
    	}
    	else{
        	larger = new BigInteger(F);
        	smaller = new BigInteger(M);
    	}
    	

    	
    	while(larger.compareTo(BigInteger.ZERO) > 0 && smaller.compareTo(BigInteger.ZERO) > 0)
    	{
    		placeholder = larger.divide(smaller);
    		iterations = iterations.add(placeholder);
    		larger = larger.subtract(placeholder.multiply(smaller));
    		
    		if(larger.compareTo(smaller) < 0){
    			placeholder = smaller;
	    		smaller = larger;
	    		larger = placeholder;
    		}
    	}
    	
    	if((larger.compareTo(BigInteger.ONE) == 0 || smaller.compareTo(BigInteger.ONE) == 0) && iterations.compareTo(BigInteger.ZERO) > 0)
    		retVal = iterations.subtract(BigInteger.ONE).toString();
        // Your code goes here.
    	return retVal;
    }
    
    
    
    
    
    
    
}
