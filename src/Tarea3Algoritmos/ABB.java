package Tarea3Algoritmos;

import java.io.*;
import org.junit.*;
import com.javamex.classmexer.*;

public class ABB implements GenericTree {
	private Node root;
	
	private class Node{
		@SuppressWarnings("rawtypes")
		private Comparable key;
		private Node left, right;
		
		@SuppressWarnings("rawtypes")
		public Node(Comparable i, Node left, Node right){
			this.key = i;
			this.setLeft(left);
			this.setRight(right);
		}
		public Node getRight() {
			return right;
		}
		public void setRight(Node rigth) {
			this.right = rigth;
		}
		public Node getLeft() {
			return left;
		}
		public void setLeft(Node left) {
			this.left = left;
		}
	}
	
	public ABB() {
		// TODO Auto-generated constructor stub
		root = null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable get(Comparable key) {
		// TODO Auto-generated method stub
		Node aux = root;
		while(aux != null){
			int i = aux.key.compareTo(key);
			if(i == 0){
				return aux.key;
			}
			else if(i < 0)
				aux = aux.getLeft();
			else
				aux = aux.getRight();
		}
		return aux != null? aux.key : null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void insert(Comparable key){
		// TODO Auto-generated method stub
		if(root==null)
			root = new Node(key, null, null);
		else{
			for(Node aux=root; aux!=null;){
				if(key.compareTo(aux.key)<=0){
					if(aux.getLeft()!=null){
						aux = aux.getLeft();
						continue;
					}
					else{
						aux.setLeft(new Node(key, null, null));
						return;
					}
				}
				else{
					if(aux.getRight()!=null){
						aux = aux.getLeft();
						continue;
					}
					else{
						aux.setRight(new Node(key, null, null));
						return;
					}
				}
			}
		}
		return;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void delete(Comparable key) {
		// TODO Auto-generated method stub
		root = delete(key, root);
	}
	private Node delete(Comparable key, Node node){
		if(node != null){
			if(root.key.compareTo(key)==0){
				Node aux = root.getLeft();
				if(aux == null){
					return root.getRight();
				}
				while(aux.getRight() != null)
					aux = aux.getRight();
				aux.setRight(root.getRight());
				return aux;
			}
			else if(root.key.compareTo(key)<0){
				
			}
		}
		return null;
	}

	public long size(){
		return MemoryUtil.deepMemoryUsageOf(this);
	}

	private long sizeR(Node n){
		if(n==null){
			return 0;
		}
		return this.sizeR(n.getLeft())+sizeR(n.getRight());
	}
	
	static public void main(String [] args) throws IOException{
		ABB a = new ABB();
		a.insert(new Integer(1));
		a.insert(new Integer(2));
		a.insert(new Integer(0));
		a.insert(new Integer(4));
		System.out.println(a.size());
		System.out.println(a.root.key);
		a.delete(new Integer(1));
		System.out.println(a.root.key);
		a.delete(new Integer(0));
		System.out.println(a.root.key);
		a.delete(new Integer(4));
		System.out.println(a.root.key);
		a.delete(new Integer(2));
		System.out.println(a.size());
		a.delete(new Integer(5));
	}
	
}
