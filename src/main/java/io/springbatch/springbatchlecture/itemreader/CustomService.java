package io.springbatch.springbatchlecture.itemreader;

public class CustomService<T> {

	private int cnt = 0 ;

	public void customWrite(T item) {
		System.out.println(item);
	}
	
	public T customRead() {
		return (T)("item" + cnt++);
	}
	
}
