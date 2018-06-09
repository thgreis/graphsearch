import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;

public abstract class AbstractFrontier<E> implements Frontier<E> {
	//immutable state
	protected final Collection<E> frontier;
	protected final Map<E, E> lookup;
	protected final List<E> batch;

	//mutable state
	protected int limit;
	protected int n;

	//constructors
	protected AbstractFrontier(Collection<E> frontier) {
		this(frontier, Integer.MAX_VALUE, 1);
	}

	protected AbstractFrontier(Collection<E> frontier, int limit) {
		this(frontier, limit, 1);
	}

	protected AbstractFrontier(Collection<E> frontier, int limit, int n) {
		if (limit < 1 || n < 1) {
			throw new IllegalArgumentException("Invalid argument(s) value(s).");
		}

		this.frontier = Objects.requireNonNull(frontier, "Invalid null frontier.");
		this.batch = new ArrayList<>(n);
		this.limit = limit;
		this.n = n;

		if (limit == Integer.MAX_VALUE) {
			this.lookup = new HashMap<>((int)(10_000 / .75f) + 1);
		} else {
			this.lookup = new HashMap<>((int)(limit / .75f) + 1);
		}
	}

	//frontier behaviour
	@Override
	public abstract boolean add(E element);

	@Override
	public abstract E get();

	@Override
	public E find(Object object) {
		return lookup.get(Objects.requireNonNull(object, "Invalid null object."));
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setLimit(int limit) {
		if (limit < frontier.size()) {
			throw new IllegalArgumentException("Invalid limit value, lower than frontier current size.");
		}

		this.limit = limit;
	}

	@Override
	public int getN() {
		return n;
	}

	@Override
	public void setN(int n) {
		if (n < batch.size()) {
			throw new IllegalArgumentException("Invalid N value, lower than batch current size.");
		}

		this.n = n;
	}

	//container behaviour
	@Override
	public int size() {
		return frontier.size() + batch.size();
	}

	@Override
	public boolean isEmpty() {
		return frontier.isEmpty() && batch.isEmpty();
	}

	@Override
	public boolean contains(Object object) {
		return lookup.containsKey(Objects.requireNonNull(object, "Invalid null object."));
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return lookup.keySet().containsAll(Objects.requireNonNull(collection, "Invalid null collection."));
	}

	@Override
	public Object[] toArray() {
		return stream().toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return stream().toArray(dummy -> array);
	}

	@Override
	public Spliterator<E> spliterator() {
		return Spliterators.spliterator(batch, limit);
	}

	@Override
	public Stream<E> stream() {
		return Stream.concat(batch.stream(), frontier.stream());
	}

	@Override
	public Stream<E> parallelStream() {
		return Stream.concat(batch.parallelStream(), frontier.parallelStream());
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private final Iterator<E> batchIterator = batch.iterator();
			private final Iterator<E> frontierIterator = frontier.iterator();

			@Override
			public boolean hasNext() {
				return batchIterator.hasNext() || frontierIterator.hasNext();
			}

			@Override
			public E next() {
				return (batchIterator.hasNext() ? batchIterator.next() : frontierIterator.next());
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Invalid unsupported operation.");
			}
		};
	}

	//object behaviour
	@Override
	public int hashCode() {
		return Objects.hash(frontier, limit, n);
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}

		if (!(object instanceof Frontier)) {
			return false;
		}

		if (object instanceof AbstractFrontier) {
			AbstractFrontier another = (AbstractFrontier)object;
			return limit == another.limit && n == another.n && frontier.equals(another.frontier);
		} else {
			Frontier another = (Frontier)object;

			if (frontier.size() != another.size() || limit != another.getLimit() || n != another.getN()) {
				return false;
			} else {
				Iterator<?> iterator1 = frontier.iterator();
				Iterator<?> iterator2 = another.iterator();

				//comparing by elements order and equality
				while (iterator1.hasNext() && iterator2.hasNext()) {
					if (!Objects.equals(iterator1.next(), iterator2.next())) {
						return false;
					}
				}

				return true;
			}
		}
	}

	@Override
	public String toString() {
		return "Limit=" + limit + ", N=" + n + ", " + frontier.toString();
	}
}
