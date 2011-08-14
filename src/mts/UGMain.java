package mts;

import java.util.Enumeration;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;
//import java.util.*;
import org.lwjgl.input.*;

class UGGameState
{
	int nPlayers;
	
	int[][] playerBlocks;
	UGVertex[] playerPos;
	double[] playerPaddleAngle;
	UGColor[] playerColor;
	
	UGVertex[] ballPos;

	public UGGameState(int nPlayers)
	{
		playerPos = new UGVertex[4];
		playerColor = new UGColor[4];
		
		playerPos[0] = new UGVertex(-45, 0, 95);
		playerPos[1] = new UGVertex(45, 0, 95);
		playerPos[2] = new UGVertex(-45, 0, -95);
		playerPos[3] = new UGVertex(45, 0, -95);
		
		playerColor[0] = new UGColor(1, 0.5, 0.5, 1);
		playerColor[1] = new UGColor(0.5, 1, 0.5, 1);
		playerColor[2] = new UGColor(0.5, 0.5, 1, 1);
		playerColor[3] = new UGColor(1, 1, 0.5, 1);
		
		playerPaddleAngle = new double[4];
		
		playerPaddleAngle[0] = 0;
		
		playerBlocks = new int[4][];
		
		for (int i=0; i<4; i++)
			playerBlocks[i] = new int[27];
		
		for (int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				for (int k=0; k<3; k++)
					for (int n=0; n<4; n++)
						playerBlocks[n][i*9 + j * 3 + k] = 1;
		
	}
}

public class UGMain {
	
	long time;
	long timeDelta;
	double playerLookPhi, playerLookTheta;
	UGVertex[][] blockPoints;	
	UGGameState gs;
	
	public void start() 
	{
		try 
		{
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		} 
		catch (LWJGLException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		gs = new UGGameState(4);
		
		blockPoints = new UGVertex[4][];
		for (int i=0; i<4; i++)
			blockPoints[i] = new UGVertex[27];
		
		// init OpenGL here
		UGWorld world = new UGWorld();
		int DOOS = world.addMesh(new UGBox(70, 40, 120, new UGColor(1,1,1,1)));
		int BLOKJE_P1 = world.addMesh(new UGBox(3,3,3, gs.playerColor[0]));
		int BLOKJE_P2 = world.addMesh(new UGBox(3,3,3, gs.playerColor[1]));
		int BLOKJE_P3 = world.addMesh(new UGBox(3,3,3, gs.playerColor[2]));
		int BLOKJE_P4 = world.addMesh(new UGBox(3,3,3, gs.playerColor[3]));
		int PADDLE_P1 = world.addMesh(new UGBox(8, 6, 0.5, gs.playerColor[0]));
		
		world.addObject(new UGObject(DOOS, new UGVertex(0, 0, 0), new UGVertex()));
		
		for (int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				for (int k=0; k<3; k++)
					for (int n=0; n<4; n++)
						blockPoints[n][i*9 + j * 3 + k] = new UGVertex(gs.playerPos[n].x + -8 + 8 * i, 
																	   gs.playerPos[n].y + -8 + 8 * j, 
																	   gs.playerPos[n].z + -8 + 8 * k);
		
		for (int j=0; j<27; j++)
		{
			if (gs.playerBlocks[0][j] != 0)
				world.addObject(new UGObject(BLOKJE_P1, blockPoints[0][j], new UGVertex()));
			
			if (gs.playerBlocks[1][j] != 0)
				world.addObject(new UGObject(BLOKJE_P2, blockPoints[1][j], new UGVertex()));
			
			if (gs.playerBlocks[2][j] != 0)
				world.addObject(new UGObject(BLOKJE_P3, blockPoints[2][j], new UGVertex()));
			
			if (gs.playerBlocks[3][j] != 0)
				world.addObject(new UGObject(BLOKJE_P4, blockPoints[3][j], new UGVertex()));
		}
		
		glViewport(0, 0, 800, 600);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(60, 800.f/600.f, 1.f, 400.f);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_DEPTH_TEST);
		
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); 
		glEnable(GL_LINE_SMOOTH); 
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		
		time = System.nanoTime()/1000000;
		
		playerLookPhi = playerLookTheta = 0;
		UGVertex vForward = new UGVertex(0, 0, 1);
		UGVertex vUpward = new UGVertex(0, 1, 0);
		
		glDisable(GL_CULL_FACE);
		
		long t = System.nanoTime();
		
		while (!Display.isCloseRequested() && ! Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) 
		{
			
			timeDelta = System.nanoTime() - t;
			t = System.nanoTime();
			
			glClearColor(0.f, 0.f, 0.f, 0.f);
			glClearStencil(0);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			
			glLoadIdentity();
			float x = (float)(40 * Math.cos(t/1013));
			float z = (float)(40 * Math.sin(t/1013));
			
			if (Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				playerLookPhi -= timeDelta/1000000000.f * 90;
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				playerLookPhi += timeDelta/1000000000.f * 90;
			}
			
			gs.playerPaddleAngle[0] += Mouse.getDX() * 0.3;
			//playerLookTheta += Mouse.getDY() * 0.3;
			//if (playerLookTheta > 90)
			//	playerLookTheta = 90;
			//if (playerLookTheta < -90)
			//	playerLookTheta = -90;
			
			Mouse.setCursorPosition(400, 300);
			
			//glRotatef(-(float)playerLookTheta, 1, 0, 0);
			glRotatef((float)playerLookPhi, 0, 1, 0);
			glTranslatef(50, -20, -100);
			
			//gluLookAt(-50, 20, 100, 0, 0, 0, 0, 1, 0);
			
			world.render();
			
			glPushMatrix();
			
			for (int n=0; n<1; n++)
			{
				float addX = (float)(25 * Math.cos(-Math.PI / 2 + Math.PI * gs.playerPaddleAngle[n] / 180));
				float addZ = (float)(25 * Math.sin(-Math.PI / 2 + Math.PI * gs.playerPaddleAngle[n] / 180));
				glTranslatef((float)gs.playerPos[n].x+addX, (float)gs.playerPos[n].y, (float)gs.playerPos[n].z+addZ);
				glRotatef(-(float)gs.playerPaddleAngle[n], 0, 1, 0);
				
							
				UGMesh mesh = world.meshes.elementAt(PADDLE_P1);
				
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
				
				glTranslatef(-(float)gs.playerPos[n].x, -(float)gs.playerPos[n].y, -(float)gs.playerPos[n].z);
				
			}
			
			glPopMatrix();
			
			Display.update();
		}
		 
		Display.destroy();
		
	}
		 
	public static void main(String[] argv) 
	{
		UGMain game = new UGMain();
		game.start();
	}
}
