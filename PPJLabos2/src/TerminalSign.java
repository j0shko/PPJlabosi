import java.io.Serializable;


public class TerminalSign extends Sign implements Serializable{

	private static final long serialVersionUID = 635480218359362483L;
	public static final TerminalSign EPSILON = new TerminalSign("$");
	public static final TerminalSign END = new TerminalSign("$END$");
	
	private boolean isSyn;
	
	public TerminalSign(String name) {
		super(name);
		isSyn = false;
	}

	public boolean isSynchronSign() {
		return isSyn;
	}

	public void setSynchronization() {
		isSyn = true;
	}
}
