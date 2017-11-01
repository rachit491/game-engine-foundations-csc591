function update() {
	var box = platform.get(6).getBox();
	//specifying random color to moving platform on every update
	box.setColor(factory.getRandomInt(256));
}