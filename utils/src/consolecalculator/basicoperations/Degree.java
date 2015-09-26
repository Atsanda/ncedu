package consolecalculator.basicoperations;

import java.util.Deque;
import java.util.NoSuchElementException;

import consolecalculator.Conversion;

public class Degree implements Conversion{
	
	@Override
	public double converse(Deque<Double> stack) throws IllegalStateException, ArithmeticException{
		try{ 
			double degree = stack.pop();
			double basis  = stack.pop();
			if( degree == 0 && basis == 0)
				throw new ArithmeticException("0^0 is undefined");
			double res = Math.pow(basis, degree);
			stack.push(res);
			return res;
		}catch(NoSuchElementException e){
			throw new IllegalStateException("Not enough arguments for degree operation");
		}
	}

	@Override
	public boolean isThisConversion(String symbol) {
		return symbol.compareTo("^") == 0;
	}

	@Override
	public int operandsNum() {
		return 2;
	}

	@Override
	public String getSymbol() {
		return "^";
	}
	
}
