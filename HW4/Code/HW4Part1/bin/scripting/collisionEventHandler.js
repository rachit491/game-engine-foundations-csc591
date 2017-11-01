function checkCollision(e) {
	var args = e.getEventArgs();
	var x = args.get(0);
	var y = args.get(1);
	var index = args.get(2);
	var platform = args.get(3);
	
	for(var i = 0; i < platform.size(); i++) {
		plt = platform.get(i).getBox().getRectangle();
		if(x.get(index) < (plt.x + plt.width) && (x.get(index) + 30) > plt.x && 
		   (y.get(index) + 37) < (plt.y + plt.height) && (y.get(index) + 35) > plt.y) {
			// but we only care about platforms when falling
	    	if (phy.getVelocity().y > 0) {
	    		// for bouncing
	    		phy.setVelocityY(0);
	    		ui.onPlatform = true;
	    		break;
	    	}
	    }
	}
}