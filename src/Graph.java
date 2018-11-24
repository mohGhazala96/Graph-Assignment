import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

	private HashMap<String,Vertex> vertices;
	private HashMap<String,Edge> edges;

	public Graph() {
		super();
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
}
