package Tarea1Algoritmos;

public abstract class GenericTextSearch {
	public char [] innerText;
	
	public int search(char [] patron){
		return 0;
	}
	
	public GenericTextSearch(char [] text){
		this.innerText = text;
	}
	
	public void delete(){
		this.innerText = null;
	}
}
