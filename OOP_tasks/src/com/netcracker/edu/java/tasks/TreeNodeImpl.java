package com.netcracker.edu.java.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeNodeImpl implements TreeNode{
	private List<TreeNode> children;
	private TreeNode parent;
	private boolean expanded = false;
	private Object data;
	
	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public void setParent(TreeNode parent) {
		this.parent = parent;	
	}

	@Override
	public TreeNode getRoot() {
		TreeNode tmp = this;
		while(tmp.getParent() !=null )
			tmp = tmp.getParent();
		
		if(tmp == this)
			return null;
		else
			return tmp;
	}
	
	@Override
	public boolean isLeaf() {
		if (children != null){
			return children.isEmpty();
		}
		return true;
	}

	@Override
	public int getChildCount() {
		if (children != null){
			return children.size();
		}
		return 0;
	}

	@Override
	public Iterator<TreeNode> getChildrenIterator() {
		if (children != null){
			return children.iterator();
		}
		return null;
	}

	@Override
	public void addChild(TreeNode child) {
		child.setParent(this);
		if (children != null){
			children.add(child);
		}else {
			children = new ArrayList<TreeNode>(); 
			children.add(child);
		}
	}

	@Override
	public boolean removeChild(TreeNode child) {
		if (children == null)
			return false;
		if (children.remove(child)){
			child.setParent(null);
			return true;
		} else{
			return false;
		}
	}

	@Override
	public boolean isExpanded() {
		return expanded;
	}

	@Override
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
		for(TreeNode x: children)
			x.setExpanded(expanded);
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String getTreePath() {
		if (parent == null){
			return this.toString();
		}else {
			return parent.getTreePath() + "->" +  this.toString();
		}
	}
		
	public String toString() {
		if (data == null){
			return "empty";
		}else{
			return data.toString();
		}
	}

	@Override
	public TreeNode findParent(Object data) {
		if(data != null){
			if (data.equals(this.data)){
				return this;
			}else if( data.equals(this.data) || this.parent == null){
				return null;
			}else{
				return parent.findParent(data);
			}
		}else{
			if(this.data == null){
				return this;
			}
			if(this.parent != null){
				return parent.findParent(data);
			}
			return null;
		}
	}

	@Override
	public TreeNode findChild(Object data) {
		if (children == null)
			return null;
		for(TreeNode x: children){
			if ( data != null && data.equals(x.getData()))
				return x;
			if ( data == null && x.getData() == null){
				return x;
			}
		}
		for(TreeNode x: children)
			return x.findChild(data);
		return null;
	}

}
