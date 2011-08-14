package mts;

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
	double playerPaddleAngle;
	
	UGVertex[] ballPos;

	public UGGameState(int nPlayers)
	{
		playerPos = new UGVertex[4];
		
		playerPos[0] = new UGVertex(-45, 0, 95);
		playerPos[1] = new UGVertex(45, 0, 95);
		playerPos[2] = new UGVertex(-45, 15, -95);
		playerPos[3] = new UGVertex(45, 15, -95);
		
		playerBlocks = new int[4][];
		
		for (int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				for (int k=0; k<3; k++)
					for (int n=0; n<4; n++)
						playerBlocks[n][i*9 + j * 3 + k] = 1;
		
	}
}

public class UGMain {
	
	long time;
	int timeDelta;
	double playerLookPhi, playerLookTheta;
	UGVertex[][] blockPoints;	
	
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
		
		blockPoints = new UGVertex[4][];
		for (int i=0; i<4; i++)
			blockPoints[i] = new UGVertex[27];
		
		
		
		// init OpenGL here
		UGWorld world = new UGWorld();
		int DOOS = world.addMesh(new UGBox(50, 20, 100, new UGColor(1,1,1,1)));
		int BLOKJE = world.addMesh(new UGBox(0.7,0.7,0.7, new UGColor(1,0.5,0.5,1)));
		
		world.addObject(new UGObject(DOOS, new UGVertex(0, 0, 0)));
		
		for (int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				for (int k=0; k<3; k++)
					for (int n=0; n<4; n++)
						blockPoints[n][i*9 + j * 3 + k] = new UGVertex(-1.5 + 1.5 * i, 
																	   -1.5 + 1.5 * j, 
																	   -1.5 + 1.5 * k);
		
		for (int i=0; i<1; i++)
			for (int j=0; j<27; j++)
			{
				world.addObject(new UGObject(BLOKJE, blockPoints[i][j]));
			}
		
		glViewport(0, 0, 800, 600);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(60, 800.f/600.f, 1.f, 200.f);
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
		
		while (!Display.isCloseRequested() && ! Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) 
		{
			
			float t = System.nanoTime()/1000000-time;
			
			glClearColor(0.f, 0.f, 0.f, 0.f);
			glClearStencil(0);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			
			glLoadIdentity();
			float x = (float)(40 * Math.cos(t/1013));
			float z = (float)(40 * Math.sin(t/1013));
			
			playerLookPhi += Mouse.getDX() * 0.3;
			playerLookTheta += Mouse.getDY() * 0.3;
			if (playerLookTheta > 90)
				playerLookTheta = 90;
			if (playerLookTheta < -90)
				playerLookTheta = -90;
			
			Mouse.setCursorPosition(400, 300);
			
			glRotatef(-(float)playerLookTheta, 1, 0, 0);
			glRotatef((float)playerLookPhi, 0, 1, 0);
			glTranslatef(20 * (float)Math.cos(t*0.001), 0, 20 * (float)Math.sin(t*0.001));
			
			//gluLookAt(0, 0, -20, 
			//		(float)(Math.sin(playerLookTheta)*Math.cos(playerLookPhi)),
			//		(float)(Math.sin(playerLookTheta)*Math.sin(playerLookPhi)), 
			//		-20 + (float)Math.cos(playerLookTheta), 0.f, 1.f, 0.f);
			
			world.render();
			
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
