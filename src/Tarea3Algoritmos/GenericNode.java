package Tarea3Algoritmos;

public abstract class GenericNode {
	protected int key, value;

	public int getKey(){
		return this.key;
	}
	public int getValue(){
		return this.value;
	}
	public void setKey(int i){
		this.key = i;
	}
	public void setValue(int i){
		this.value = i;
	}	
}
