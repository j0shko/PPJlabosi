import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Automat {
	
	public static final TerminalSign dot = new TerminalSign("*");

	public static class State {
		
		private String name;
		
		private NonTerminalSign leftSide;
		private List<Sign> rightSide;
		private Set<TerminalSign> nextSigns;
		
		private Map<Sign, State> transits;
		private List<State> epsilonTransits;
		
		public State(NonTerminalSign leftSide, List<Sign> rightSide, Set<TerminalSign> nextSigns) {
			this.leftSide = leftSide;
			this.rightSide = rightSide;
			this.nextSigns = nextSigns;
			transits = new HashMap<>();
			epsilonTransits = new ArrayList<>();
			
			name = leftSide + " -> ";
			for (Sign sign : rightSide) {
				name += sign + " ";
			}
			name += "{";
			for (TerminalSign sign : nextSigns) {
				name += sign + " ";
			} 
			name += "}";
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
		
		public NonTerminalSign getLeftSide() {
			return leftSide;
		}

		public List<Sign> getRightSide() {
			return rightSide;
		}

		public Set<TerminalSign> getNextSigns() {
			return nextSigns;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((leftSide == null) ? 0 : leftSide.hashCode());
			result = prime * result
					+ ((nextSigns == null) ? 0 : nextSigns.hashCode());
			result = prime * result
					+ ((rightSide == null) ? 0 : rightSide.hashCode());
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
			if (leftSide == null) {
				if (other.leftSide != null)
					return false;
			} else if (!leftSide.equals(other.leftSide))
				return false;
			if (nextSigns == null) {
				if (other.nextSigns != null)
					return false;
			} else if (!nextSigns.equals(other.nextSigns))
				return false;
			if (rightSide == null) {
				if (other.rightSide != null)
					return false;
			} else if (!rightSide.equals(other.rightSide))
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
	
	public Automat(NonTerminalSign initialNTSign) {
		states = new ArrayList<>();
		Set<TerminalSign> initialNextSigns = new HashSet<>();
		initialNextSigns.add(TerminalSign.END);
		initialState = create(initialNTSign, 0, 0, initialNextSigns);
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
	
	/*
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
	*/
	
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
		
		State newState = new State(currentNTS, line, nextSigns);
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
					
					State tempState = new State(pointedNTS, tempLine, newNextSigns);
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
