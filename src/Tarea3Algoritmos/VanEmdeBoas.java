package Tarea3Algoritmos;

public class VanEmdeBoas extends GenericTree {
	private Node root;
	
	private class Node extends GenericNode{
		private int key, value;
		private Node left, right;
		
		public Node(int key, int value){
			this.key = key;
			this.value = value;
			this.setLeft(null);
			this.setRight(null);
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
	
	public VanEmdeBoas() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericNode find(int key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(int key, int value) {
		// TODO Auto-generated method stub
		Node aux = new Node(key,value);
		root = aux;
	}

	@Override
	public void delete(int key) {
		// TODO Auto-generated method stub
		return;
	}

}
