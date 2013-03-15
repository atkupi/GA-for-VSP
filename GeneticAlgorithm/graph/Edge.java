package graph;
/**
 * @author Lückerath
 * @version 27.07.2011
 */
public class Edge {
	private int		id			= -1;

	private Node	source		= null;
	private Node	destination	= null;

	private int		trackLength	= -1;

	public Edge(final int id, final Node source, final Node destination,
			final int trackLength) {
		this.id = id;
		this.source = source;
		this.destination = destination;

		this.trackLength = trackLength;

		source.addEdge(this);
		destination.addEdge(this);
	}

	public Node getSource() {
		return source;
	}

	public Node getDestination() {
		return destination;
	}

	public int getId() {
		return id;
	}

	@Override
	public boolean equals(final Object e) {
		final Edge eg = (Edge) e;
		return id == eg.getId();
	}

	public int getTrackLength() {
		return trackLength;
	}
}
