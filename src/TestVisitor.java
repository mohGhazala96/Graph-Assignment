
public class TestVisitor implements Visitor {
	protected String _strResult = new String();

	public void visit(Vertex v) {
		_strResult += "v=" + v.getUniqueID() + " ";
	}

	public void visit(Edge e) {
		_strResult += "e=" + e.getUniqueID() + " ";
	}

	public String getResult() {
		return _strResult;
	}
}