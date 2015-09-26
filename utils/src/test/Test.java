package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

class SuperBase{}
class Base extends SuperBase{}
class Derived extends Base{}
class CovariantTest1 {
	public Base getIt() {
		return new Base();
	}
}


public class Test extends CovariantTest1{
	//public SuperBase getIt() { return new Derived(); }	
	//public SuperBase getIt() { return new Base(); }	
	//public Base getIt() { return new Derived(); }	
	//public Derived getIt() { return new Base(); }	
	public Derived getIt() { return new Derived(); }
	
	public static void main(String[] args){
		
	}
}
