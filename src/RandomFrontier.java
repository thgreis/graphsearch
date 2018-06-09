import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

public class RandomFrontier<E> extends AbstractFrontier<E> implements Serializable {
	//immutable state
	private final Random shuffler;

	//constructors
	public RandomFrontier() {
		super(new ArrayList<>());
		this.shuffler = new Random();
	}

	public RandomFrontier(int limit) {
		super(new ArrayList<>(limit), limit);
		this.shuffler = new Random();
	}

	public RandomFrontier(int initialCapacity, int limit) {
		super(new ArrayList<>(initialCapacity), limit);
		this.shuffler = new Random();
	}

	public RandomFrontier(int initialCapacity, int limit, int n) {
		super(new ArrayList<>(initialCapacity), limit, n);
		this.shuffler = new Random();
	}

	public RandomFrontier(Random shuffler, int limit) {
		super(new ArrayList<>(limit), limit);
		this.shuffler = Objects.requireNonNull(shuffler, "Invalid null shuffler.");
	}

	public RandomFrontier(Random shuffler, int initialCapacity, int limit) {
		super(new ArrayList<>(initialCapacity), limit);
		this.shuffler = Objects.requireNonNull(shuffler, "Invalid null shuffler.");
	}

	public RandomFrontier(Random shuffler, int initialCapacity, int limit, int n) {
		super(new ArrayList<>(initialCapacity), limit, n);
		this.shuffler = Objects.requireNonNull(shuffler, "Invalid null shuffler.");
	}

	//frontier behaviour
	@Override
	public boolean add(E element) {
		Objects.requireNonNull(element, "Invalid null element.");

		//limited size
		if (frontier.size() == limit) {
			//removendo aleatorio
			lookup.remove(((ArrayList<E>)frontier).remove(shuffler.nextInt(frontier.size())));
		}

		((ArrayList<E>)frontier).add(element);
		lookup.put(element, element);
		return true;
	}

	@Override
	public E get() {
		if (batch.isEmpty()) {
			for (int iterator = 1; iterator <= n && !frontier.isEmpty(); iterator++) {
				batch.add(((ArrayList<E>)frontier).remove(shuffler.nextInt(frontier.size())));
			}
		}

		E element = batch.remove(0);
		lookup.remove(element);
		return element;
	}

	//object behaviour
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}

		if (!(object instanceof Frontier)) {
			return false;
		}

		//comparing elements just by equality because random frontier has no defined order
		if (object instanceof RandomFrontier) {
			RandomFrontier another = (RandomFrontier)object;

			if (frontier.size() != another.size() || limit != another.getLimit() || n != another.getN()) {
				return false;
			} else {
				for (Iterator<E> iterator = frontier.iterator(); iterator.hasNext();) {
					if (!another.contains(iterator.next())) {
						return false;
					}
				}

				return true;
			}
		} else {
			return super.equals(object);
		}
	}
}
