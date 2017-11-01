import java.io.Serializable;

public interface IComponent extends Serializable {
	public GameObject getObject();
	
	public Components getID();
}
