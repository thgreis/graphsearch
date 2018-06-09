import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

public class QueueFrontier<E> extends AbstractFrontier<E> implements Serializable {
	//constructors
	public QueueFrontier() {
		super(new LinkedList<>());
	}

	public QueueFrontier(int limit) {
		super(new LinkedList<>(), limit);
	}

	public QueueFrontier(int limit, int n) {
		super(new LinkedList<>(), limit, n);
	}

	//frontier behaviour
	@Override
	public boolean add(E element) {
		Objects.requireNonNull(element, "Invalid null element.");

		//limited size
		if (frontier.size() == limit) {
			//removendo ultimo (remove mais profundo priorizando mais razo)
			lookup.remove(((LinkedList<E>)frontier).removeLast());
		}

		((LinkedList<E>)frontier).addLast(element);
		lookup.put(element, element);
		return true;
	}

	@Override
	public E get() {
		if (batch.isEmpty()) {
			for (int iterator = 1; iterator <= n && !frontier.isEmpty(); iterator++) {
				batch.add(((LinkedList<E>)frontier).removeFirst());
			}
		}

		E element = batch.remove(0);
		lookup.remove(element);
		return element;
	}

	//object behaviour
	//inherited
}
