package Tarea3Algoritmos;

public interface GenericTree {
	public interface Node{
		
	}
	public int get(Comparable<?> key);
	
	public Object insert(Comparable<?> key);
	
	public void delete(Comparable<?> key);
}
