import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class AutomatRegex {

	public static class State {
		private Set<State> epsilonTransits = new HashSet<>();
		private Map<Character, List<State>> transits = new HashMap<>();

		public void addEpsilonTransit(State state) {
			epsilonTransits.add(state);
		}

		public void addTransit(char c, State state) {
			if (transits.containsKey(c)) {
				List<State> current = transits.get(c);
				current.add(state);
				transits.put(c, current);
			} else {
				List<State> newList = new ArrayList<>();
				newList.add(state);
				transits.put(c, newList);
			}
		}
		
		public List<State> getTransit(char c) {
			return transits.get(c);
		}
		
		public Set<State> getEpsilonTransits() {
			return epsilonTransits;
		}
	}

	private int stateCount;
	private State initialState;
	private State acceptableState;
	private Set<State> states = new HashSet<>();

	public AutomatRegex(String regex) {
		stateCount = 0;
		State[] result = create(regex);
		this.initialState = result[0];
		this.acceptableState = result[1];
	}
	
	public int getStateCount() {
		return stateCount;
	}
	
	private State[] create(String regex) {
		int bracketCount = 0;
		boolean hasChoice = false;
		List<String> choices = new ArrayList<>();

		int nextChoiceIndex = 0;
		for (int i = 0; i < regex.length(); i++) {
			if (regex.charAt(i) == '(' && isOperator(regex, i)) {
				bracketCount++;
			} else if (regex.charAt(i) == ')' && isOperator(regex, i)) {
				bracketCount--;
			} else if (bracketCount == 0 && regex.charAt(i) == '|'
					&& isOperator(regex, i)) {
				choices.add(regex.substring(nextChoiceIndex, i));
				hasChoice = true;
				nextChoiceIndex = i + 1;
			}
		}

		if (hasChoice) {
			choices.add(regex.substring(nextChoiceIndex));
		}

		State leftState = newState();
		State rightState = newState();

		if (hasChoice) {
			for (int i = 0; i < choices.size(); i++) {
				State[] temp = create(choices.get(i));
				leftState.addEpsilonTransit(temp[0]);
				temp[1].addEpsilonTransit(rightState);
			}
		} else {
			boolean prefixed = false;
			State lastState = leftState;
			for (int i = 0; i < regex.length(); i++) {
				char currentChar = regex.charAt(i);
				State a, b;
				if (prefixed) {
					// slucaj 1
					prefixed = false;
					char c;
					switch (currentChar) {
					case 't':
						c = '\t';
						break;
					case 'n':
						c = 'n';
						break;
					case '_':
						c = ' ';
						break;
					default:
						c = currentChar;
					}

					a = newState();
					b = newState();
					a.addTransit(c, b);
				} else {
					// slucaj 2
					if (currentChar == '\\') {
						prefixed = true;
						continue;
					}
					if (currentChar != '(') {
						a = newState();
						b = newState();

						if (currentChar == '$') {
							a.addEpsilonTransit(b);
						} else {
							a.addTransit(currentChar, b);
						}
					} else {
						int j = getIndexOfClosingBracket(regex, i+1);
						State[] temp = create(regex.substring(i+1, j));
						a = temp[0];
						b = temp[1];
						i = j;
					}
				}
				
				if (i+1 < regex.length() && regex.charAt(i+1) == '*') {
					State x = a;
					State y = b;
					a = newState();
					b = newState();
					a.addEpsilonTransit(x);
					y.addEpsilonTransit(b);
					a.addEpsilonTransit(b);
					y.addEpsilonTransit(x);
					i++;
				}
				
				lastState.addEpsilonTransit(a);
				lastState = b;
			}
			lastState.addEpsilonTransit(rightState);
		}

		return new State[] { leftState, rightState };
	}

	public boolean accepts(String string) {
		Set<State> currentStates = new HashSet<>();
		currentStates.add(initialState);
		addAllEpsilonNeighbours(currentStates);
		
		
		for (int i = 0; i < string.length(); i++) {
			Set<State> newCurrentStates = new HashSet<>();
			for (State state : currentStates) {
				List<State> transits = state.getTransit(string.charAt(i));
				if (transits != null) {
					newCurrentStates.addAll(transits);
				}
			}
			currentStates = newCurrentStates;
			addAllEpsilonNeighbours(currentStates);
		}
		
		return currentStates.contains(acceptableState);
	}
	
	private Set<State> addAllEpsilonNeighbours(Set<State> states) {
		Set<State> checked = new HashSet<>();
		boolean change;
		do {
			change = false;
			Set<State> newStates = new HashSet<>(); 
			for (State current : states) {
				if (!checked.contains(current)) {
					checked.add(current);
					newStates.addAll(current.getEpsilonTransits());
					change = true;
				}
			}
			states.addAll(newStates);
		} while(change);
		return states;
	}
	
	private static int getIndexOfClosingBracket(String s, int i) {
		int bracketCount = 0;
		char current = s.charAt(i);
		while (current != ')' || bracketCount != 0) {
			if (current == '(') {
				bracketCount++;
			} else if (current == ')') {
				bracketCount--;
			}
			i++;
			current = s.charAt(i);
		}
		return i;
	}

	private State newState() {
		State newState = new State();
		stateCount++;
		states.add(newState);
		return newState;
	}

	private static boolean isOperator(String string, int index) {
		int num = 0;
		while (index - 1 >= 0 && string.charAt(index - 1) == '\\') {
			num++;
			index--;
		}
		return num % 2 == 0;
	}
}
