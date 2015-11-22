import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Automat {

	public static class State {
		
		private String name;
		
		//unused, maybe important later
//		private NonTerminalSign leftSide;
//		private List<Sign> rightSide;
//		private List<TerminalSign> nextSign;
		
		private Map<Sign, State> transits;
		private List<State> epsilonTransits;
			
		public State() {
			this("");
		}
		
		public State(String name) {
			this.name = name;
			transits = new HashMap<>();
			epsilonTransits = new ArrayList<>();
		}
		
		public void addTransit(Sign sign, State state) {
			transits.put(sign, state);
		}
		
		public State getTransit(Sign sign) {
			return transits.get(sign);
		}
		
		public void addEpsilonTransit(State state) {
			epsilonTransits.add(state);
		}
		
		public List<State> getAllEpsilonTransits() {
			return epsilonTransits;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	private State initialState;
	private List<State> states;
	private State currentState;
	
	public Automat(NonTerminalSign initialNTSign) {
		states = new ArrayList<>();
		initialState = create(0, initialNTSign, 0);
	}
	
	public boolean isDKA() {
		for (State state: states) {
			if (!state.getAllEpsilonTransits().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public State getInitialState() {
		return initialState;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public void transformToDKA() {
		//TODO napravi sve ovo fun
	}
	
	private State create(int pointer, NonTerminalSign current, int grammarLine) {
		// RADI ALI NE; MALO VISKI MALO STACK OVERFLOW
		State createdState = new State();
		StringBuilder name = new StringBuilder(current.toString());
		
		NonTerminalSign.GrammarLine currentLine = current.getGrammar().get(grammarLine);
				
		if (pointer == currentLine.length()) {
			name.append("->").append(currentLine.toStringWithDotAt(pointer));
			createdState.setName(name.toString());
			states.add(createdState);
			return createdState;
		} else {
			Sign currentSign = currentLine.getSignAt(pointer);
			
			if (currentSign instanceof NonTerminalSign) {
				NonTerminalSign currentNTSign = (NonTerminalSign) currentSign;
				for (int i = 0; i < currentNTSign.getGrammar().size(); i++) {
					State newState = create(0, currentNTSign, i);
					createdState.addEpsilonTransit(newState);
				}
			} else if(currentSign.equals(TerminalSign.EPSILON)) {
				name.append("->*");
				createdState.setName(name.toString());
				states.add(createdState);
				return createdState;
			}
			
			State newState = create(pointer + 1, current, grammarLine);
			createdState.addTransit(currentSign, newState);
		}
		
		name.append("->").append(currentLine.toStringWithDotAt(pointer));
		createdState.setName(name.toString());
		states.add(createdState);
		return createdState;
	}
}
