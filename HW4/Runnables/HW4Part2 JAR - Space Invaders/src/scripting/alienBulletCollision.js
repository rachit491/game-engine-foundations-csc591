function checkCollision() {
	var ex, ey, ew, eh;
	
	for(var i=0; i<GameClient.alienBullets.size(); i++) {
		var bx = GameClient.alienBullets.get(i).getTransform().getPositionX();
		var by = GameClient.alienBullets.get(i).getTransform().getPositionY();
		var bw = GameClient.alienBullets.get(i).getBox().getWidth();
		var bh = GameClient.alienBullets.get(i).getBox().getHeight();
		
		ex = GameClient.player.get(GameClient.playerid).getTransform().getPositionX();
		ey = GameClient.player.get(GameClient.playerid).getTransform().getPositionY();
		ew = GameClient.player.get(GameClient.playerid).getBox().getWidth();
		eh = GameClient.player.get(GameClient.playerid).getBox().getHeight();

		if(GameClient.player.get(GameClient.playerid).getTransform().getVisible() && GameClient.alienBullets.get(i) != null && 
				((bx > ex && bx < ex+ew) && (by > ey))) {
			//hide the enemy
			//death event
			GameClient.player.get(GameClient.playerid).getTransform().setVisible(false);
			var colo = [0,0,0];
			GameClient.player.get(GameClient.playerid).getBox().setColor(colo);
			GameClient.player.set(GameClient.playerid, null);
			//player.get(playerid).getTransform().setPosition(-99, -99);
			GameClient.alienBullets.remove(i);
			GameClient.showMessage("GAME OVER.");
		}
		
		if(by > 570) {
			GameClient.alienBullets.remove(i);
		}
	}
}