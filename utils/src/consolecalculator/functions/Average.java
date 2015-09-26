package consolecalculator.functions;

import java.util.Deque;
import java.util.NoSuchElementException;

import consolecalculator.Conversion;

public class Average implements Conversion{

	@Override
	public double converse(Deque<Double> stack) throws IllegalStateException,
			ArithmeticException {
		Double sum = 0.0;
		int stackSize = stack.size();
		
		if(stackSize == 0)
			throw new IllegalStateException("Not enough arguments for average function");
			
		for(int i=0; i<stackSize; i++)
			sum += stack.pop();
			
		Double res =  sum/stackSize;
		stack.push(res);
		return res;
	}

	@Override
	public boolean isThisConversion(String symbol) {
		return symbol.compareTo("avr") == 0;
	}

	@Override
	public int operandsNum() {
		return -1; //can be 1 2 3 ...
	}

	@Override
	public String getSymbol() {
		return "avr";
	}

}
