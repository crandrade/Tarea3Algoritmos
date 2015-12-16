package Tarea3Algoritmos;

public class AVL extends GenericTree {
	private Node root;

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
		if(root==null){
			root = new Node(key, value);
			rebalance();
		}
		else if(root.key > key){
			root.getLeft().insert(key,value);
		}
		else
			root.getRight().insert(key,value);
	}
	private int height(){
		if(root == null){
			return 0;
		}
		return 1+(int)Math.max(root.getLeft().height(), root.getRight().height());
	}
	private void rebalance(){
		// TODO Auto-generated method stub
		this.height();
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

}
