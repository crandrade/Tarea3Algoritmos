package Tarea3Algoritmos;

import com.javamex.classmexer.MemoryUtil;


public abstract class GenericTree {

	
	public abstract GenericNode find(int key);
	
	public abstract void insert(int key, int value);
	
	public abstract void delete(int key);
	
	public long size(){
		return MemoryUtil.deepMemoryUsageOf(this);
	}
}
