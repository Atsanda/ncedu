package ru.ncedu.java.tasks;

import ru.ncedu.java.tasks.ControlFlowStatements1.BankDeposit;

public class ControlFlowStatements1Impl implements ControlFlowStatements1{

	
	public float getFunctionValue(float x) {
		if(x>0)
			return (float) (2*Math.sin((double) x));
		else
			return 6-x;
	}

	/**
	 * Дано целое число в диапазоне 1–7.
	 * Вернуть строку — название дня недели, соответствующее этому числу:<br/>
	 * 1 — Monday, 2 — Tuesday, 3 - Wednesday, 4 - Thursday, 5 - Friday, 6 - Saturday, 7 - Sunday.
	 * @param weekday
	 * @return строковое представление weekday
	 */
	public String decodeWeekday(int weekday) {
		switch(weekday){
		case 1:
			return "Monday";
		case 2:
			return "Tuesday";
		case 3:
			return "Wednesday";
		case 4:
			return "Thursday";
		case 5: 
			return "Friday";
		case 6:
			return "Saturday";
		case 7:
			return "Sunday";
			default:
				return null;
					
		}
	}

	/**
	 * Создать двумерный массив, содержащий 8x5 целочисленных элементов,
	 * и присвоить каждому элементу произведение его индексов: array[i][j] = i*j.
	 * @return массив.
	 */
	public int[][] initArray() {
		int ret[][] = new int[8][5];
		for(int i=0; i<8; i++)
			for(int j=0; j<5; j++)
				ret[i][j] = i*j;
		return ret;
	}

	/**
	* Найти минимальный элемент заданного двумерного массива.
	* @param array массив, содержащий как минимум один элемент
	* @return минимальный элемент массива array.
	*/
	public int getMinValue(int[][] array) {
		int ret = array[0][0];
		
		for(int i=0; i < array.length; i++)
			for(int j=0; j < array[i].length; j++ )
				ret = Math.min(ret, array[i][j]);
		
		return ret;
	}

	/**
	 * Начальный размер вклада в банке равен $1000. 
	 * По окончанию каждого года размер вклада увеличивается на P процентов (вклад с капитализацией процентов).<br/>
	 * По заданному P определить, через сколько лет размер вклада превысит $5000, а также итоговый размер вклада.
	 * @param P процент по вкладу
	 * @return информация о вкладе (в виде экземпляра класса {@link BankDeposit}) после наступления вышеуказанного условия
	 */
	public BankDeposit calculateBankDeposit(double P) {
		BankDeposit ret = new BankDeposit();
		ret.amount = 1000;
		ret.years = 0;
		while(true)
		{
			ret.amount *= (P*0.01 + 1);
			++ret.years;
			if(ret.amount > 5000)
				break;
		}
		return ret;
	}
	
	//public static void main(String args[]){}
}
	