package Tarea3Algoritmos;

import java.io.IOException;

public class AVL extends GenericTree {
	private Node root;
	private static final int LEFT = 0, RIGHT = 1, SINGLE=1, DOUBLE = 2;
	
	private class Node extends GenericNode{
		private AVL left, right;
		
		public Node(int i, int j){
			this.key = i;
			this.value = j;
			this.setLeft(new AVL());
			this.setRight(new AVL());
		}
		public AVL getRight() {
			return right;
		}
		public void setRight(AVL rigth) {
			this.right = rigth;
		}
		public AVL getLeft() {
			return left;
		}
		public void setLeft(AVL left) {
			this.left = left;
		}
	}
	
	public AVL(){
		root = null;
	}
	public AVL(Node root){
		this.root = root;
	}

	@Override
	public GenericNode find(int key) {
		return findAVL(key).root;
	}
	private AVL findAVL(int key){
		if(root != null){
			if(root.key == key){
				return this;
			}
			else if(root.key > key){
				return root.getLeft().findAVL(key);
			}
			else 
				return root.getRight().findAVL(key);
		}
		else return this;
	}

	@Override
	public void insert(int key, int value) {
		Node aux = new Node (key, value);
		if(root==null){
			root = aux;
		}
		else if(root.key > key){
			root.getLeft().insert(key,value);
			if(Math.abs(root.getLeft().height()-root.getRight().height())==2){
				if(key < root.getLeft().root.key){
					rebalance(LEFT,SINGLE);
				}
				else rebalance(LEFT,DOUBLE);
			}
		}
		else if (root.key < key){
			root.getRight().insert(key, value);
			if(Math.abs(root.getLeft().height() - root.getRight().height())==2){
				if(key > root.getRight().root.key){
					rebalance(RIGHT,SINGLE);
				}
				else rebalance(RIGHT,DOUBLE);
			}
		}
	}
	private int height(){
		return root == null? 0: 1 + root.getLeft().height() + root.getRight().height();
	}
	private void rebalance(int side, int times){
		AVL auxleft, auxright;
		if(side == LEFT){
			if(times==SINGLE){
				auxleft = root.getLeft();
				root.setLeft(auxleft.root.getRight());
				auxright = new AVL(this.root);
				auxleft.root.setRight(auxright);
				root = auxleft.root;
				return;
			}
			else{
				root.getLeft().rebalance(RIGHT, SINGLE);
				this.rebalance(LEFT, SINGLE);
			}
		}
		else{
			if(times == SINGLE){
				auxright = root.getRight();
				root.setRight(auxright.root.getLeft());
				auxleft = new AVL(this.root);
				auxright.root.setLeft(auxleft);
				root = auxright.root;
				return;
			}
			else{
				root.getRight().rebalance(LEFT, SINGLE);
				this.rebalance(RIGHT, SINGLE);	
			}
		}
		
	}
	
	public boolean isLeaf(){
		return root.getLeft().isEmpty() && root.getRight().isEmpty();
	}
	public boolean isEmpty(){
		return root == null;
	}
	@Override
	public void delete(int key) {

		return;
	}
	
	static public void main(String [] args) throws IOException{
		AVL a = new AVL();
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
		System.err.println((a.find(5)).getValue());
		a.delete(5);
		System.out.println(a.size());
	}

}
