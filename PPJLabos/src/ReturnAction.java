
public class ReturnAction extends Action {

	private int index;
	
	public ReturnAction(int index) {
		super("VRATI_SE");
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
