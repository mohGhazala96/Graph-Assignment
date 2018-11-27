import java.util.LinkedList;

public class Vertex {

	protected StringBuffer _strUniqueID, _strData;
	protected int _nX, _nY;
	protected LinkedList<Edge> _edges;
	
	public Vertex(StringBuffer _strUniqueID, StringBuffer _strData, int _nX, int _nY) {
		super();
		this._strUniqueID = _strUniqueID;
		this._strData = _strData;
		this._nX = _nX;
		this._nY = _nY;
		this._edges= new LinkedList<Edge>();
	}

	public Vertex(StringBuffer _strUniqueID, StringBuffer _strData) {
		new Vertex(_strUniqueID, _strData, 0, 0);
	}

	
	// a unique id identifying vertex
	// data associated with vertex
	// Coordinates of vertex on some
	// map. Assume 0,0 is
	// bottom left.
	public StringBuffer getUniqueID() {
		return _strUniqueID;
	}

	public StringBuffer getData() {
		return _strData;
	}

	public int getX() {
		return _nX;
	}

	public int getY() {
		return _nY;
	}
	
	public static double getDistance(Vertex[] vertexTuple) {
		if (vertexTuple.length <= 1) {
			return Double.MAX_VALUE;
		} else {
			int x1 = vertexTuple[0].getX();
			int y1 = vertexTuple[0].getY();
			int x2 = vertexTuple[1].getX();
			int y2 = vertexTuple[1].getY();
			
			return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
		}
		
	}
}
