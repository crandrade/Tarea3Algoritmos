package Tarea3Algoritmos;

import java.io.IOException;

public class SplayTree extends GenericTree {
	private Node root, aux;
	
	private class Node extends GenericNode{
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
	
	
	public SplayTree() {
		// TODO Auto-generated constructor stub
		root = null;
		aux = new Node(-1, -1);
	}

	@Override
	public GenericNode find(int key) {
		// TODO Auto-generated method stub
		if (root == null) return null;
		splay(key);
		if(root.key != key){
			return null;
		}
		return root;
	}

	private void splay(int key){
		Node l, r, t, y;
		l = r = aux;
		t = root;
		for (;;) {
		    if (key < t.key) {
				if (t.left == null) break;
				if (key < t.left.key) {
				    y = t.left;                            /* rotate right */
				    t.left = y.right;
				    y.right = t;
				    t = y;
				    if (t.left == null) break;
				}
				r.left = t;                                 /* link right */
				r = t;
				t = t.left;
		    } 
		    else if (key > t.key) {
				if (t.right == null) break;
				if (key > t.right.key) {
				    y = t.right;                            /* rotate left */
				    t.right = y.left;
				    y.left = t;
				    t = y;
				    if (t.right == null) break;
				}
				l.right = t;                                /* link left */
				l = t;
				t = t.right;
		    } 
		    else {
		    	break;
		    }
		}
		l.right = t.left;                                   /* assemble */
		r.left = t.right;
		t.left = aux.right;
		t.right = aux.left;
		root = t;
	}

	@Override
	public void insert(int key, int value) {
		// TODO Auto-generated method stub
		Node n = new Node(key, value);
		if (root == null){
			root = n;
			return;
		}
		splay(key);
		if(root.key == key){ // non-duplicated trees, modify
			root.value = value;
			return;
		}
		else if (root.key > key){
			n.setLeft(root.getLeft());
			n.setRight(root);
			root.setLeft(null);
			root = n;
		}
		else{
			n.setRight(root.getRight());
			n.setLeft(root); 
			root.setRight(null);
			root = n;
		}
	}

	@Override
	public void delete(int key) {
		if(root == null) return;
		splay(key);
		if(root.key == key){
			if(root.getLeft() == null){
				root = root.getRight();
			}
			else{
				Node aux = root.getRight();
				root = root.getLeft();
				splay(key);
				root.setRight(aux);
			}
		}
		return;
	}
	
	
	static public void main(String [] args) throws IOException{
		SplayTree a = new SplayTree();
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
