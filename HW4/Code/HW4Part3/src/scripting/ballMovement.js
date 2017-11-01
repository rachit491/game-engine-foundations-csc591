var width;
var height;
var event;

function update(w, h, e) {
	width = w;
	height = h;
	event = e;

	var phy = obj.getComponents().get(physics);
	
	var newX = (obj.getTransform().getPositionX() + phy.velocity.x);
	var newY = (obj.getTransform().getPositionY() + phy.velocity.y);
	
	obj.getTransform().setPosition(newX, newY);
	checkBoundaryCollision();
}
	
function checkBoundaryCollision() {
	var phy = obj.getComponents().get(physics);
	var x = obj.getTransform().getPositionX();
	var y = obj.getTransform().getPositionY();
	
	var w = obj.getBox().getWidth();
	var h = obj.getBox().getHeight();

	if(checkPlayerHit(x, y)) {}
	
	else if(x > width - w) {
		obj.getTransform().setPositionX(width-w);
		phy.velocity.x *= -1;
	}
	else if(x < 0) {
		obj.getTransform().setPositionX(0);
		phy.velocity.x *= -1;
	}
	else if(y > height - h) {
		obj.getTransform().setPositionY(height-h);
		//phy.velocity.y *= -1;
		//death event
		em.trigger(event);
	}
	else if(y < 1) {
		obj.getTransform().setPositionY(1);
		//phy.velocity.y *= -1;
		//death event
		em.trigger(event);
	}
}

function checkPlayerHit(x, y) {
	var px, py, pw, ph, ball;
	var phy = obj.getComponents().get(physics);
	var w = obj.getBox().getWidth();
	var h = obj.getBox().getHeight();

	//player is platforms or paddles
	//obj is the ball
	for(var i=0; i<player.size(); i++) {
		px = player.get(i).getTransform().getPositionX();
		py = player.get(i).getTransform().getPositionY();
		pw = player.get(i).getBox().getWidth();
		ph = player.get(i).getBox().getHeight();
		ball = obj.getBox().getRectangle();

		if(i == 0) {
			//for paddle 1
			if((y + ball.height) > py && (y + ball.height) < (py + ph) && (x + ball.width) > px && x < (px + pw)) {
				obj.getTransform().setPositionY(y-2);
				phy.velocity.y *= -1;	
				return true;
			}
		}
		else {
			//for paddle 2
			if(y < (py + ph) && y > py && (x + ball.width) > px && x < (px + pw)) {
				obj.getTransform().setPositionY(y+2);
				phy.velocity.y *= -1;
				return false;
			}
		}
	}
}