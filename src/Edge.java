
public class Edge {
	// a unique id identifying edge
	// data associated with this edge.
	// Data could be name of edge or
	// any meaningful property for
	// an edge.
	// cost of traversing this edge

	protected StringBuffer _strUniqueID, _strData;
	protected int _nEdgeCost;
	
	private Vertex[] _vertices;

	
	
	public Edge(StringBuffer _strUniqueID, StringBuffer _strData, int _nEdgeCost, Vertex v1, Vertex v2) {
		
		this._strUniqueID = _strUniqueID;
		this._strData = _strData;
		this._nEdgeCost = _nEdgeCost;

		this._vertices = new Vertex[2];
		this._vertices[0] = v1;
		this._vertices[1] = v2;
		
		v1._edges.add(this);
		v2._edges.add(this);
	}

	public Vertex getConnectedVertex(Vertex vertex) {
		/*
		 * ask wessam
		 */
		if (this._vertices[0].equals(vertex)) {
			return this._vertices[1];
		} else if(this._vertices[1].equals(vertex)) {
			return this._vertices[0];
		} else {
			return null;
		}
	}
	
	public StringBuffer getUniqueID() {
		return _strUniqueID;
	}

	public StringBuffer getData() {
		return _strData;
	}

	public Vertex[] get_vertices() {
		return _vertices;
	}

	public int getCost() {
		return _nEdgeCost;
	}
	protected void removeEdge() {
		this._vertices[0]._edges.remove(this);
		this._vertices[1]._edges.remove(this);

	}
//	public int compareTo(Edge e){
//		if(this._nEdgeCost == e._nEdgeCost){
//			return 0;
//		}
//		else{
//			if(this._nEdgeCost < e._nEdgeCost){
//				return -1;
//			}
//			else{
//				return 1;
//			}
//		}
//	}
}
