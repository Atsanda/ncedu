package consolecalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Artyom Tsanda
 * @version 1.0
 * 
 */
public class ConsoleCalculator {
	private Scanner input = new Scanner(System.in);
	private Deque<Double> stack = new ArrayDeque<Double>();
	private List<Conversion> conversions = new LinkedList<Conversion>();

	/**
	 * Creates an instance of ConsoleCalculator<br/>
	 * Also instances classes of all operations calculator uses according to
	 * configurations list<br/>
	 * Note: all operations are working in postfix format, except binaric operations, for them two variants(ab+;a+b) appropriate/
	 * At one line must be only one: number or operation
	 */
	public ConsoleCalculator() {
		String confPath = "./src/consolecalculator/configurations.txt";

		try (Scanner fileinput = new Scanner(new File(ConsoleCalculator.class.getResource("configurations.txt").getFile()))) {
			Class<?> conversionClass;
			while (fileinput.hasNextLine()) {
				conversionClass = Class.forName(fileinput.nextLine());
				Conversion conv = (Conversion) conversionClass.newInstance();
				conversions.add(conv);
			}
			// check for symbol uniqueness
			Set<String> operationSymbols = new HashSet<String>();
			for (Conversion tmp : conversions) {
				if (!operationSymbols.add(tmp.getSymbol()))
					throw new Exception("operation symbols conflict, it can't be two operations with one symbol");
			}
			if (!operationSymbols.add("q") && !operationSymbols.add("clear"))
				throw new Exception("operation symbols conflict, it can't be two operations with one symbol");

			operationSymbols = null;
		} catch (FileNotFoundException e) {
			output("Configuration file error: file could not be found on the path :" + confPath + "\n");
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			output("Configuration file error: class \"" + e.getMessage() + "\" could not be found\n");
			output("To add new class operation you should put in a new line in format: [packagename].[classname], package must be local");
			System.exit(-1);
		} catch (Exception e) {
			output("Configuration file error: " + e.getMessage() + "\n");
			System.exit(-1);
		}
	}

	/**
	 * Launches calculator.
	 */
	public void proceed() {
		output("Welcome to ConsoleCalculator 1.0\n");
		boolean toThrow = true;
		try {
			String literal;
			double res;
			while (true) {
				output(">");
				toThrow = true;
				literal = getLiteral();

				if (literal.compareTo("q") == 0) {
					System.exit(0);
				}

				if (literal.compareTo("clear") == 0) {
					stack.clear();
					continue;
				}

				//if_number
				try {
					stack.push(Double.parseDouble(literal));
					continue;
				} catch (NumberFormatException e) {}

				for (Conversion tmp : conversions) {

					if (tmp.isThisConversion(literal)) {

						if (stack.size() == 1 && tmp.operandsNum() == 2) {
							output(">");
							literal = getLiteral();
							try{
								stack.push(Double.parseDouble(literal));
							}catch (NumberFormatException e){
								throw new Exception("Invalid input data format");
							}
						}
						res = tmp.converse(stack);
						if (stack.size() != 1)
							throw new Exception("Invalid input data format: at one line must be only one: number or operation\n" +
									"\t\t\t\t numbers amount should be equivalent to function arguments amount\n");

						output("!>");
						output(res);
						toThrow = false;
					}
				}

				if (toThrow)
					throw new Exception("Invalid input data format: at one line must be only one: number or operation\n" +
							"\t\t numbers amount should be equivalent to function arguments amount\n");
			}

		} catch (Exception e) {
			output("Error: " + e.getMessage());
			System.exit(-1);
		}
	}

	private String getLiteral() {
		while (!input.hasNextLine()) {
		}

		return input.nextLine();
	}

	private void output(String str) {
		System.out.print(str);
	}

	private void output(Double num) {
		System.out.println(num);
	}

	public static void main(String[] args) {

		ConsoleCalculator a = new ConsoleCalculator();
		a.proceed();
	}
}
