
public class Edge {
	// a unique id identifying edge
	// data associated with this edge.
	// Data could be name of edge or
	// any meaningful property for
	// an edge.
	// cost of traversing this edge

	protected StringBuffer _strUniqueID, _strData;
	protected int _nEdgeCost;

	public StringBuffer getUniqueID() {
		return _strUniqueID;
	}

	public StringBuffer getData() {
		return _strData;
	}

	public int getCost() {
		return _nEdgeCost;
	}
}
