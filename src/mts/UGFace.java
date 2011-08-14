// UGFace
// Triangle, v1-3 zijn nummers van bijbehorende vertices, c1-3 de corresponderende kleuren, onderdeel van een mesh

package mts;

class UGFace
{
	
	int v1, v2, v3;
	UGColor c1, c2, c3;
	
	public UGFace(int v1, int v2, int v3)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		
		this.c1 = new UGColor();
		this.c2 = new UGColor();
		this.c3 = new UGColor();
	}
	
	public UGFace(int v1, int v2, int v3, 
				  UGColor c1, UGColor c2, UGColor c3)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
	}
	
}
