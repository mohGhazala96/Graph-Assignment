import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class Graph {

	private HashMap<String, Vertex> vertices;
	private HashMap<String, Edge> edges;
	private boolean[] IsVisited;
	private ArrayList<Edge> visitedEdges; //stores all visited edges per search in the array list
	private ArrayList<Vertex> visitedVertices; //stores all visited vertices per search in the array list
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
	public void visit(Vertex v) { // store visited vertex
		visitedVertices.add(v);

	}
	public void visit(Edge e) { // store visited edge
		visitedEdges.add(e);
	}

	public void bfs(String strStartVertexUniqueID, Visitor visitor) throws GraphException

	{

		//empty the stored edges and vertices from previous searches
		visitedEdges = new ArrayList<Edge>();
		visitedVertices = new ArrayList<Vertex>();
		
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
				
				if (!visitedEdges.contains(currentEdge)) {
					this.visit(currentEdge);
					visitor.visit(currentEdge);
				} 
				
				Vertex nextVertex =currentEdge.get_vertices()[1];
				if (!visitedVertices.contains(nextVertex)) {
						visitor.visit(nextVertex);
						this.visit(nextVertex);
						queue.add(nextVertex);
					}
			}
		}

	}
	
	public void dfs(String strStartVertexUniqueID, Visitor visitor) throws GraphException{
		
		visitedEdges = new ArrayList<Edge>();
		visitedVertices = new ArrayList<Vertex>();
		
		Stack stack = new Stack();
		Vertex originalVertex = vertices.get(strStartVertexUniqueID);

		this.visit(originalVertex);
		visitor.visit(originalVertex);
		stack.push(originalVertex);
		
		while(!stack.isEmpty()){
			
			boolean noMore = false;
			
			while(!noMore){
				
				if(!((Vertex)stack.peek())._edges.isEmpty()){
					LinkedList<Edge> edgesOfVertex = ((Vertex)stack.peek())._edges;	
					Edge currentEdge = edgesOfVertex.getFirst();
				
					for(int i = 0;i<edgesOfVertex.size();i++){
						if (!visitedEdges.contains(currentEdge)){
							this.visit(currentEdge);
							visitor.visit(currentEdge);
							break;
						}
						
						else{
							currentEdge = edgesOfVertex.get(i);
						}
					}
				
					Vertex nextVertex =currentEdge.get_vertices()[1];
					if (!visitedVertices.contains(nextVertex)) {
							this.visit(nextVertex);
							visitor.visit(nextVertex);
							stack.push(nextVertex);
					}
					else{
						noMore = true;
						stack.pop();
					}
				}
				else{
					noMore = true;
					stack.pop();
				}
			}
		}
	}
	public Vector<PathSegment> pathDFS(String strStartVertexUniqueID,String strEndVertexUniqueID) throws GraphException {
		Vector<PathSegment> path = new Vector<PathSegment>();
		visitedEdges = new ArrayList<Edge>();
		visitedVertices = new ArrayList<Vertex>();
		Vertex originalVertex = vertices.get(strStartVertexUniqueID);
		Vertex endVertex = vertices.get(strEndVertexUniqueID);
		PathSegment p = new PathSegment();
		p._vertex = originalVertex;
		path.addElement(p);
		return  pathDfsHelper(originalVertex,endVertex,path);
	}
	public Vector<PathSegment> pathDfsHelper(Vertex start,Vertex end,Vector<PathSegment> path) {
		this.visit(start);
		if(start.equals(end)) {
			
			return path;
		}
		if(!(start._edges.isEmpty())) {
			LinkedList<Edge> edgesOfVertex = start._edges;	
			Edge currentEdge = edgesOfVertex.getFirst();
			for(int i = 0;i<edgesOfVertex.size();i++){
				if (!visitedEdges.contains(currentEdge)){
					this.visit(currentEdge);
					PathSegment x = new PathSegment();
					x._edge = currentEdge;
					path.addElement(x);
					pathDfsHelper(currentEdge.get_vertices()[1], end,path);
				}
				
				else{
					currentEdge = edgesOfVertex.get(i);
					this.visit(currentEdge);
					pathDfsHelper(currentEdge.get_vertices()[1], end,path);
				}
			}
		}
		return path;
	}
}
