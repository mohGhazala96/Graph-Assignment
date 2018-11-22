
public class Vertex {

protected StringBuffer _strUniqueID, _strData;
protected int _nX,_nY;
//a unique id identifying vertex
//data associated with vertex
//Coordinates of vertex on some
//map. Assume 0,0 is
//bottom left.
public StringBuffer getUniqueID( ){ return _strUniqueID;
}
public StringBuffer getData( ){
return _strData;
}
public int getX( ){
return _nX;
}
public int getY( ){
return _nY;
}
}
