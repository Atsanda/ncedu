package consolecalculator.basicoperations;

import java.util.Deque;
import java.util.NoSuchElementException;

import consolecalculator.Conversion;

public class Division implements Conversion{

	@Override
	public double converse(Deque<Double> stack) throws IllegalStateException, ArithmeticException{
		try{ 
			double operand2 = stack.pop();
			
			if(operand2 == 0)
				throw new ArithmeticException("Division by zero is not allowed according to axioms of real numbers");
			
			double res = stack.pop() / operand2;
			stack.push(res);
			return res;
		}catch(NoSuchElementException e){
			throw new IllegalStateException("Not enough arguments for division operation");
		}
	}

	@Override
	public boolean isThisConversion(String symbol) {
		return symbol.compareTo("/") == 0;
	}

	@Override
	public int operandsNum() {
		return 2;
	}

	@Override
	public String getSymbol() {
		return "/";
	}

}
