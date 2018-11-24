import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Graph {
	private HashMap<String ,Vertex> vertices;
	private HashMap<String ,Edge> edges;

	public Graph() {
		super();
	}
	
	public void removeVertex(String strVertexUniqueID) throws GraphException{
		for(int i=0;i<vertices.get(strVertexUniqueID)._edges.size();i++){ 
			boolean found = false;
			String idOfEdge = vertices.get(strVertexUniqueID)._edges.get(i)._strUniqueID.toString();
			Vertex v = vertices.get(strVertexUniqueID)._edges.get(i).getConnectedVertex(vertices.get(strVertexUniqueID));
			for(int j =0;j<v._edges.size() && !found ;i++){
				if(v._edges.get(j).getConnectedVertex(v)._strUniqueID.toString().equals(vertices.get(strVertexUniqueID)._strUniqueID.toString())){
					found = true;
					v._edges.remove(v._edges.get(j));
				}
			}
			found = false;
			edges.remove(idOfEdge);
		}
		vertices.remove(strVertexUniqueID);
	}
}
