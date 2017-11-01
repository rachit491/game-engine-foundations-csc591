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
	private ArrayList<GameObject> platform;
	private int index;
	private ArrayList<Integer> x;
	private ArrayList<Integer> y;
	
	public CollisionDetection(EventManager em, Physics phy, UserInputs ui) {
		this.em = em;
		this.phy = phy;
		this.ui = ui;
		em.addDispatcher(EventType.COLLISION_EVENT, this);
	}
	
	@Override
	public void onEvent(Event e) {
		/*if(this.replay.getIsReplaying()) {
		System.out.println("Replay-Collision");
	}
	else {*/
		// standard rectangle intersections, but only for our lowest quarter
		ArrayList<Object> args = e.getEventArgs();
		x = (ArrayList<Integer>) args.get(0);
		y = (ArrayList<Integer>) args.get(1);
		index = (int) args.get(2);
		platform = (ArrayList<GameObject>) args.get(3);
		
		for(int i = 0; i < platform.size(); i++) {
			Rectangle plt = platform.get(i).getBox().getRectangle();
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
		//System.out.println("COLLISION_EVENT");
	//}
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
