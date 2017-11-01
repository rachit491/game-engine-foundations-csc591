function checkCollision() {
	var ex, ey, ew, eh;
	var bx = GameClient.bullet.getTransform().getPositionX();
	var by = GameClient.bullet.getTransform().getPositionY();
	var bw = GameClient.bullet.getBox().getWidth();
	var bh = GameClient.bullet.getBox().getHeight();
	
	for(var i = 0; i < GameClient.platform.size(); i++) {
		ex = GameClient.platform.get(i).getTransform().getPositionX();
		ey = GameClient.platform.get(i).getTransform().getPositionY();
		ew = GameClient.platform.get(i).getBox().getWidth();
		eh = GameClient.platform.get(i).getBox().getHeight();

		if(GameClient.platform.get(i).getTransform().getVisible() && GameClient.bullet != null && 
				((bx > ex && bx < ex+ew) && (by <= ey+eh))) {
			//hide the enemy
			//death event
			GameClient.platform.get(i).getTransform().setVisible(false);
			GameClient.platform.get(i).getTransform().setPosition(-99, -99);
			GameClient.platform.get(i).getAnimator().setupInitialValues(0,0,0,0);
			GameClient.platform.get(i).getAnimator().playX(false);
			GameClient.bullet = null;
			print("Shot " + i);
		}
		
		if(by < 10) {
			GameClient.bullet = null;
		}
	}
}