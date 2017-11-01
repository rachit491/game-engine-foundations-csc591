import processing.core.PApplet;
import java.awt.*;
import processing.core.PShape;

public class UsingProcessing extends PApplet{
	int x = 20;
	int y = 475;
	int directionX = 1;
	PShape object, obstacle1, obstacle2, obstacle3;
	Rectangle objRect, obsRect1, obsRect2, obsRect3;
	
	public static void main(String[] args) {
		PApplet.main("UsingProcessing");

	}
	
	public void settings() {
		size(800, 600);
		smooth();
		objRect = new Rectangle(0, 0, 35, 75);
		obsRect1 = new Rectangle(640, 515, 20, 35);
		obsRect2 = new Rectangle(150, 515, 20, 35);
		obsRect3 = new Rectangle(450, 500, 35, 50);
	}
	
	public void setup() {
		fill(120, 50, 250);
	}
	
	public void draw() {
		background(12, 214, 201);
		
		//Sun
		fill(255, 255, 0);
		ellipse(600, 100, 100, 100);
		
		//Object
		x+=directionX;
		if(x > 799)
			x = 0;
		if(x < 0)
			x = 765;
		if(y < 475)
			y += 5;
		stroke(25, 55, 36);
		fill(105, 25, 31);
		object = createShape(RECT, x, y, 35, 75);
		shape(object);
		//update x-y for awt.Rectangle
		objRect.setLocation(x, y);
		
		//Creating a platform for object to move
		stroke(0);
		fill(25, 5, 100);
		rect(0, 550, 800, 50);
		
		//Obstacles
		stroke(125);
		fill(150);
		obstacle1 = createShape(RECT, 640, 515, 20, 35);
		shape(obstacle1);
		stroke(125);
		fill(150);
		obstacle2 = createShape(RECT, 150, 515, 20, 35);
		shape(obstacle2);
		stroke(25);
		fill(100);
		obstacle3 = createShape(RECT, 450, 500, 35, 50);
		shape(obstacle3);
	
		if((obsRect1.intersects(objRect) || obsRect2.intersects(objRect) || obsRect3.intersects(objRect)) && directionX != 0) {
			directionX = 0;
		}
	}
	
	public void keyPressed() {
	  if (key == CODED) {
	    if (keyCode == LEFT) {
    		directionX = -1;
			x+=10*directionX;
	    }
	    else if (keyCode == RIGHT) {
	    	directionX = 1;
			x+=10*directionX;
	    }
	  }
	  if (key == ' ') {
		  x+=(directionX);
		  y-=100;
	  }
	}
}
