package com.netcracker.edu.java.tasks;

import java.util.Arrays;
import java.util.Comparator;

public class ComplexNumberImpl implements ComplexNumber{
	private double real;
	private double imag;
	
	public ComplexNumberImpl(){
		real = 0;
		imag = 0;
	}
	
	public ComplexNumberImpl( double re, double im){
		this.real = re;
		this.imag = im;
	}

	@Override
	public double getRe() {
		return real;
	}

	@Override
	public double getIm() {
		return imag;
	}

	@Override
	public boolean isReal() {
		return (imag == 0);
	}

	@Override
	public void set(double re, double im) {
		this.real = re;
		this.imag = im;
	}

	@Override
	public void set(String value) throws NumberFormatException {
		int imagpartend = value.indexOf("i");
		if( imagpartend == -1){ //number is real
			real = Double.parseDouble(value);
			imag = 0;
			return;
		}
		if( imagpartend != value.length()-1){	//there must be no numbers after i
			throw new NumberFormatException();
		}
		int j;//needs after circle
		for(j=1; imagpartend-j >= 0 && (Character.isDigit( value.charAt(imagpartend-j)) || value.charAt(imagpartend-j) == '.') ; j++){}
		int imagpartbegin = imagpartend-j+1;
		if(imagpartbegin == 0){ //number is "*i"
			real = 0;
			if( imagpartbegin == imagpartend){
				imag = Double.parseDouble( value.substring(0,imagpartend) + "1");
			}else{
				imag = Double.parseDouble( value.substring(0,imagpartend) );
			}
			return;
		}else{
			if( imagpartbegin == imagpartend){
				imag = Double.parseDouble(value.substring(imagpartbegin-1,imagpartend) + "1" ); //number is "*[+-]i"
			}
			else{
				imag = Double.parseDouble(value.substring(imagpartbegin-1,imagpartend));//number is "*[+-]*i
			}
			
			if(imagpartbegin-1 != 0){
				real = Double.parseDouble(value.substring(0,imagpartbegin-1));
			}else{
				real = 0;
			}
			return;
		}
	}

	@Override
	public ComplexNumber copy() {
		ComplexNumber ret = new ComplexNumberImpl(real, imag);
		return ret; 
	}
	
	@Override
	public ComplexNumber clone() throws CloneNotSupportedException {
		return (ComplexNumber) super.clone();	
	}
	
	@Override
	public String toString()
	{
		if (Double.compare(imag, 0) == 0){
			return Double.toString(real);
		}
        if (Double.compare(real, 0) == 0){
        	return Double.toString(imag) + "i";
        }
        if (imag > 0){
        	return Double.toString(real) + "+" + Double.toString(imag) + "i";
        }
        return Double.toString(real) + Double.toString(imag) + "i";
	}
	
	@Override
	public boolean equals(Object other){
		if ( other == null ){
			return false;
		}else if (!(other instanceof ComplexNumber)){
			return false;
		}else {
			ComplexNumber othercomplexnumber = (ComplexNumber) other;
			return (Double.compare(real, othercomplexnumber.getRe()) == 0) && (Double.compare(imag, othercomplexnumber.getIm()) == 0);
		}
	}

	@Override
	public int compareTo(ComplexNumber other) {
		if(Math.hypot(real, imag) - Math.hypot(other.getRe(), other.getIm()) > 0)
			return 1;
		else if(Math.hypot(real, imag) - Math.hypot(other.getRe(), other.getIm()) < 0)
			return -1;
		else
			return 0;
	}

	@Override
	public void sort(ComplexNumber[] array) {
		Arrays.sort(array, new Comparator<ComplexNumber>(){

			@Override
			public int compare(ComplexNumber arg0, ComplexNumber arg1) {
				return arg0.compareTo(arg1);
			}
			
		});
	}
	

	@Override
	public ComplexNumber negate() {
		imag *= (-1);
		real *= (-1);
		return this;
	}

	@Override
	public ComplexNumber add(ComplexNumber arg2) {
		real += arg2.getRe();
		imag += arg2.getIm();
		return this;
	}

	@Override
	public ComplexNumber multiply(ComplexNumber arg2) {
		double oldreal = real;
		real = real * arg2.getRe() - imag * arg2.getIm();
		imag = oldreal * arg2.getIm() + imag * arg2.getRe();
		return this;
	}

}
