package consolecalculator.basicoperations;

import java.util.Deque;
import java.util.NoSuchElementException;

import consolecalculator.Conversion;

public class Sqrt implements Conversion{

	@Override
	public double converse(Deque<Double> stack) throws IllegalStateException,
			ArithmeticException {
		try{
			double val = stack.pop();
			
			if(val < 0)
				throw new ArithmeticException("Square root  is not defined for negative real numbers");
			
			stack.push(val);
			
			return Math.sqrt(val);
		}catch(NoSuchElementException e){
			throw new IllegalStateException("Not enough arguments for square root operation");
		}
	}

	@Override
	public boolean isThisConversion(String symbol) {
		return symbol.compareTo("sqrt") == 0;
	}

	@Override
	public int operandsNum() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "sqrt";
	}

}
