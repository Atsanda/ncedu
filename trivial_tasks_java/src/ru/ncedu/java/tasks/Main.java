package ru.ncedu.java.tasks;
interface a{
	int b();
}
abstract class C a{}
class A{
	String x = "A";
	public void get(){System.out.print("A " + x);}
}
class B extends A{
	String x = "B";
	public void get(){System.out.print("B " + x);}
}
public class Main {
	public static void main(String args[])
	{
		
		Integer i = new Integer(5);
		i = i + 1;
		System.out.println(i);
		Object a = new Object();
		a.equals("s");
	}

}
