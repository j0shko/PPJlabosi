import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NKA {
	
	public static final TerminalSign dot = new TerminalSign("*");

	public static class State {
		
		private String name;
		
		private LLProduction production;
		
		private Map<Sign, State> transits;
		private List<State> epsilonTransits;
		
		public State(LLProduction production){
			this.production = production;
			transits = new HashMap<>();
			epsilonTransits = new ArrayList<>();
			
			name = production.toString();
		}
		
		public void addTransit(Sign sign, State state) {
			transits.put(sign, state);
		}
		
		public State getTransit(Sign sign) {
			return transits.get(sign);
		}
		
		public Map<Sign, State> getAllTransits() {
			return transits;
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
		
		public LLProduction getProduction() {
			return production;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((production == null) ? 0 : production.hashCode());
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
			if (production == null) {
				if (other.production != null)
					return false;
			} else if (!production.equals(other.production))
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
	
	public NKA(NonTerminalSign initialNTSign) {
		states = new ArrayList<>();
		Set<TerminalSign> initialNextSigns = new HashSet<>();
		initialNextSigns.add(TerminalSign.END);
		initialState = create(initialNTSign, 0, 0, initialNextSigns);
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
	
	public State getStateForProduction(LLProduction production) {
		for (State state : states) {
			if (state.getProduction().equals(production)) {
				return state;
			}
		}
		
		return null;
	}
	
	private State create(NonTerminalSign currentNTS, int pointer, int grammarLineNum, Set<TerminalSign> nextSigns) {
		NonTerminalSign.GrammarLine currentLine = currentNTS.getGrammar().get(grammarLineNum);
		List<Sign> line = new ArrayList<>(currentLine.getLine());
		
		Sign current = null;
		boolean isEpsilon = false;
		boolean isNTS = false;
		boolean isEnd = false;
		
		if (pointer == line.size()) {
			isEnd = true;
			line.add(dot);
		} else {
			current = line.get(pointer);
			if (current instanceof TerminalSign && ((TerminalSign) current).equals(TerminalSign.EPSILON)) {
				isEpsilon = true;
				line = new ArrayList<>();
				line.add(dot);
			} else {
				if (current instanceof NonTerminalSign) {
					isNTS = true;
				}
				line.add(pointer, dot);
			}
		}
		
		State newState = new State(new LLProduction(currentNTS, line, nextSigns));
		states.add(newState);
		
		if (!isEnd && !isEpsilon) {
			if(isNTS) {
				List<Sign> sufix = new ArrayList<>();
				for (int i = pointer + 2, end = line.size(); i < end ; i++) {
					sufix.add(line.get(i));
				}
				
				Set<TerminalSign> newNextSigns = NonTerminalSign.getStartsWithForLineSufix(sufix);
				if (newNextSigns.isEmpty()) {
					newNextSigns = nextSigns;
				}
				
				NonTerminalSign pointedNTS = (NonTerminalSign) current;
				List<NonTerminalSign.GrammarLine> pointedNTSgrammar = pointedNTS.getGrammar();
				for(int i = 0, end = pointedNTSgrammar.size(); i < end; i++) {
					List<Sign> tempLine = new ArrayList<>(pointedNTSgrammar.get(i).getLine());
					if (tempLine.get(0).equals(TerminalSign.EPSILON)) {
						tempLine.clear();
					}
					tempLine.add(0, dot);
					
					State tempState = new State(new LLProduction(pointedNTS, tempLine, newNextSigns));
					int indexOfFoundState = states.indexOf(tempState);
					if (indexOfFoundState >= 0) {
						State foundState = states.get(indexOfFoundState);
						newState.addEpsilonTransit(foundState);
					} else {
						State createdState = create(pointedNTS, 0, i, newNextSigns);
						newState.addEpsilonTransit(createdState);
					}
				}
			}
			
			State nextState = create(currentNTS, pointer + 1, grammarLineNum, nextSigns);
			newState.addTransit(current, nextState);
		}
		
		return newState;
	}
}
