function playerShootEventHandler() {
	if(GameClient.bullet == null) {
		var index = e.getClientID();
		GameClient.bullet = fct.createBullet(index, GameClient.player.get(index));
		GameClient.renderBullet(GameClient.bullet);
	}
}

function alientShootEventHandler() {
	var alienIndex = e.getEventArgs().get(1);
	GameClient.alienBullets.add(fct.createAlienBullet(alienIndex, GameClient.platform.get(alienIndex)));
	var size = GameClient.alienBullets.size();
	GameClient.renderAlienBullet(GameClient.alienBullets.get(size-1));
}