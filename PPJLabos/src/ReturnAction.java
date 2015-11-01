import java.io.Serializable;

public class ReturnAction extends Action implements Serializable {

	private static final long serialVersionUID = 220L;
	private int index;
	
	public ReturnAction(int index) {
		super("VRATI_SE");
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
