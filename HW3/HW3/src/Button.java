import processing.core.PApplet;

public class Button extends PApplet {
	int rectX, rectY;      // Position of square button
	int circleX, circleY;  // Position of circle button
	int rectSize = 90;     // Diameter of rect
	int circleSize = 93;   // Diameter of circle
	boolean rectOver = false;
	boolean circleOver = false;

	public void setup() {
	  //size(400, 50);
	  circleX = width/2+circleSize/2+10;
	  circleY = height/2;
	  rectX = width/2-rectSize-10;
	  rectY = height/2-rectSize/2;
	  ellipseMode(CENTER);
	}

	public void draw() {
	  update(mouseX, mouseY);
	  background(0);
	  
	  if (rectOver) {
	    fill(0,255,0);
	  } else {
	    fill(255);
	  }
	  stroke(255);
	  rect(rectX, rectY, rectSize, rectSize);
	  
	  if (circleOver) {
	    fill(255,0,0);
	  } else {
	    fill(255);
	  }
	  stroke(0);
	  ellipse(circleX, circleY, circleSize, circleSize);
	}

	public void update(int x, int y) {
	  if ( overCircle(circleX, circleY, circleSize) ) {
	    circleOver = true;
	    rectOver = false;
	  } else if ( overRect(rectX, rectY, rectSize, rectSize) ) {
	    rectOver = true;
	    circleOver = false;
	  } else {
	    circleOver = rectOver = false;
	  }
	}

	public void mousePressed() {
	  if (circleOver) {
	    System.out.println("Circle");
	  }
	  if (rectOver) {
		  System.out.println("Square");
	  }
	}

	public boolean overRect(int x, int y, int width, int height)  {
	  if (mouseX >= x && mouseX <= x+width && 
	      mouseY >= y && mouseY <= y+height) {
	    return true;
	  } else {
	    return false;
	  }
	}

	public boolean overCircle(int x, int y, int diameter) {
	  float disX = x - mouseX;
	  float disY = y - mouseY;
	  if (sqrt(sq(disX) + sq(disY)) < diameter/2 ) {
	    return true;
	  } else {
	    return false;
	  }
	}

}
