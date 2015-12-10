package Tarea3Algoritmos;

public class ABB implements GenericTree {
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
	
	public ABB() {
		// TODO Auto-generated constructor stub
		root = null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable get(Comparable key) {
		// TODO Auto-generated method stub
		Node aux = root;
		while(aux != null){
			int i = aux.key.compareTo(key);
			if(i == 0){
				return aux.key;
			}
			else if(i < 0)
				aux = aux.getLeft();
			else
				aux = aux.getRight();
		}
		return aux != null? aux.key : null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void insert(Comparable key){
		// TODO Auto-generated method stub
		if(root==null)
			root = new Node(key, null, null, null);
		else{
			for(Node aux=root; aux!=null;){
				if(key.compareTo(aux.key)<=0){
					if(aux.getLeft()!=null){
						aux = aux.getLeft();
						continue;
					}
					else{
						aux.setLeft(new Node(key, null, null, aux));
						return;
					}
				}
				else{
					if(aux.getRight()!=null){
						aux = aux.getLeft();
						continue;
					}
					else{
						aux.setRight(new Node(key, null, null, aux));
						return;
					}
				}
			}
		}
		return;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean delete(Comparable key) {
		// TODO Auto-generated method stub
		return false;
	}

	public long size(){
		return sizeR(root);
	}
	private long sizeR(Node n){
		if(n==null){
			return 0;
		}
		return 1+sizeR(n.getLeft())+sizeR(n.getRight());
	}
	
}
