package graph;

import java.util.Vector;

/**
 * @author Lückerath
 * @version 27.07.2011
 */
public class Node {
	public static final int	NODE_TYPE_PLATFORM		= 1;
	public static final int	NODE_TYPE_TRACK_SPLIT	= 3;

	private int				type					= -1;
	private int				id						= -1;

	private Vector<Edge>	edges					= null;

	public Node(final int id, final int type) {
		this.id = id;
		this.type = type;
		edges = new Vector<Edge>();
	}

	public void addEdge(final Edge edge) {
		if (!edges.contains(edge))
			if ((type == NODE_TYPE_PLATFORM && edges.size() < 2)
					|| (type == NODE_TYPE_TRACK_SPLIT && edges.size() < 3))
				edges.add(edge);
	}

	@Override
	public boolean equals(final Object n) {
		final Node no = (Node) n;
		return id == no.getId() && type == no.getType();
	}

	public int getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public Vector<Edge> getEdges() {
		return edges;
	}

	public boolean hasDirectOutgoingNeighbor(final Node destination) {
		if (edges.isEmpty()) return false;
		for (final Edge e : edges)
			if (e.getDestination().equals(destination)) return true;
		return false;
	}
}
