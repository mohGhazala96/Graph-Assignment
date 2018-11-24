import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

	private HashMap<String, Vertex> vertices;

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
}
