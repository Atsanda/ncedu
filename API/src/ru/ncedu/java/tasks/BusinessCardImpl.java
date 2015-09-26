package ru.ncedu.java.tasks;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BusinessCardImpl implements BusinessCard {
	
	public BusinessCardImpl(){}
	
	BusinessCardImpl(String name, String lastName, String department, String birthDate, char gender, int salary,String phoneNumber, Calendar brthDate){
		this.name = name;
		this.lastName = lastName;
		this.department = department;
		this.birthDate = birthDate;
		this.gender = gender;
		this.salary = salary;
		this.brthDate = brthDate;
		this.phoneNumber = phoneNumber;
	}

	String name;
	String lastName;
	String department; 
	String birthDate; //"DD-MM-YYYY"
	char gender; //F or M 
	int salary; // number from 100 to 100000 Phone number : 10-digits number
	String phoneNumber;
	private Calendar brthDate;

	@Override
	public BusinessCard getBusinessCard(Scanner scanner) {
		scanner.useDelimiter(";");
		
		String nameArg = null;
		String lastNameArg = null;
		String departmentArg = null; 
		String birthDateArg = null; //"DD-MM-YYYY"
		char genderArg = 'a'; //F or M 
		int salaryArg = -1; // number from 100 to 100000 Phone number : 10-digits number
		String phoneNumberArg = null;
		Calendar brthDateArg = null;
		
		if (scanner.hasNext()){
			nameArg = scanner.next();
		}
		if (scanner.hasNext())
			lastNameArg = scanner.next();
		if (scanner.hasNext())
			departmentArg = scanner.next();
		if (scanner.hasNext()){
			birthDateArg = scanner.next();
			brthDateArg = this.ValidDate( birthDateArg, "dd-MM-yyyy");
			if ( brthDateArg == null )
				throw new InputMismatchException();
		}
		if (scanner.hasNext()){
			String gndr = scanner.next();
			if (!(gndr.equals("F") || gndr.equals("M")))
				throw new InputMismatchException();
			genderArg = gndr.charAt(0);
		}
		if (scanner.hasNext()){
			String numb = scanner.next();
			try{
			salaryArg = Integer.parseInt(numb);
			}catch(NumberFormatException nfe){
				throw new InputMismatchException();
			}
			if (salaryArg < 100 || salaryArg > 100000 )
				throw new InputMismatchException();
		}
		if (scanner.hasNext()){
			String numb = scanner.next();
			if(numb.length() != 10)
				throw new InputMismatchException();
			phoneNumberArg = numb.substring(0,10);
		}
		if (phoneNumberArg == null)
			throw new NoSuchElementException();
		
		return new BusinessCardImpl(nameArg, lastNameArg, departmentArg, birthDateArg, genderArg, salaryArg,phoneNumberArg, brthDateArg);
	}
	
	private Calendar ValidDate(String stringToValidate, String format)
	{
		if(stringToValidate == null)
			return null;
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		
		Date date;
		
		try{
			date = sdf.parse(stringToValidate);
		}catch (ParseException e){
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
		
		return cal;
	}

	@Override
	public String getEmployee() {
		return this.name + " " + this.lastName;
	}

	@Override
	public String getDepartment() {
		return this.department;
	}

	@Override
	public int getSalary() {
		return this.salary;
	}

	@Override
	public int getAge() {
		long currentTime = System.currentTimeMillis();
	     Calendar now = Calendar.getInstance();
	     now.setTimeInMillis(currentTime);
		return now.get(Calendar.YEAR) - brthDate.get(Calendar.YEAR);
	}

	@Override
	public String getGender() {
		return (this.gender == 'F')?("Female"):("Male");
	}

	@Override
	public String getPhoneNumber() {
		String number = this.phoneNumber;
		return "+7 "+ number.substring(0,3) +
				"-" + number.substring(3,6) +
				"-" + number.substring(6,8) +
				"-" + number.substring(8,10);
	}
	
	public static void main(String[] args) throws IOException{
		File f = new File("tst.txt");
		FileReader fin = new FileReader(f);
		Scanner src = new Scanner(fin);
		BusinessCard crd = new BusinessCardImpl();
		crd = crd.getBusinessCard(src);
		System.out.println(crd.getAge());
		System.out.println(crd.getDepartment());
		System.out.println(crd.getEmployee());
		System.out.println(crd.getGender());
		System.out.println(crd.getPhoneNumber());
		System.out.println(crd.getSalary());
		src.close();
		fin.close();
	}

}
