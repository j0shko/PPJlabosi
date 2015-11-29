import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
	
	public List<State> getStates() {
		return states;
	}
	
	private void createFromNKA(NKA nkaAutomat) {		
		NKA.State initial = nkaAutomat.getInitialState();
		
		Set<NKA.State> currentNKAStates = new HashSet<>();
		Set<NKA.State> initialSet = new HashSet<>();
		initialSet.add(initial);
		currentNKAStates.addAll(getEpsilonClosure(initialSet));
		
		List<LLProduction> productions = new ArrayList<>();
		for (NKA.State state : currentNKAStates) {
			productions.add(state.getProduction());
		}
		
		initialState = new State(productions);
		states.add(initialState);
		
		Deque<State> stateStack = new ArrayDeque<>();
		Deque<Set<NKA.State>> nkaStateStack = new ArrayDeque<>();
		
		stateStack.push(initialState);
		nkaStateStack.push(currentNKAStates);
		
		while (!stateStack.isEmpty()) {
			currentNKAStates = nkaStateStack.pop();
			State currentState = stateStack.pop();
			
			Set<Sign> transitionSigns = getTransitionsForNKAStates(currentNKAStates);
			for (Sign sign : transitionSigns) {
				Set<NKA.State> newStates = getTransitionedStates(sign, currentNKAStates);
				newStates.addAll(getEpsilonClosure(newStates));
				
				List<LLProduction> newProductions = new ArrayList<>();
				for (NKA.State state : newStates) {
					newProductions.add(state.getProduction());
				}
				
				State sameState = null;
				for (State state : states) {
					if (state.productions.containsAll(newProductions) && state.productions.size() == newProductions.size()) {
						sameState = state;
						break;
					}
				}
				if (sameState != null) {
					currentState.addTransit(sign, sameState);
				} else {
					State newState = new State(newProductions);
					states.add(newState);
					currentState.addTransit(sign, newState);
					stateStack.push(newState);
					nkaStateStack.push(newStates);
				}
			}
		}
	}
	
	private Set<NKA.State> getEpsilonClosure(Set<NKA.State> states) {
		Set<NKA.State> closure = new HashSet<>();
		closure.addAll(states);
		
		Set<NKA.State> uncheckedEpsilons = new HashSet<>();
		for (NKA.State state : states) {
			uncheckedEpsilons.addAll(state.getAllEpsilonTransits());
		}
		
		Set<NKA.State> newUnchecked = new HashSet<>();
		do {
			
			for (NKA.State state : uncheckedEpsilons) {
				closure.add(state);
				for (NKA.State epsilonState : state.getAllEpsilonTransits()) {
					if (!closure.contains(epsilonState)) {
						newUnchecked.add(epsilonState);
					}
				}
			}		
			uncheckedEpsilons.clear();
			uncheckedEpsilons.addAll(newUnchecked);
			newUnchecked.clear();
		} while (!uncheckedEpsilons.isEmpty());
		
		return closure;
	}
	
	private Set<Sign> getTransitionsForNKAStates(Set<NKA.State> states) {
		Set<Sign> transitionsSigns = new HashSet<>();
		
		for (NKA.State state : states) {
			transitionsSigns.addAll(state.getAllTransits().keySet());
		}
		
		return transitionsSigns;
	}
	
	private Set<NKA.State> getTransitionedStates(Sign sign, Set<NKA.State> states) {
		Set<NKA.State> newStates = new HashSet<>();
		
		for (NKA.State state : states) {
			NKA.State transitState = state.getTransit(sign);
			if (transitState != null) {
				newStates.add(transitState);
			}
		}
		
		return newStates;
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
