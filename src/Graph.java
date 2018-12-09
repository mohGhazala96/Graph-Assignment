import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class Graph {

	private HashMap<String, Vertex> vertices;
	private HashMap<String, Edge> edges;
	private PathSegment p;
	private ArrayList<Vertex> localPath;
	private ArrayList<Vertex> finalPath;
	private Vector<PathSegment> path;
	private ArrayList<Edge> visitedEdges; // stores all visited edges per search in the array list
	private ArrayList<Vertex> visitedVertices; // stores all visited vertices per search in the array list
	private ArrayList<Edge> testEdge;

	public Graph() {
		super();
		this.localPath = new ArrayList<Vertex>();
		this.finalPath = new ArrayList<Vertex>();
		this.testEdge = new ArrayList<Edge>();

		this.vertices = new HashMap<String, Vertex>();
		this.edges = new HashMap<String, Edge>();
	}

	public String getLibraryName() {
		return "A Great Graph Library";
	}

	public String getLibraryVersion() {
		return "0.1";
	}

	public void insertVertex(String strUniqueID, String strData, int nX, int nY) throws GraphException {
		if (vertices.containsKey(strUniqueID)) {
			throw new GraphException("ID already exists");
		}

		Vertex vertex = new Vertex(new StringBuffer(strUniqueID), new StringBuffer(strData), nX, nY);
		vertices.put(strUniqueID, vertex);
		System.out.println("Inserted vertex " + strUniqueID + " successfully");
	}

	// inserts an edge between 2 specified vertices
	public void insertEdge(String strVertex1UniqueID, String strVertex2UniqueID, String strEdgeUniqueID,
			String strEdgeData, int nEdgeCost) throws GraphException {
		if (edges.containsKey(strEdgeUniqueID)) {
			throw new GraphException("Edge Already Exist");
		} else if (!vertices.containsKey(strVertex1UniqueID)) {
			throw new GraphException("Vertix " + strVertex1UniqueID + " doesn't exist");
		} else if (!vertices.containsKey(strVertex2UniqueID)) {
			throw new GraphException("Vertix " + strVertex2UniqueID + " doesn't exist");
		}
		Vertex vertexA = this.vertices.get(strVertex1UniqueID);
		Vertex vertexB = this.vertices.get(strVertex2UniqueID);
		Edge edge = new Edge(new StringBuffer(strEdgeUniqueID), new StringBuffer(strEdgeData), nEdgeCost, vertexA,
				vertexB);
		edges.put(strEdgeUniqueID, edge);
		System.out.println("Inserted Edge " + strEdgeUniqueID + " successfully");
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
		System.out.println("Removed Edge " + strEdgeUniqueID + " successfully");

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

		// empty the stored edges and vertices from previous searches
		visitedEdges = new ArrayList<Edge>();
		visitedVertices = new ArrayList<Vertex>();

		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		Vertex originalVertex = vertices.get(strStartVertexUniqueID);

		this.visit(originalVertex);
		queue.add(originalVertex);

		while (queue.size() != 0) {
			Vertex outVertex = queue.poll();
			Iterator<Edge> edgesOfVertex = outVertex._edges.listIterator();

			while (edgesOfVertex.hasNext()) {
				Edge currentEdge = edgesOfVertex.next();

				if (!visitedEdges.contains(currentEdge)) {
					this.visit(currentEdge);
					visitor.visit(currentEdge);
				}

				Vertex nextVertex = currentEdge.get_vertices()[1];
				if (!visitedVertices.contains(nextVertex)) {
					visitor.visit(nextVertex);
					this.visit(nextVertex);
					queue.add(nextVertex);
				}
			}
		}

	}

	public void dfs(String strStartVertexUniqueID, Visitor visitor) throws GraphException {

		visitedEdges = new ArrayList<Edge>();
		visitedVertices = new ArrayList<Vertex>();

		Stack<Vertex> stack = new Stack<Vertex>();
		Vertex originalVertex = vertices.get(strStartVertexUniqueID);

		this.visit(originalVertex);
		visitor.visit(originalVertex);
		stack.push(originalVertex);

		while (!stack.isEmpty()) {
			boolean noMore = false;

			if (!(stack.peek())._edges.isEmpty()) {
				LinkedList<Edge> edgesOfVertex = (stack.peek())._edges;
				Edge currentEdge = null;

				for (int i = 0; i < edgesOfVertex.size(); i++) {
					currentEdge = edgesOfVertex.get(i);
					if (!(visitedEdges.contains(currentEdge))) {
						this.visit(currentEdge);
						visitor.visit(currentEdge);
						break;
					}
					if (i == edgesOfVertex.size() - 1 && visitedEdges.contains(currentEdge)) {
						noMore = true;
					}
				}
				if (noMore) {
					stack.pop();
				} else {
					Vertex nextVertex = currentEdge.get_vertices()[1];
					if (!visitedVertices.contains(nextVertex)) {
						this.visit(nextVertex);
						visitor.visit(nextVertex);
						stack.push(nextVertex);
					} else {
						noMore = true;
						stack.pop();
					}
				}
			} else {
				stack.pop();
			}
		}
		for (int i = 0; i < visitedVertices.size(); i++) {
			System.out.println("number " + i + " is " + visitedVertices.get(i)._strUniqueID);
		}
	}

	private ArrayList<Vertex[]> divideVerticesByMedian(Vertex[] curr, boolean horizontal, int median) {
		ArrayList<Vertex[]> out = new ArrayList<Vertex[]>();
		ArrayList<Vertex> first = new ArrayList<Vertex>();
		ArrayList<Vertex> second = new ArrayList<Vertex>();

		// to divide points at the median equally in the 2 lists
		boolean lastMed = true;

		for (Vertex v : curr) {
			if (horizontal) {
				if (v.getY() == median) {
					if (lastMed) {
						second.add(v);
					} else {
						first.add(v);
					}
					lastMed = !lastMed;
				} else if (v.getY() > median) {
					second.add(v);
				} else {
					first.add(v);
				}
			} else {
				if (v.getX() == median) {
					if (lastMed) {
						second.add(v);
					} else {
						first.add(v);
					}
					lastMed = !lastMed;
				} else if (v.getX() >= median) {
					second.add(v);
				} else {
					first.add(v);
				}
			}
		}
		out.add(0, first.toArray(new Vertex[0]));
		out.add(1, second.toArray(new Vertex[0]));
		return out;
	}

	private Vertex[] getMidVertices(Vertex[] curr, boolean horizontal, int median, double range) {
		ArrayList<Vertex> midVer = new ArrayList<Vertex>();

		for (Vertex v : curr) {
			if (horizontal) {
				if (v.getY() > median - range / 2.0 && v.getY() < median + range / 2.0) {
					midVer.add(v);
				}
			} else {
				if (v.getX() > median - range / 2.0 && v.getX() < median + range / 2.0) {
					midVer.add(v);
				}
			}
		}

		return midVer.toArray(new Vertex[0]);
	}

	private int getMedian(Vertex[] curr, boolean horizontal) {

		ArrayList<Integer> points = new ArrayList<Integer>();

		if (horizontal) {
			for (Vertex v : curr) {
				points.add(v.getY());
			}
		} else {
			for (Vertex v : curr) {
				points.add(v.getX());
			}
		}
		Collections.sort(points);
		return points.get(curr.length / 2);
	}

	private Vertex[] closestPairHelper(Vertex[] curr, boolean horizontal) throws GraphException {
		// @param horizontal : specifies if the next division will be by X or Y
		int n = curr.length;
		if (n <= 2) {
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
		} else if (min == rightDist) {
			return rightOut;
		} else {
			return midOut;
		}
	}

	// finds the closest pair of vertices using divide and conquer
	// algorithm. Use X and Y attributes in each vertex.
	public Vertex[] closestPair() throws GraphException {
		Collection<Vertex> verts = this.vertices.values();
		Vertex[] verticesOut = verts.toArray(new Vertex[0]);
		Vertex[] outHor = closestPairHelper(verticesOut, true);
		Vertex[] outVer = closestPairHelper(verticesOut, false);

		double disHor = Vertex.getDistance(outHor);
		double disVer = Vertex.getDistance(outVer);

		if (disHor <= disVer) {
			return outHor;
		} else {
			return outVer;
		}
	}

	// for testing
	public void printPresentEdgesAndVertices() {
		System.out.println(this.vertices.toString());
		System.out.println(this.edges.toString());

	}

	public Vector<PathSegment> pathDFS(String strStartVertexUniqueID, String strEndVertexUniqueID)
			throws GraphException {
		path = new Vector<PathSegment>(); // init
		p = new PathSegment();
		visitedEdges = new ArrayList<Edge>();
		visitedVertices = new ArrayList<Vertex>();
		Vertex originalVertex = vertices.get(strStartVertexUniqueID);
		Vertex endVertex = vertices.get(strEndVertexUniqueID);
		localPath.add(originalVertex); // add src to path
		pathDfsHelper(originalVertex, endVertex);
		for (int i = 0; i < finalPath.size(); i++) {// after getting the final path of vertex and edges , init new
													// vector of pathsegment
			p = new PathSegment();
			p._vertex = finalPath.get(i);
			if (i < finalPath.size() - 1) {
				p._edge = testEdge.get(i);
			}
			path.add(p);
		}
		return path;
	}

	// this method discovers all paths from src to dest using dfs , however 1 path
	// is shown
	public void pathDfsHelper(Vertex start, Vertex end) { // recursion helper for path using dfs
		this.visit(start); // initially mark source as visited

		if (start.equals(end)) { // gets last possible path if exists
			for (int i = 0; i < localPath.size(); i++) {
				finalPath.add(localPath.get(i));
			}
		}
		for (int i = 0; i < start._edges.size(); i++) { // recur on all vertices adjacent to current vertex
			if (!(visitedEdges.contains(start._edges.get(i)))) {// check that edge is never visited before
				this.visit(start._edges.get(i)); // mark edge as visited
				Vertex next = start._edges.get(i).get_vertices()[0];
				if (start.equals(next)) { // make sure that new vertex is not same as old
					next = start._edges.get(i).get_vertices()[1];
				}
				if (!(visitedVertices.contains(next))) { // check if vertex is visited to avoid cycling
					testEdge.add(start._edges.get(i)); // sets actual edges taken in path
					localPath.add(next); // add vertex in path
					pathDfsHelper(next, end); // recur on new dest
					localPath.remove(next); // removes current vertex in path since there's several paths
				}
			}
		}
		visitedVertices.remove(start); // mark current vertex as unvisited to get the new path once more
	}

	public void printPath() { // prints path created by pathDfs
		for (int i = 0; i < path.size(); i++) {
			System.out.print("(" + path.get(i).getVertex().getUniqueID() + ") ");
			if (path.get(i).getEdge() != null) {
				System.out.print(path.get(i).getEdge().getUniqueID() + " ");
			}
		}
	}

	
	// finds shortest paths using bellman ford dynamic programming returns all such
	// paths starting from given vertex.
	// Use Edge._nEdgeCost attribute in finding the shortest path
	public Vector<Vector<PathSegment>> findShortestPathBF(String strStartVertexUniqueID) throws GraphException {
		HashMap<String, Vector<PathSegment>> shortestPaths = new HashMap<String, Vector<PathSegment>>();
		// maps the key of the vertex to actual cost reaching the vertex
		HashMap<String, Integer> costToVertex = new HashMap<String, Integer>();
		
		//Initializes the cost to all vertices to infinity except for the start vertex to zero
		for (Entry<String, Vertex> entry : this.vertices.entrySet()) {
			if (entry.getKey().equals(strStartVertexUniqueID)) {
				costToVertex.put(entry.getKey(), 0);
			}
			shortestPaths.put(entry.getValue()._strUniqueID.toString(), new Vector<PathSegment>());
			costToVertex.put(entry.getKey(), Integer.MAX_VALUE);

		}

		// picks each vertex and goes through all possible edges to update the cost
		for (int i = 0; i < this.vertices.size(); i++) {
			for (Entry<String, Edge> edge : this.edges.entrySet()) {

				String u = edge.getValue().get_vertices()[0]._strUniqueID.toString();//source vertex id
				String v = edge.getValue().get_vertices()[1]._strUniqueID.toString();//destination vertex id
				int weight = edge.getValue()._nEdgeCost; // weight of the edge between them


				/*if the cost of the source is not infinity and the cost of the source plus the weight of the
				  edge between the source and the destination is less than the cost of the destination vertex
				  then update the cost of the destination vertex*/
				if (costToVertex.get(u) != Integer.MAX_VALUE && costToVertex.get(u)+weight<costToVertex.get(v) ) {

					costToVertex.put(v, costToVertex.get(u) + weight); // update the cost relative to the destination id
					
					Vector<PathSegment> vPath = shortestPaths.get(v);
					Vector<PathSegment> uPath = shortestPaths.get(u);
					
					vPath.clear();
					
					for(PathSegment path: uPath) {
						vPath.add(path);
					}
					vPath.add(new PathSegment(edge.getValue().get_vertices()[1], edge.getValue()));
				}
			}
		}
		
		Vector<Vector<PathSegment>> returnValue = new Vector<Vector<PathSegment>>();
		
		for(Entry<String, Vector<PathSegment>> entry: shortestPaths.entrySet()) {
			returnValue.add(entry.getValue());
		}
		return returnValue;
	}

	
	// finds all shortest paths using Floyd–Warshall dynamic
	// programming algorithm and returns all such paths. Use
	// Edge._nEdgeCost attribute in finding the shortest path
	//
	public Vector<Vector<PathSegment>> findAllShortestPathsFW( ) throws GraphException {
		Hashtable<Vertex, Hashtable<Vertex, Integer>> costs = new Hashtable<Vertex, Hashtable<Vertex, Integer>>();
		
		for (Entry<String, Vertex> e : this.vertices.entrySet()) {
			Vertex v = e.getValue();
			costs.put(v, new Hashtable<Vertex, Integer>());
			costs.get(v).put(v, 0);
		}
		
		for (Entry<String, Edge> e : this.edges.entrySet()) {
			Edge edge = e.getValue();
			costs.get(edge.get_vertices()[0]).put(edge.get_vertices()[1], edge.getCost());
		}
		
		for (Entry<String, Vertex> ei : this.vertices.entrySet()) {
			Vertex vi = ei.getValue();
			for (Entry<String, Vertex> ej : this.vertices.entrySet()) {
				Vertex vj = ei.getValue();
				for (Entry<String, Vertex> ek : this.vertices.entrySet()) {
					Vertex vk = ei.getValue();
					long costij = Integer.MAX_VALUE;
					long costik = Integer.MAX_VALUE;
					long costkj = Integer.MAX_VALUE;
					
					boolean flag = true;
					
					if (costs.get(vi).containsKey(vj)) {
						costij = costs.get(vi).get(vj);
						flag = false;
					}
					if (costs.get(vi).containsKey(vk)) {
						costik = costs.get(vi).get(vk);
						flag = false;
					}
					if (costs.get(vk).containsKey(vj)) {
						costkj = costs.get(vk).get(vj);
						flag = false;
					}
					
					if (flag) {
						continue;
					}
					
					if (costij > costik + costkj) {
						costs.get(vi).put(vj, (int)(costik + costkj));
						
						// updates to path segments here
					}
				}
			}
		}
		return null;
	}
	
	public static void main(String args[]) throws GraphException {
		Graph g = new Graph();
		TestVisitor gVisitor = new TestVisitor();
		g.insertVertex("1", "1", 0, 0);
		g.insertVertex("2", "2", 60, 60);
		g.insertVertex("3", "3", 49, 50);
		g.insertVertex("4", "4", 49, 49);
		g.insertVertex("5", "5", 50, 50);
		g.insertEdge("1", "4", "88", "88", 5);
		g.insertEdge("1", "2", "2", "2", 2);
		g.insertEdge("2", "3", "14", "14", 14);
		g.insertEdge("2", "4", "99", "99", 5);
		g.insertEdge("2", "5", "4", "4", 4);
		g.insertEdge("4", "5", "58", "58", 58);
		g.insertEdge("3", "5", "34", "34", 34);

		// mido
		// Vector<PathSegment> path = g.pathDFS("1", "5");
		// g.printPath(); //prints path of pathDfs

		// aboughazala
		// g.bfs("1",gVisitor);

		// yasser
		// g.dfs("1", gVisitor);

		// returns the vertices and edges you have visited

		System.out.println(gVisitor._strResult);
		// wessam
		// Vertex[] cp= g.closestPair();
		// for(int i=0;i<cp.length;i++)
		// System.out.println(cp[i]._strData.toString());
		//

		// TESTED BAS MOMKN TOBSO FEH TANI PLEASE <3
		// Vector<Edge> incidentEdges = g.incidentEdges("1");
		// Enumeration vectorElementsEnum = incidentEdges.elements();
		// while(vectorElementsEnum.hasMoreElements())
		// System.out.println(vectorElementsEnum.nextElement());
		//
		// Vector<Vertex> outputVertices= g.vertices();
		// vectorElementsEnum = outputVertices.elements();
		// while(vectorElementsEnum.hasMoreElements())
		// System.out.println(vectorElementsEnum.nextElement());
		//
		// Vector<Edge> outputEdges = g.edges();
		// vectorElementsEnum = outputEdges.elements();
		// while(vectorElementsEnum.hasMoreElements())
		// System.out.println(vectorElementsEnum.nextElement());
		//
		// Vertex[] endVerticesOut= g.endVertices("88");
		// System.out.println(endVerticesOut[0]);
		// System.out.println(endVerticesOut[1]);
		//
		// Vertex oppositVertices= g.opposite("4", "88");
		// System.out.println(oppositVertices);

		// g.removeVertex("1");
		// g.removeVertex("2");
		// g.removeVertex("3");
		// g.removeVertex("4");
		// g.removeVertex("5");

		// g.removeEdge("88");
		// g.removeEdge("2");
		// g.removeEdge("14");
		// g.removeEdge("99");
		// g.removeEdge("4");
		// g.removeEdge("58");
		// g.removeEdge("34");

		// g.printPresentEdgesAndVertices();;

	}
}
