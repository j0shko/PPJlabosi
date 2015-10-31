
public class EnterStateAction extends Action {

	private State state;
	
	public EnterStateAction(State state) {
		super("UDJI_U_STANJE");
		this.state = state;
	}
	
	public State getState() {
		return state;
	}

}
