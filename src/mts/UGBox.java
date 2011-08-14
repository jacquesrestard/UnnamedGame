// UGBox
// extends UGMesh, definieert een doosje van afmetingen 2x xs, ys, zs.

package mts;

class UGBox extends UGMesh
{
	UGColor col;
	
	public UGBox(double xs, double ys, double zs, UGColor col)
	{
		super(8, 12); // create object with 8 verts and 12 faces
		
		vertices[0] = new UGVertex(-xs, ys, zs);
		vertices[1] = new UGVertex(xs, ys, zs);
		vertices[2] = new UGVertex(xs, -ys, zs);
		vertices[3] = new UGVertex(-xs, -ys, zs);
		
		vertices[4] = new UGVertex(-xs, ys, -zs);
		vertices[5] = new UGVertex(xs, ys, -zs);
		vertices[6] = new UGVertex(xs, -ys, -zs);
		vertices[7] = new UGVertex(-xs, -ys, -zs);
		
		faces[0] = new UGFace(0, 1, 3, col, col, col);
		faces[1] = new UGFace(1, 2, 3, col, col, col);		
		faces[2] = new UGFace(1, 5, 2, col, col, col);
		faces[3] = new UGFace(5, 6, 2, col, col, col);
		faces[4] = new UGFace(5, 4, 6, col, col, col);
		faces[5] = new UGFace(4, 7, 6, col, col, col);
		faces[6] = new UGFace(4, 0, 7, col, col, col);
		faces[7] = new UGFace(0, 3, 7, col, col, col);
		faces[8] = new UGFace(4, 5, 0, col, col, col);
		faces[9] = new UGFace(5, 1, 0, col, col, col);
		faces[10] = new UGFace(3, 2, 7, col, col, col);
		faces[11] = new UGFace(2, 6, 7, col, col, col);
	}
}
