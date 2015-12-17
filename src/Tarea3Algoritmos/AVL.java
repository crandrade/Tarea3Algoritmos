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
					root = rebalance(root, LEFT,SINGLE);
				}
				else root = rebalance(root, LEFT,DOUBLE);
			}
		}
		else if (root.key < key){
			root.getRight().insert(key, value);
			if(Math.abs(root.getLeft().height() - root.getRight().height())==2){
				if(key > root.getRight().root.key){
					root = rebalance(root, RIGHT,SINGLE);
				}
				else root = rebalance(root, RIGHT,DOUBLE);
			}
		}
	}
	private int height(){
		return root == null? 0: 1 + Math.max(root.getLeft().height(),root.getRight().height());
	}
	private Node rebalance(Node r, int side, int times){
		Node auxleft, auxright;
		if(side == LEFT){
			if(times==SINGLE){
				auxleft = r.getLeft().root;
				r.setLeft(auxleft.getRight());
				auxleft.setRight(new AVL(r));
				return auxleft;
			}
			else{
				r.setLeft(new AVL(rebalance(r.getLeft().root,RIGHT, SINGLE)));
				return rebalance(r,LEFT, SINGLE);
			}
		}
		else{
			if(times == SINGLE){
				auxright = r.getRight().root;
				r.setRight(auxright.getLeft());
				auxright.setLeft(new AVL(r));
				return auxright;
			}
			else{
				r.setRight(new AVL(rebalance(r.getRight().root,LEFT, SINGLE)));
				return rebalance(r,RIGHT, SINGLE);	
			}
		}
		
	}
	public boolean isLeaf(){
		return root.getLeft().isEmpty() && root.getRight().isEmpty();
	}
	public boolean isEmpty(){
		return root == null;
	}
	private Node extractMin(){
		AVL aux = new AVL(root);
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
	public void delete(int key) {
		if(root == null) return;
		if(root.key > key){
			root.getLeft().delete(key);
			if(Math.abs(root.getLeft().height()-root.getRight().height())==2){
				if(root.getLeft().root != null && key < root.getLeft().root.key){
					root = rebalance(root, RIGHT,SINGLE);
				}
				else root = rebalance(root, LEFT,SINGLE);
			}
		}
		else if (root.key < key){
			root.getRight().delete(key);
			if(Math.abs(root.getLeft().height() - root.getRight().height())==2){
				if(root.getRight().root != null && key > root.getRight().root.key){
					root = rebalance(root, LEFT,SINGLE);
				}
				else root = rebalance(root, RIGHT,SINGLE);
			}
		}
		else{ // found it
			if(isLeaf()){
				root = null;
			}
			else{
				if(!root.getLeft().isEmpty() && !root.getRight().isEmpty()){
					Node aux = root.getRight().extractMin();
					aux.setLeft(root.getLeft());
					aux.setRight(root.getRight());
					if(root.getRight().height()-root.getLeft().height() >=2){
						root = rebalance(aux, RIGHT, SINGLE);
					}
					root = aux; 
				}
				else if(root.getLeft().isEmpty()){
					root = root.getRight().root;
				}
				else{
					root = root.getLeft().root;
				}
			}
		}
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
		a.insert(13, 4);
		a.insert(14, 4);
		a.insert(15, 4);
		a.insert(16, 4);
		a.insert(0, 4);
		System.out.println(a.size());
		a.delete(9);
		System.out.println(a.size());
		a.delete(11);
		a.delete(5);
		a.delete(7);
		a.delete(5);
		System.out.println(a.size());
	}

}
