public interface Frontier<E> extends Container<E> {
	boolean add(E element);
	E get();
	E find(Object object);
	int getLimit();
	void setLimit(int limit);
	int getN();
	void setN(int n);
}
