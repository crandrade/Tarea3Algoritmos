package Tarea3Algoritmos;


public class AVL extends GenericTree {
	private Node root;

	private class Node extends GenericNode{
		private AVL left, right, father;
		
		public Node(int i, int j, AVL father){
			this.key = i;
			this.value = j;
			this.setLeft(new AVL());
			this.setRight(new AVL());
			this.setFather(father);
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
		public AVL getFather() {
			return father;
		}
		public void setFather(AVL father) {
			this.father = father;
		}
	}
	
	public AVL() {
		// TODO Auto-generated constructor stub
		root = null;
	}

	@Override
	public GenericNode find(int key) {
		// TODO Auto-generated method stub
		return root;
	}

	@Override
	public void insert(int key, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int key) {

		return;
	}

}
