package graph;

import java.util.Vector;

/**
 * @author Lückerath
 * @version 28.07.2011
 */
public class Graph {
	private Vector<Node>	nodes		= null;
	private Vector<Edge>	edges		= null;

	private int				maxEdgeId	= 0;

	public Graph() {
		nodes = new Vector<Node>();
		edges = new Vector<Edge>();
	}

	private Graph getSubgraph(final Node source, final Node destination) {
		//p(source.getType()+","+source.getId()+";"+destination.getType()+","+destination.getId());
		Graph graph = null;
		if (source.hasDirectOutgoingNeighbor(destination)) {
			graph = new Graph();
			graph.addNode(source);
			graph.addNode(destination);
			graph.addEdge(getTrack(source, destination));
		} else {
			int i = 0;
			final Vector<Edge> sourceEdges = source.getEdges();
			while (graph == null && i < sourceEdges.size())
				if (sourceEdges.elementAt(i).getDestination().equals(source))
					i++;
				else {
					//if (sourceEdges.elementAt(i).getDestination().getType() == Node.NODE_TYPE_TRACK_SPLIT)
						graph = getSubgraph(sourceEdges.elementAt(i)
								.getDestination(), destination);
					if (graph == null) i++;
				}
			if (graph != null) {
				graph.addNode(source);
				graph.addEdge(sourceEdges.elementAt(i));
			}
		}
		//if(graph==null) p("?");
		return graph;
	}

	public Graph getSubgraph(final int sourceId, final int destId) {
		final Node source = getNode(sourceId, Node.NODE_TYPE_PLATFORM);
		final Node dest = getNode(destId, Node.NODE_TYPE_PLATFORM);

		if (source != null && dest != null) return getSubgraph(source, dest);
		return null;
	}

	public int[] getConvertedData(final int sourceNodeId,
			final int sourceNodeType, final int destinationNodeId,
			final int destinationNodeType, final int offset) {

		// System.out.println("Graph.convertData() > SourceNodeId: "
		// + sourceNodeId + ", sourceNodeType: " + sourceNodeType
		// + ", destNodeId: " + destinationNodeId + ", destNodeType: "
		// + destinationNodeType + ", offset: " + offset);

		int[] convertedData = { sourceNodeId, sourceNodeType,
				destinationNodeId, destinationNodeType, offset };

		final Node sourceNode = getNode(sourceNodeId, sourceNodeType);
		if (sourceNode != null) {
			final Vector<Edge> edges = sourceNode.getEdges();
			int i = 0;
			while (i < edges.size()
					&& (edges.elementAt(i).getDestination().equals(sourceNode) || getTrack(
							sourceNode, edges.elementAt(i).getDestination()) == null))
				i++;
			if (i < edges.size()) {
				boolean found = false;
				while (i < edges.size() && !found)
					if (offset > edges.elementAt(i).getTrackLength()
							&& edges.elementAt(i).getDestination().getType() != Node.NODE_TYPE_PLATFORM) {
						convertedData = getConvertedData(edges.elementAt(i)
								.getDestination().getId(), edges.elementAt(i)
								.getDestination().getType(), destinationNodeId,
								destinationNodeType, offset
										- edges.elementAt(i).getTrackLength());
						if (convertedData == null)
							i++;
						else found = true;
					} else {
						if (convertedData == null) {
							convertedData = new int[5];
							convertedData[0] = edges.elementAt(i).getSource()
									.getId();
							convertedData[1] = edges.elementAt(i).getSource()
									.getType();
							convertedData[4] = offset;
						}
						convertedData[2] = edges.elementAt(i).getDestination()
								.getId();
						convertedData[3] = edges.elementAt(i).getDestination()
								.getType();
						found = true;
					}
				if (!found)
					throw new NullPointerException("Keine weiteren Kanten!");
			} else throw new NullPointerException("Keine weiteren Kanten!");
		} else return null;
		return convertedData;
	}

	public Node getNode(final int id, final int type) {
		if (nodes != null) for (final Node n : nodes)
			if (n.getId() == id && n.getType() == type) return n;
		return null;
	}

	public Edge getTrack(final Node source, final Node destination) {
		if (source == null || destination == null) return null;
		final Vector<Edge> sourceNodeEdges = source.getEdges();
		if (sourceNodeEdges != null) for (final Edge e : sourceNodeEdges)
			if (e.getDestination().equals(destination)) return e;
		return null;
	}

	public int getNextEdgeId() {
		return ++maxEdgeId;
	}

	public void addEdge(final Edge edge) {
		if (!edges.contains(edge)) edges.add(edge);
	}

	public void addNode(final Node node) {
		if (!nodes.contains(node)) nodes.add(node);
	}


	public int giveTrackLength(){
		int l = 0;
		for(Edge e : edges){
			l += e.getTrackLength();
		}
		return l;
	}

	public static void p(Object s){
		System.out.println(s);
	}
}
