package Tarea3Algoritmos;

import java.io.IOException;

@SuppressWarnings("unused")
public class VanEmdeBoas extends GenericTree {
	private Node root;
	private static int BASE_SIZE = 2;
	private static int NULL = -1;
	
	private class Node extends GenericNode{
		private Node [] cluster;
		private Node summary;
		private int universeSize;
		private int key, value;
		private int min, max, minvalue, maxvalue;
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
		private void setMin(int key, int value){
			this.min = key;
			this.minvalue = value;
		}
		private void setMax(int key, int value){
			this.max = key;
			this.maxvalue = value;
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
		if (root.min == NULL && root.max == NULL){
			root.setMin(key,value);
			root.setMax(key, value);
			return;
		}
		if(root.min == root.max){
			if(key < root.min){
				root.setMin(key, value);
			}
			if(key > root.max){
				root.setMax(key, value);
			}
		}
		if (key < root.min){
			//swap(key, value, root.min);
		}
		if (key > root.max){
			root.setMax(key, value);
		}
		//i = floor (key / Math.sqrt(M));
		//insert(T.children[i])
		//if (T.children[i].min == T.children[i].max){
//			insert(T.aux,i);
	//	}
	}

	@Override
	public void delete(int key) {
		// TODO Auto-generated method stub
		return;
	}
	static public void main(String [] args) throws IOException{
		VanEmdeBoas a = new VanEmdeBoas();
		System.out.println(a.size());
		a.insert(1, 1);
		a.insert(2, 2);
		a.insert(4, 0);
		a.insert(4, 4);
		a.insert(5, 4);
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
