package Tarea3Algoritmos;

public class VanEmdeBoas extends GenericTree {
	private Node root;
	
	private class Node extends GenericNode{
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

	}

	@Override
	public void delete(int key) {
		// TODO Auto-generated method stub
		return;
	}

}
