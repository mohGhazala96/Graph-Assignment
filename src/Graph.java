import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

public class Graph {

	private HashMap<String,Vertex> vertices;
	private HashMap<String,Edge> edges;

	public Graph() {
		super();
		
		this.vertices = new HashMap<String, Vertex>();
	}
	
	
	public void insertVertex(String strUniqueID,
			String strData,
			int
			nX,
			int
			nY) throws GraphException {
		if (vertices.containsKey(strUniqueID)) {
			throw new GraphException("ID already exists .. and it's called unique you idiot");
		}

		Vertex vertex = new Vertex(new StringBuffer(strUniqueID), new StringBuffer(strData), nX, nY);
		
		vertices.put(strUniqueID, vertex);
	}

	// inserts an edge between 2 specified vertices
	public void insertEdge(String strVertex1UniqueID, String strVertex2UniqueID, String strEdgeUniqueID,
			String strEdgeData, int nEdgeCost) throws GraphException {
		
		if(edges.containsKey(strEdgeUniqueID)) {
			throw new GraphException("Edge Already Exist");
		}
		Vertex vertexA = this.vertices.get(strVertex1UniqueID);
		Vertex vertexB = this.vertices.get(strVertex2UniqueID);
		Edge edge= new Edge(new StringBuffer(strEdgeUniqueID) , new StringBuffer(strEdgeData),nEdgeCost, vertexA,vertexB);
		edges.put(strEdgeUniqueID, edge);
		
	}
	
	public Vector<Edge> incidentEdges(String strVertexUniqueID)
			throws GraphException {
		Vertex vertex = vertices.get(strVertexUniqueID);
		
		if (vertex == null) {
			throw new GraphException("Vertex doesn't exist dude...");
		}
		
		return new Vector<Edge>(vertex._edges);
	}
}
