package Tarea3Algoritmos;


public interface GenericTree {

	public long size();
	
	@SuppressWarnings("rawtypes")
	public Comparable get(Comparable key);
	
	@SuppressWarnings("rawtypes")
	public void insert(Comparable key);
	
	@SuppressWarnings("rawtypes")
	public boolean delete(Comparable key);
}
