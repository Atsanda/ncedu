package consolecalculator.basicoperations;

import java.util.Deque;
import java.util.NoSuchElementException;

import consolecalculator.Conversion;

public class Plus implements Conversion{

	@Override
	public double converse(Deque<Double> stack) throws IllegalStateException, ArithmeticException{
		try{ 
			double res = stack.pop() + stack.pop();
			stack.push(res);
			return res;
		}catch(NoSuchElementException e){
			throw new IllegalStateException("Not enough arguments for plus operation");
		}
	}

	@Override
	public boolean isThisConversion(String symbol) {
		return symbol.compareTo("+") == 0;
	}

	@Override
	public int operandsNum() {
		return 2;
	}

	@Override
	public String getSymbol() {
		return "+";
	}

}
