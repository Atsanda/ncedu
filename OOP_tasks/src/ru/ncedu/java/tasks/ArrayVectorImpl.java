package ru.ncedu.java.tasks;
import java.util.Arrays;;

public class ArrayVectorImpl implements ArrayVector{
	private double array[];
	
	/**
     * Задает все элементы вектора (определяет длину вектора).
     * Передаваемый массив не клонируется.
     * @param elements Не равен null
     */
	public void set(double... elements) {
		array = new double[elements.length];
		array = elements;
	}

	/**
     * Возвращает все элементы вектора. Массив не клонируется.
     */
	public double[] get() {
		return array;
	}
	/**
     * Возвращает копию вектора (такую, изменение элементов 
     *  в которой не приводит к изменению элементов данного вектора).<br/>
     * Рекомендуется вызвать метод clone() у самого массива или использовать
     *   {@link System#arraycopy(Object, int, Object, int, int)}.
     */
	public ArrayVector clone() {
		double copy[] = new double[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		ArrayVector ret = new ArrayVectorImpl();
		ret.set(copy);
		return ret;
	}

	/**
     * Возвращает число элементов вектора.
     */
	public int getSize() {
		return array.length;
	}

	/**
     * Изменяет элемент по индексу. 
     * @param index В случае выхода индекса за пределы массива:<br/>
     *  а) если index<0, ничего не происходит;<br/>
     *  б) если index>=0, размер массива увеличивается так, чтобы index стал последним.
     */
	public void set(int index, double value) {
		if(index < 0)
			return;
		else if(index < array.length)
			array[index] = value;
		else{
			array = Arrays.copyOf(array, index+1);
			array[index] = value;
		}
	}

	/**
     * Возвращает элемент по индексу.
     * @param index В случае выхода индекса за пределы массива 
     *   должно генерироваться ArrayIndexOutOfBoundsException
     */
	public double get(int index) throws ArrayIndexOutOfBoundsException {
		if( index >= array.length )
			throw new ArrayIndexOutOfBoundsException();
		else
			return array[index];
	}

	/**
     * Возвращает максимальный элемент вектора.
     */
	public double getMax() {
		double ret = array[0];
		for(int i=0; i<array.length; i++)
			ret = Math.max(ret, array[i]);
		return ret;
	}

	/**
     * Возвращает минимальный элемент вектора.
     */
	public double getMin() {
		double ret = array[0];
		for(double x: array)
			ret = Math.min(ret, x);
		return ret;
	}

    /**
     * Сортирует элементы вектора в порядке возрастания.
     */
	public void sortAscending() {
		Arrays.sort(array);
	}

	/**
     * Умножает вектор на число.<br/>
     * Замечание: не пытайтесь использовать безиндексный цикл foreach: 
     *  для изменения элемента массива нужно знать его индекс. 
     * @param factor
     */
	public void mult(double factor) {
		for(int i=0; i<array.length; i++)
			array[i] *= factor;
	}

	/**
     * Складывает вектор с другим вектором, результат запоминает в элементах данного вектора.<br/>
     * Если векторы имеют разный размер, последние элементы большего вектора не учитываются<br/>
     *  (если данный вектор - больший, его размер менять не надо, просто не меняйте последние элементы).
     * @param anotherVector Не равен null
     * @return Ссылка на себя (результат сложения)
     */
	public ArrayVector sum(ArrayVector anotherVector) {
		for(int i=0; i<array.length && i<anotherVector.getSize(); i++ )
				array[i] += anotherVector.get(i);
		
		return this;
	}

	/**
     * Возвращает скалярное произведение двух векторов.<br/>
     * Если векторы имеют разный размер, последние элементы большего вектора не учитываются.
     * @param anotherVector Не равен null
     */
	public double scalarMult(ArrayVector anotherVector) {
		double sum = 0;
		for(int i=0; i<array.length && i<anotherVector.getSize(); i++ )
			sum += array[i]*anotherVector.get(i);
		return sum;
	}

	/**
     * Возвращает евклидову норму вектора (длину вектора 
     *  в n-мерном евклидовом пространстве, n={@link #getSize()}).
     * Квадрат нормы вектора равен скалярному произведению вектора на себя.
     */
	public double getNorm() {
		return Math.sqrt(scalarMult(this));
	}

}
