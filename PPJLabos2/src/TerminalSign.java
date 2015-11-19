
public class TerminalSign extends Sign {

	boolean isSyn;
	
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
