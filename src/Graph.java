import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Set;
import java.util.Vector;

public class Graph implements Visitor {

	private HashMap<String, Vertex> vertices;
	private HashMap<String, Edge> edges;
	HashMap<String, Boolean> visitedEdges;
	HashMap<String, Boolean> visitedVertices;

	public Graph() {
		super();

		this.vertices = new HashMap<String, Vertex>();
	}

	public void insertVertex(String strUniqueID, String strData, int nX, int nY) throws GraphException {
		if (vertices.containsKey(strUniqueID)) {
			throw new GraphException("ID already exists");
		}

		Vertex vertex = new Vertex(new StringBuffer(strUniqueID), new StringBuffer(strData), nX, nY);

		vertices.put(strUniqueID, vertex);
	}

	// inserts an edge between 2 specified vertices
	public void insertEdge(String strVertex1UniqueID, String strVertex2UniqueID, String strEdgeUniqueID,
			String strEdgeData, int nEdgeCost) throws GraphException {

		if (edges.containsKey(strEdgeUniqueID)) {
			throw new GraphException("Edge Already Exist");
		}
		Vertex vertexA = this.vertices.get(strVertex1UniqueID);
		Vertex vertexB = this.vertices.get(strVertex2UniqueID);
		Edge edge = new Edge(new StringBuffer(strEdgeUniqueID), new StringBuffer(strEdgeData), nEdgeCost, vertexA,
				vertexB);
		edges.put(strEdgeUniqueID, edge);

	}

	// removes an edge from the graph
	public void removeEdge(String strEdgeUniqueID) throws GraphException {
		if (!edges.containsKey(strEdgeUniqueID)) {
			throw new GraphException("Edge Not Found");
		}
		edges.get(strEdgeUniqueID).removeEdge();
		edges.remove(strEdgeUniqueID);
	}

	public Vector<Edge> incidentEdges(String strVertexUniqueID) throws GraphException {
		Vertex vertex = vertices.get(strVertexUniqueID);

		if (vertex == null) {
			throw new GraphException("Vertex doesn't exist.");
		}

		return new Vector<Edge>(vertex._edges);
	}

	public Vector<Vertex> vertices() throws GraphException {
		Set verSet = this.vertices.entrySet();

		return new Vector<Vertex>(verSet);
	}

	public Vector<Edge> edges() throws GraphException {
		Set edgSet = this.edges.entrySet();

		return new Vector<Edge>(edgSet);
	}

	public void removeVertex(String strVertexUniqueID) throws GraphException {
		for (int i = 0; i < vertices.get(strVertexUniqueID)._edges.size(); i++) {
			boolean found = false;
			String idOfEdge = vertices.get(strVertexUniqueID)._edges.get(i)._strUniqueID.toString();
			Vertex v = vertices.get(strVertexUniqueID)._edges.get(i)
					.getConnectedVertex(vertices.get(strVertexUniqueID));
			for (int j = 0; j < v._edges.size() && !found; i++) {
				if (v._edges.get(j).getConnectedVertex(v)._strUniqueID.toString()
						.equals(vertices.get(strVertexUniqueID)._strUniqueID.toString())) {
					found = true;
					v._edges.remove(v._edges.get(j));
				}
			}
			found = false;
			edges.remove(idOfEdge);
		}
		vertices.remove(strVertexUniqueID);
	}

	public Vertex[] endVertices(String strEdgeUniqueID) throws GraphException {
		return edges.get(strEdgeUniqueID).get_vertices();
	}

	public Vertex opposite(String strVertexUniqueID, String strEdgeUniqueID) throws GraphException {
		Edge current = edges.get(strEdgeUniqueID);
		return current.getConnectedVertex(vertices.get(strVertexUniqueID));

	}
	public  void visit(Vertex v) {
		visitedVertices.put(v._strUniqueID.toString(), true);

	}
	public  void visit(Edge e) {
		visitedEdges.put(e._strUniqueID.toString(), true);

	}

	public void bfs(String strStartVertexUniqueID, Visitor visitor) throws GraphException

	{

		visitedEdges = new HashMap<String, Boolean>();
		visitedVertices = new HashMap<String, Boolean>();
		
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		Vertex originalVertex = vertices.get(strStartVertexUniqueID);

		this.visit(originalVertex);
		queue.add(originalVertex);

		while (queue.size() != 0) {
			Vertex outVertex = queue.poll();
			System.out.print(outVertex._strUniqueID + " -> ");
			Iterator<Edge> edgesOfVertex = outVertex._edges.listIterator();
			
			while (edgesOfVertex.hasNext()) {
				Edge currentEdge = edgesOfVertex.next();
				this.visit(currentEdge);
				Vertex nextVertex =currentEdge.get_vertices()[1];
				if (!visitedVertices.get(nextVertex._strUniqueID.toString())) {
					this.visit(nextVertex);
					queue.add(nextVertex);
				}
			}
		}

	}
}
