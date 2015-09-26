package com.netcracker.edu.java.tasks;

public class Main {
	public static void main(String[] args) throws CloneNotSupportedException{

		//java.lang.AssertionError: Failed to find a child with not-null data: expected:<2> but was:<null>
		
		TreeNode root = new TreeNodeImpl();
		root.setData("root");
		
		TreeNode node1 = new TreeNodeImpl();//null node
		root.addChild(node1);
		
		TreeNode node2 = new TreeNodeImpl();
		node1.setData(2.0);
		root.addChild(node2);
		
		TreeNode node3 = new TreeNodeImpl();
		root.addChild(node3);
		node3.setData(3);
		
		System.out.print("findChild(!=null) : ");
		System.out.println(root.findChild(2.0));
		
		System.out.print("findChild(==null) : ");
		System.out.println(root.findChild(null));
	}

}
