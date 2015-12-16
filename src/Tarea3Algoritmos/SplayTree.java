package Tarea3Algoritmos;

public class SplayTree extends GenericTree {
	private Node root;
	private class Node{
		@SuppressWarnings("rawtypes")
		private Comparable key;
		private Node left, right, father;
		
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
	
	public SplayTree() {
		// TODO Auto-generated constructor stub
		root = null;
	}

	@Override
	public GenericNode find(int key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(int key, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int key) {
		// TODO Auto-generated method stub
		return;
	}
	
	public void splay(Node n){
		
	}

}
