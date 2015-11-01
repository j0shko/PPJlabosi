import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class LAAutomat implements Serializable {

	private static final long serialVersionUID = 322L;

	private Set<State> states = new HashSet<>();
	private State currentState;
	
	private State initialState;
	
	public LAAutomat(State initialState) {
		states.add(initialState);
		this.initialState = initialState;
	}
	
	public void resetAutomat() {
		currentState = initialState;
	}
	
	public void addState(State state) {
		states.add(state);
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public void setCurrentState(State state) {
		currentState = state;
	}
}
