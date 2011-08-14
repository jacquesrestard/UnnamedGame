// UGObject
// Renderbaar object. Verwijst naar het nummer van de bijbehorende mesh, en heeft een positie, zodat we dingen van mesh space
// naar world space kunnen vertalen.

package mts;

class UGObject
{
	int mesh;
	UGVertex pos;
	UGVertex rot;
	
	public UGObject(int mesh, UGVertex pos, UGVertex rot)
	{
		this.mesh = mesh;
		this.pos = pos;
		this.rot = rot;
	}
}
