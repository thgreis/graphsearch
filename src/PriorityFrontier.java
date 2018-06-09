import java.io.Serializable;
import java.util.TreeSet;
import java.util.Objects;

public class PriorityFrontier<E> extends AbstractFrontier<E> implements Serializable {
	//constructors
	public PriorityFrontier() {
		super(new TreeSet<>());
	}

	public PriorityFrontier(int limit) {
		super(new TreeSet<>(), limit);
	}

	public PriorityFrontier(int limit, int n) {
		super(new TreeSet<>(), limit, n);
	}

	//public PriorityFrontier(int limit, int initialCapacity) {
	//	super(new PriorityQueue<E>(initialCapacity), limit);
	//}

	//public PriorityFrontier(int limit, int initialCapacity, int n) {
	//	super(new PriorityQueue<E>(initialCapacity), limit, n);
	//}

	//frontier behaviour
	@Override
	public boolean add(E element) {
		Objects.requireNonNull(element, "Invalid null element.");

		//limited size
		if (frontier.size() == limit) {
			//removendo ultimo (remove menos prioritario priorizando mais prioritario)
			//lookup.remove(((PriorityQueue<E>)frontier).);
			lookup.remove(((TreeSet<E>)frontier).pollLast());
		}

		//((PriorityQueue<E>)frontier).offer(element);
		((TreeSet<E>)frontier).add(element);
		lookup.put(element, element);
		return true;
	}

	@Override
	public E get() {
		if (batch.isEmpty()) {
			for (int iterator = 1; iterator <= n && !frontier.isEmpty(); iterator++) {
				//batch.add(((PriorityQueue<E>)frontier).poll());
				batch.add(((TreeSet<E>)frontier).pollFirst());
			}
		}

		E element = batch.remove(0);
		lookup.remove(element);
		return element;
	}

	//object behaviour
	//inherited
}
