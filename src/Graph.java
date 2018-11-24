import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

	private HashMap<String,Vertex> vertices;

	public Graph() {
		super();
	}

	// inserts an edge between 2 specified vertices
	public void insertEdge(String strVertex1UniqueID, String strVertex2UniqueID, String strEdgeUniqueID,
			String strEdgeData, int nEdgeCost) throws GraphException {
		Vertex vertexA = this.vertices.get(strVertex1UniqueID);
		Vertex vertexB = this.vertices.get(strVertex2UniqueID);
		Edge edge= new Edge(new StringBuffer(strEdgeUniqueID) , new StringBuffer(strEdgeData),nEdgeCost, vertexA,vertexB);

	}
}
