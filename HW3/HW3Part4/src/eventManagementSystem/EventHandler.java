package eventManagementSystem;

import java.io.Serializable;

public interface EventHandler extends Serializable {
	public void onEvent(Event e);
}
