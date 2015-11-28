import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DKA {

	public static class State {
		private String name;
		
		private List<LLProduction> productions = new ArrayList<>();
		
		private Map<Sign, State> transits;

		public State(List<LLProduction> productions) {
			this.productions = productions;
			this.transits = new HashMap<>();
			
			StringBuilder name = new StringBuilder();
			
			for (LLProduction production : productions) {
				name.append(production).append("\n");
			}
			name.deleteCharAt(name.length() - 1);
			
			this.name = name.toString();
		}
		
		public void addProduction(LLProduction production) {
			productions.add(production);
		}
		
		public List<LLProduction> getProductions() {
			return productions;
		}
		
		public void addTransit(Sign sign, State state) {
			transits.put(sign, state);
		}
		
		public State getTransit(Sign sign) {
			return transits.get(sign);
		}
		
		public String getName() {
			return name;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((productions == null) ? 0 : productions.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			State other = (State) obj;
			if (productions == null) {
				if (other.productions != null)
					return false;
			} else if (!productions.equals(other.productions))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return name;
		}
	}
	
	private State initialState;
	private List<State> states;
	private State currentState;
	
	public DKA(NKA nkaAutomat) {
		states = new ArrayList<>();
		
		createFromNKA(nkaAutomat);
	}
	
	public void addState(State state) {
		states.add(state);
	}
	
	public State getInitialState() {
		return initialState;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	private void createFromNKA(NKA nkaAutomat) {
		Set<NKA.State> checkedStates = new HashSet<>();
		
		for (NKA.State state : nkaAutomat.getStates()) {
			if (!checkedStates.contains(state)) {
				List<LLProduction> productions = new ArrayList<>();
				
				checkedStates.add(state);
				productions.add(state.getProduction());
				
				Set<NKA.State> uncheckedEpsilons = new HashSet<>();
				uncheckedEpsilons.addAll(state.getAllEpsilonTransits());
				
				while (!uncheckedEpsilons.isEmpty()) {
					
					Set<NKA.State> newUnchecked = new HashSet<>();
					for (NKA.State epsilonTransitState : uncheckedEpsilons) {
						productions.add(epsilonTransitState.getProduction());
						checkedStates.add(epsilonTransitState);
						
						newUnchecked.addAll(epsilonTransitState.getAllEpsilonTransits());
					}
					
					uncheckedEpsilons = newUnchecked;
				}
				
				
				states.add(new State(productions));
			}
		}
		
		for (State state : states) {
			for (LLProduction production : state.productions) {
				NKA.State nkaState = nkaAutomat.getStateForProduction(production);
				Map<Sign, NKA.State> nkaTransits = nkaState.getAllTransits();
				
				for (Sign sign : nkaTransits.keySet()) {
					LLProduction resultProduction = nkaTransits.get(sign).getProduction();
					State resultState = getStateForProduction(resultProduction);
					state.addTransit(sign, resultState);
				}
			}
		}
		
		initialState = states.get(0);
		currentState = initialState;
	}
	
	public State getStateForProduction(LLProduction production) {
		for (State state : states) {
			for (LLProduction stateProduction : state.getProductions()) {
				if (stateProduction.equals(production)) {
					return state;
				}
			}
		}
		return null;
	}
}
