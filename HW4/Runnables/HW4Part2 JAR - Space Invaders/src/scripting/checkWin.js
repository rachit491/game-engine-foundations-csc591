function checkWin() {
	var count = 0;
	for(var i=0; i< platform.size(); i++) {
		if(!platform.get(i).getTransform().getVisible()) {
			count++;
		}
	}

	if(count == platform.size()) {
		GameClient.showMessage("YOU WIN!");
	}
}