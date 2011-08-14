// UGWorld
// Bevat alle world data. Voeg meshes toe met addMesh. Bewaar het resultaat, want daarmee verwijs je in addObject naar de goeie mesh.
// Het is dus de bedoeling dat je een mesh maar 1x toevoegt, en voor elke keer dat je 'm gerenderd wilt hebben voeg je een object toe
// op de goeie plaats, dat naar je mesh verwijst.
// render() pusht de modelview matrix, hopst heen en weer met glTranslatef, rendert de object list en popt de modelview matrix

package mts;

import static org.lwjgl.opengl.GL11.*;

import java.util.Enumeration;
import java.util.Vector;

class UGWorld
{
	Vector<UGMesh> meshes;
	Vector<UGObject> objects;
	
	public UGWorld()
	{
		meshes = new Vector<UGMesh>();
		objects = new Vector<UGObject>();
	}
	
	public int addMesh(UGMesh mesh)
	{
		meshes.add(mesh);
		return meshes.indexOf(mesh);
	}
	
	public void addObject(UGObject object)
	{
		objects.add(object);
	}
	
	public UGVertex[] squiggly(int n, UGVertex v1, UGVertex v2)
	{
		UGVertex[] v = new UGVertex[n];
		
		double xd = (v2.x - v1.x);
		double yd = (v2.y - v1.y);
		double zd = (v2.z - v1.z);
		
		//double l = Math.sqrt(xd*xd + yd*yd + zd*zd);
		
		for (int i=0; i<n; i++)
		{
			double randPhase = System.nanoTime() / 50000000;
			double addX = 0.4*Math.cos(2 * Math.PI * i * 0.06312547 + randPhase);
			double addY = 0.5*Math.cos(2 * Math.PI * i * 0.05345 + randPhase);
			double addZ = 0.5*Math.cos(2 * Math.PI * i * 0.013275 + randPhase);
			
			//addX *= Math.random() * 3;
			//addY *= Math.random() * 3;
			
			float x = (float)(v1.x + i * xd / (n-1) + addX);
			float y = (float)(v1.y + i * yd / (n-1) + addY);
			float z = (float)(v1.z + i * zd / (n-1) + addZ);
			v[i] = new UGVertex(x, y, z);
		}
		
		return v;
	}
	
	public void render()
	{
		glPushMatrix();
		
		for (Enumeration<UGObject> e = objects.elements(); e.hasMoreElements(); )
		{
			UGObject object = e.nextElement();
			UGMesh mesh = meshes.elementAt(object.mesh);
			
			//glLoadIdentity();
			glRotatef((float)object.rot.x, 1, 0, 0);
			glRotatef((float)object.rot.y, 0, 1, 0);
			glRotatef((float)object.rot.z, 0, 0, 1);
			glTranslatef((float)object.pos.x, (float)object.pos.y, (float)object.pos.z);
			
			//glEnable(GL_STENCIL_TEST);
			
			//glStencilFunc(GL_ALWAYS, 1, 1);
			//glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
			
			//glLineWidth(5.f);
			/*
			glBegin(GL_LINES);
			
			for (int i=0; i<mesh.nFaces; i++)
			{
				UGFace f = mesh.faces[i];
				
				UGVertex v1 = mesh.vertices[f.v1];
				UGVertex v2 = mesh.vertices[f.v2];
				UGVertex v3 = mesh.vertices[f.v3];
				
				
				UGVertex[] lijntje = squiggly(50, v1, v2);
				
				for (int j=0; j<49; j++)
				{
					//glLineWidth((float)(2 * (0.5 - 0.5 * Math.cos(Math.PI * j / 48))));
					float c = (float)(0.5 + 0.5 * Math.cos(Math.PI * j / 48));
					glColor3f(c, c, c);
					glVertex3f(lijntje[j].x, lijntje[j].y, lijntje[j].z);
					glVertex3f(lijntje[j+1].x, lijntje[j+1].y, lijntje[j+1].z);
				}
				
				lijntje = squiggly(50, v2, v3);
				
				for (int j=0; j<49; j++)
				{
					//glLineWidth((float)(2 * (0.5 - 0.5 * Math.cos(Math.PI * j / 48))));
					float c = (float)(0.5 + 0.5 * Math.cos(Math.PI * j / 48));
					glColor3f(c, c, c);
					glVertex3f(lijntje[j].x, lijntje[j].y, lijntje[j].z);
					glVertex3f(lijntje[j+1].x, lijntje[j+1].y, lijntje[j+1].z);
				}
				
				lijntje = squiggly(50, v3, v1);
				
				for (int j=0; j<49; j++)
				{
					//glLineWidth((float)(2 * (0.5 - 0.5 * Math.cos(Math.PI * j / 48))));
					float c = (float)(0.5 + 0.5 * Math.cos(Math.PI * j / 48));
					glColor3f(c, c, c);
					glVertex3f(lijntje[j].x, lijntje[j].y, lijntje[j].z);
					glVertex3f(lijntje[j+1].x, lijntje[j+1].y, lijntje[j+1].z);
				}
				
				glColor3f(0,0,0);
				//glColor3f((float)f.c1.r, (float)f.c1.g, (float)f.c1.b);
				glVertex3f((float)v1.x, (float)v1.y, (float)v1.z);
				
				//glColor3f((float)f.c2.r, (float)f.c2.g, (float)f.c2.b);
				glColor3f(0,0,0);
				glVertex3f((float)v2.x, (float)v2.y, (float)v2.z);
				
				// deze niet renderen bij de even faces
				if (i % 2 != 0)
				{
					//glColor3f((float)f.c2.r, (float)f.c2.g, (float)f.c2.b);
					glColor3f(0,0,0);
					glVertex3f((float)v2.x, (float)v2.y, (float)v2.z);
					
					//glColor3f((float)f.c3.r, (float)f.c3.g, (float)f.c3.b);
					glColor3f(0,0,0);
					glVertex3f((float)v3.x, (float)v3.y, (float)v3.z);
				}
				
				
				// deze niet renderen bij de oneven faces
				if (i % 2 == 0)
				{
					glColor3f(0,0,0);
					//glColor3f((float)f.c3.r, (float)f.c3.g, (float)f.c3.b);
					glVertex3f((float)v3.x, (float)v3.y, (float)v3.z);
					
					glColor3f(0,0,0);
					//glColor3f((float)f.c1.r, (float)f.c1.g, (float)f.c1.b);
					glVertex3f((float)v1.x, (float)v1.y, (float)v1.z);
				}
			}
			
			glEnd();
			*/
			//glEnable(GL_STENCIL_TEST);
			//glStencilFunc(GL_EQUAL, 0, 1);
			//glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
			
			glBegin(GL_TRIANGLES);
			
			for (int i=0; i<mesh.nFaces; i++)
			{
				UGFace f = mesh.faces[i];
				
				UGVertex v1 = mesh.vertices[f.v1];
				UGVertex v2 = mesh.vertices[f.v2];
				UGVertex v3 = mesh.vertices[f.v3];
				
				glColor3f((float)f.c1.r, (float)f.c1.g, (float)f.c1.b);
				glVertex3f((float)v1.x, (float)v1.y, (float)v1.z);
				
				glColor3f((float)f.c2.r, (float)f.c2.g, (float)f.c2.b);
				glVertex3f((float)v2.x, (float)v2.y, (float)v2.z);

				glColor3f((float)f.c3.r, (float)f.c3.g, (float)f.c3.b);
				glVertex3f((float)v3.x, (float)v3.y, (float)v3.z);
			}
			
			glEnd();
			
			//glDisable(GL_STENCIL_TEST);
			
			glTranslatef(-(float)object.pos.x, -(float)object.pos.y, -(float)object.pos.z);
			
		}
		
		glPopMatrix();
		
	}
}
