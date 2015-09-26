package consolecalculator;

import java.util.Deque;

/**
 * Interface which all calculator operations and functions must implement
 * @author Artyom Tsanda
 *
 */
public interface Conversion {
	/**
	 * Makes converse<br/>
	 * Pops necessary arguments from the stack. 
	 * Makes conversion and pushes result to the stack. 
	 * After conversion in stack must be only one element
	 * @param stack
	 * @return result
	 * @throws IllegalStateException in case of shortage of arguments in stack
	 * @throws ArithmeticException in case of impossibility to do operation or function
	 */
	public double converse(Deque<Double> stack) throws IllegalStateException,
			ArithmeticException;

	/**
	 * @param symbol
	 * @return true if symbol defines this converse, otherwise false
	 */
	public boolean isThisConversion(String symbol);

	/**
	 * @return number of arguments
	 */
	public int operandsNum();

	/**
	 * @return operation or function marker
	 */
	public String getSymbol();
}
