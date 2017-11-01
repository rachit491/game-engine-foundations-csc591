import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eventManagementSystem.Event;
import eventManagementSystem.EventHandler;
import eventManagementSystem.EventManager;
import eventManagementSystem.EventType;
import scripting.ScriptManager;

public class CollisionDetection implements IComponent, Serializable, EventHandler {
	
	private EventManager em;
	private Physics phy;
	private UserInputs ui;
	
	public CollisionDetection(EventManager em, Physics phy, UserInputs ui) {
		this.em = em;
		this.phy = phy;
		this.ui = ui;
		em.addDispatcher(EventType.COLLISION_EVENT, this);
	}
	
	@Override
	public void onEvent(Event e) {
		//handling through Scripting
		ScriptManager.loadScript(System.getProperty("user.dir") + "/src/scripting/collisionEventHandler.js");
		ScriptManager.bindArgument("phy", this.phy);
		ScriptManager.bindArgument("ui", this.ui);
		ScriptManager.executeScript("checkCollision", e);
	}
	
	@Override
	public GameObject getObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Components getID() {
		// TODO Auto-generated method stub
		return Components.CollisionDetection;
	}

}
