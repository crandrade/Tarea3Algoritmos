package Tarea3Algoritmos;

import java.io.*;

public class ABB extends GenericTree {
	private Node root;
	
	private class Node extends GenericNode{
		private ABB left, right;
		
		public Node(int key, int value){
			this.key = key;
			this.value = value;
			this.setLeft(new ABB());
			this.setRight(new ABB());
		}
		public ABB getRight() {
			return right;
		}
		public void setRight(ABB rigth) {
			this.right = rigth;
		}
		public ABB getLeft() {
			return left;
		}
		public void setLeft(ABB left) {
			this.left = left;
		}
	}
	
	public ABB() {
		// TODO Auto-generated constructor stub
		root = null;
	}

	@Override
	public GenericNode find(int key) {
		return findABB(key).root;
	}
	private ABB findABB(int key){
		// TODO Auto-generated method stub
		if(root != null){
			if(root.key == key){
				return this;
			}
			else if(root.key > key){
				return root.getLeft().findABB(key);
			}
			else 
				return root.getRight().findABB(key);
		}
		else return this;
	}
	
	@Override
	public void insert(int key, int value){
		if(root==null){
			root = new Node(key, value);
		}
		else if(root.key > key){
			root.getLeft().insert(key,value);
		}
		else
			root.getRight().insert(key,value);
	}
	public boolean isLeaf(){
		return root.getLeft().isEmpty() && root.getRight().isEmpty();
	}
	public boolean isEmpty(){
		return root == null;
	}
	private Node extractMin(){
		ABB aux = this;
		while(!aux.isLeaf() 
				&& !aux.root.getLeft().isEmpty()){
			aux = aux.root.getLeft();
		}
		int key = aux.root.key;
		int value = aux.root.value;
		aux.root = aux.root.getRight().root;
		return new Node(key,value);
	}
	@Override	
	public void delete(int key){
		ABB toDelete = findABB(key);
		if(!toDelete.isEmpty()){
			if (toDelete.isLeaf()){
				toDelete.root = null;
			}
			else{
				if(!toDelete.root.getLeft().isEmpty() && !toDelete.root.getRight().isEmpty()){
					Node aux = toDelete.root.getRight().extractMin();
					aux.setLeft(toDelete.root.getLeft());
					aux.setRight(toDelete.root.getRight());
					toDelete.root = aux; 
				}
				else if(toDelete.root.getLeft().isEmpty()){
					toDelete.root = toDelete.root.getRight().root;
				}
				else{
					toDelete.root = toDelete.root.getLeft().root;
				}
			}
		}
	}
	
	
	static public void main(String [] args) throws IOException{
		ABB a = new ABB();
		System.out.println(a.size());
		a.insert(1, 1);
		a.insert(2, 2);
		a.insert(3, 0);
		a.insert(4, 4);
		a.insert(5, 5);
		a.insert(6, 4);
		a.insert(7, 4);
		a.insert(8, 4);
		a.insert(9, 4);
		a.insert(10, 4);
		a.insert(11, 4);
		a.insert(12, 4);
		a.insert(13, 4);
		a.insert(14, 4);
		a.insert(15, 4);
		a.insert(16, 4);
		a.insert(0, 4);
		System.out.println(a.size());
		a.delete(1);
		System.out.println(a.size());
		a.delete(0);
		System.err.println((a.find(5)).getValue());
		System.out.println(a.size());
		a.delete(2);
		System.err.println((a.find(5)).getValue());
		System.out.println(a.size());
		a.delete(4);
		System.out.println(a.size());
		System.err.println((a.find(4)).getValue());
		a.delete(4);
		System.out.println(a.size());
		System.err.println((a.find(5)).getValue());
		a.delete(5);
		System.out.println(a.size());
	}
	
}
