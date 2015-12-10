package Tarea3Algoritmos;

import Tarea3Algoritmos.ABB.Node;

public class VanEmdeBoas implements GenericTree {
	private Node root;
	private class Node{
		@SuppressWarnings("rawtypes")
		private Comparable key;
		private Node left, right, father;
		
		@SuppressWarnings("rawtypes")
		public Node(Comparable i, Node left, Node right, Node father){
			this.key = i;
			this.setLeft(left);
			this.setRight(right);
			this.setFather(father);
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
		public Node getFather() {
			return father;
		}
		public void setFather(Node father) {
			this.father = father;
		}
	}
	
	public VanEmdeBoas() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Comparable get(Comparable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(Comparable key) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean delete(Comparable key) {
		// TODO Auto-generated method stub
		return false;
	}

}
