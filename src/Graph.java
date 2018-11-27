import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class Graph {

	private HashMap<String, Vertex> vertices;
	private HashMap<String, Edge> edges;
	
	private ArrayList<Edge> visitedEdges; //stores all visited edges per search in the array list
	private ArrayList<Vertex> visitedVertices; //stores all visited vertices per search in the array list

	public Graph() {
		super();

		this.vertices = new HashMap<String, Vertex>();
		this.edges= new HashMap<String, Edge> ();
	}
	public String getLibraryName( ){
		return "A Great Graph Library";
	}

	public String getLibraryVersion( ){
		return "0.1";
	 }

	public void insertVertex(String strUniqueID, String strData, int nX, int nY) throws GraphException {
		if (vertices.containsKey(strUniqueID)) {
			throw new GraphException("ID already exists");
		}

		Vertex vertex = new Vertex(new StringBuffer(strUniqueID), new StringBuffer(strData), nX, nY);
		vertices.put(strUniqueID, vertex);
		System.out.println("Inserted vertex "+strUniqueID+" successfully");
	}

	// inserts an edge between 2 specified vertices
	public void insertEdge(String strVertex1UniqueID, String strVertex2UniqueID, String strEdgeUniqueID,
			String strEdgeData, int nEdgeCost) throws GraphException {
		if (edges.containsKey(strEdgeUniqueID)) {
			throw new GraphException("Edge Already Exist");
		}else if(!vertices.containsKey(strVertex1UniqueID)  ) {
			throw new GraphException("Vertix "+strVertex1UniqueID +" doesn't exist");			
		}else if(!vertices.containsKey(strVertex2UniqueID) ) {
			throw new GraphException("Vertix "+strVertex2UniqueID +" doesn't exist");			
		} 
		Vertex vertexA = this.vertices.get(strVertex1UniqueID);
		Vertex vertexB = this.vertices.get(strVertex2UniqueID);
		Edge edge = new Edge(new StringBuffer(strEdgeUniqueID), new StringBuffer(strEdgeData), nEdgeCost, vertexA,
				vertexB);
		edges.put(strEdgeUniqueID, edge);
		System.out.println("Inserted Edge "+strEdgeUniqueID+" successfully");
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

	// removes an edge from the graph
	public void removeEdge(String strEdgeUniqueID) throws GraphException {
		if (!edges.containsKey(strEdgeUniqueID)) {
			throw new GraphException("Edge Not Found");
		}
		edges.get(strEdgeUniqueID).removeEdge();
		edges.remove(strEdgeUniqueID);
		System.out.println("Removed Edge "+strEdgeUniqueID+" successfully");

	}

	public Vector<Edge> incidentEdges(String strVertexUniqueID) throws GraphException {
		Vertex vertex = vertices.get(strVertexUniqueID);

		if (vertex == null) {
			throw new GraphException("Vertex doesn't exist.");
		}
		return new Vector<Edge>(vertex._edges);
	}

	public Vector<Vertex> vertices() throws GraphException {
		Collection<Vertex> verSet = this.vertices.values();

		return new Vector<Vertex>(verSet);
	}

	public Vector<Edge> edges() throws GraphException {
		Collection<Edge> edgSet = this.edges.values();

		return new Vector<Edge>(edgSet);
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
	

	
	private ArrayList<Vertex[]> divideVerticesByMedian(Vertex[] curr, boolean horizontal, int median) {
		ArrayList<Vertex[]> out = new ArrayList<Vertex[]>();
		ArrayList<Vertex> first = new ArrayList<Vertex>();
		ArrayList<Vertex> second = new ArrayList<Vertex>();
		
		for (Vertex v: curr) {
			if (horizontal) {
				if(v.getY() >= median) {
					second.add(v);
				} else {
					first.add(v);
				}
			} else {
				if(v.getX() >= median) {
					second.add(v);
				} else {
					first.add(v);
				}
			}
		}
		
		out.add((Vertex[])first.toArray());
		out.add((Vertex[])second.toArray());	
		return out;
	}
	
	private Vertex[] getMidVertices(Vertex[] curr, boolean horizontal, int median, double range) {
		ArrayList<Vertex> midVer = new ArrayList<Vertex>();
		
		for (Vertex v : curr) {
			if (horizontal) {
				if(v.getY() >= median - range && v.getY() <= median + range) {
					midVer.add(v);
				}
			} else {
				if(v.getX() >= median - range && v.getX() <= median + range) {
					midVer.add(v);
				}
			}
		}
		
		return (Vertex[])midVer.toArray();
	}
	
	private int getMedian(Vertex[] curr, boolean horizontal) {

		ArrayList<Integer> points = new ArrayList<Integer>();
		
		if (horizontal) {
			for (Vertex v: curr) {
				points.add(v.getY());
			}
		} else {
			for (Vertex v: curr) {
				points.add(v.getX());
			}
		}
		Collections.sort(points);
		return points.get(curr.length/2);
	}
	
	
	private Vertex[] closestPairHelper(Vertex[] curr, boolean horizontal) throws GraphException {
		// @param horizontal : specifies if the next division will be by X or Y
		int n = curr.length;
		if(n <= 2) {
			return curr;
		} 
		
		int median = getMedian(curr, horizontal);
		ArrayList<Vertex[]> dividedLists = divideVerticesByMedian(curr, horizontal, median);
		
		Vertex[] leftOut = closestPairHelper(dividedLists.get(0), !horizontal);
		Vertex[] rightOut = closestPairHelper(dividedLists.get(1), !horizontal);

		double leftDist = Vertex.getDistance(leftOut);
		double rightDist = Vertex.getDistance(rightOut);
		
		double min = Math.min(rightDist, leftDist);
		
		Vertex[] midVertex = getMidVertices(curr, horizontal, median, min);
		Vertex[] midOut = closestPairHelper(midVertex, !horizontal);
		double midDist = Vertex.getDistance(midOut);
		
		
		min = Math.min(midDist, min);
		
		if (min == leftDist) {
			return leftOut;
		} else if(min == rightDist) {
			return rightOut;
		} else {
			return midOut;
		}
	}
	
	// finds the closest pair of vertices using divide and conquer
	// algorithm. Use X and Y attributes in each vertex.
	public Vertex[] closestPair() throws GraphException {
		Collection<Vertex> verts = this.vertices.values();
		return closestPairHelper((Vertex[])verts.toArray(), true);
	}
	
	//for testing
	public void printPresentEdgesAndVertices() {
		System.out.println(this.vertices.toString());
		System.out.println(this.edges.toString());

	}
	public static void main(String args[]) throws GraphException {
		Graph g = new Graph( );
		TestVisitor gVisitor = new TestVisitor();
		g.insertVertex("1", "1",0,0 );
		g.insertVertex("2", "2",0,0);
		g.insertVertex("3", "3",0,0);
		g.insertVertex("4", "4",0,0);
		g.insertVertex("5", "5",0,0);
		g.insertEdge("1","4","88","88",5);
		g.insertEdge("1","2","2","2", 2);
		g.insertEdge("2", "3","14","14",14);
		g.insertEdge("2", "4","99","99",5);
		g.insertEdge("2", "5","4","4",4);
		g.insertEdge("4", "5", "58", "58", 58);
		g.insertEdge("3", "5", "34", "34", 34);
		
		// returns the vertices and edges you have visisted
//		System.out.println(gVisitor._strResult);

		
		
		//TESTED BAS MOMKN TOBSO FEH TANI PLEASE <3
//		Vector<Edge> incidentEdges = g.incidentEdges("1");
//	    Enumeration vectorElementsEnum = incidentEdges.elements();
//	    while(vectorElementsEnum.hasMoreElements())
//	    		System.out.println(vectorElementsEnum.nextElement());
//		
//	    Vector<Vertex> outputVertices= g.vertices();
//	     vectorElementsEnum = outputVertices.elements();
//	    while(vectorElementsEnum.hasMoreElements())
//	    		System.out.println(vectorElementsEnum.nextElement());
//
//		Vector<Edge> outputEdges = g.edges();
//	    vectorElementsEnum = outputEdges.elements();
//	    while(vectorElementsEnum.hasMoreElements())
//	    		System.out.println(vectorElementsEnum.nextElement());
//		
//	    Vertex[] endVerticesOut= g.endVertices("88");
//		System.out.println(endVerticesOut[0]);
//		System.out.println(endVerticesOut[1]);
//		
//	    Vertex oppositVertices= g.opposite("4", "88");
//		System.out.println(oppositVertices);
		
//		g.removeVertex("1");
//		g.removeVertex("2");
//		g.removeVertex("3");
//		g.removeVertex("4");
//		g.removeVertex("5");

//		g.removeEdge("88");
//		g.removeEdge("2");
//		g.removeEdge("14");
//		g.removeEdge("99");
//		g.removeEdge("4");
//		g.removeEdge("58");
//		g.removeEdge("34");

//		g.printPresentEdgesAndVertices();;
		
	}
}
