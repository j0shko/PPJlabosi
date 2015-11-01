import java.io.Serializable;

public class Action implements Serializable {

	private static final long serialVersionUID = 42L;
	private String name;
	
	public Action(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public String getName() {
		return name;
	}
}
