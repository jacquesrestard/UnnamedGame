// UGMesh
// Meest basic object. Heeft vertices en faces. Dit is wat je extendtdtdtdt om je eigen 3d models te definieren.
// Zie UGBox voor een voorbeeld. Alle punten zijn gedefinieerd in mesh space, niet in world space.

package mts;

class UGMesh
{
	
	UGVertex[] vertices;
	UGFace[] faces;
	
	int nFaces, nVertices;
	
	public UGMesh(int nVertices, int nFaces) 
	{
		this.nVertices = nVertices;
		this.nFaces = nFaces;
		
		vertices = new UGVertex[nVertices];
		faces = new UGFace[nFaces];
	}
	
}
