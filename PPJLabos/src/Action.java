
public class Action {

	private String name;
	
	public Action(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
