import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NonTerminalSign extends Sign {

	public class GrammarLine {
		
		private List<Sign> line;
		private int priority;
		
		public GrammarLine(List<Sign> line, int priority) {
			this.line = line;
			this.priority = priority;
		}

		public List<Sign> getLine() {
			return line;
		}
		
		public int length() {
			return line.size();
		}
		
		public Sign getSignAt(int position) {
			return line.get(position);
		}
		
		public boolean isEmptyLine() {
			return line.get(0).equals(TerminalSign.EPSILON);
		}

		public int getPriority() {
			return priority;
		}
		
		//TODO maybe not needed
		public String toStringWithDotAt(int index) {
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < line.size(); i++) {
				if (index == i) {
					result.append("*");
				}
				result.append(line.get(i).toString());
			}
			if (index == line.size()) {
				result.append("*");
			}
			return result.toString();
		}
		
		@Override
		public String toString() {
			String rightSide = "";
			for (Sign sign : line) {
				rightSide += sign + " ";
			}
			
			return "#" + priority + ":" + getName() + " -> " + rightSide;
		}
	}
	
	private List<GrammarLine> grammar;
	private boolean empty;
	
	private Set<Sign> startsDirectlyWith;
	private Set<TerminalSign> startsWith;
	
	public NonTerminalSign(String name) {
		super(name);
		grammar = new ArrayList<>();
		empty = false;
	}
	
	public boolean isEmpty() {
		return empty;
	}

	public void addGrammarLine(List<Sign> signs, int priority) {
		if (signs != null) {
			if (signs.get(0).equals(TerminalSign.EPSILON)) {
				empty = true;
			} else {
				boolean isEmpty = true;
				for (Sign sign : signs) {
					//stop when sign is non empty or terminal
					if (sign instanceof TerminalSign || !((NonTerminalSign) sign).isEmpty()) {
						isEmpty = empty;
						break;
					}
				}
				empty = isEmpty;
			}
			grammar.add(new GrammarLine(signs, priority));
		}
	}
	
	public List<GrammarLine> getGrammar() {
		return grammar;
	}
	
	public Set<Sign> getStartsDirectlyWith() {
		if (startsDirectlyWith == null) {
			startsDirectlyWith = computeStartsDirectlyWith();
		}
		
		return startsDirectlyWith;
	} 
	
	private Set<Sign> computeStartsDirectlyWith() {
		Set<Sign> startsDirectlyWith = new HashSet<>();
		for (GrammarLine line : grammar) {
			List<Sign> lineSigns = line.getLine();
			for (Sign sign : lineSigns) {
				if (!sign.equals(TerminalSign.EPSILON)) {
					startsDirectlyWith.add(sign);
				}
				//stop when sign is non empty or terminal
				if (sign instanceof TerminalSign || !((NonTerminalSign) sign).isEmpty()) {
					break;
				}
			}
		}
		return startsDirectlyWith;
	}
	
	public Set<TerminalSign> getStartsWith() {
		if (startsWith == null) {
			startsWith = computeStartsWith();
		}
		
		return startsWith;
	}

	private Set<TerminalSign> computeStartsWith() {
		Set<TerminalSign> startsWith = new HashSet<>();
		Set<NonTerminalSign> uncheckedSigns = new HashSet<>();
		//Set<NonTerminalSign> checkedSigns = new HashSet<>();
		
		Set<Sign> startsDirectlyWith = getStartsDirectlyWith();
		
		for (Sign sign : startsDirectlyWith) {
			if (sign instanceof NonTerminalSign) {
				uncheckedSigns.add((NonTerminalSign) sign);
			} else {
				startsWith.add((TerminalSign) sign);
			}
		}
		
		while (!uncheckedSigns.isEmpty()) {
			Set<NonTerminalSign> toDelete = new HashSet<>();
			Set<NonTerminalSign> toAdd = new HashSet<>();
			
 			for (NonTerminalSign current : uncheckedSigns) {
				Set<Sign> currentStartsDirectly = current.getStartsDirectlyWith();
				
				for (Sign sign : currentStartsDirectly) {
					if (sign instanceof NonTerminalSign) {
						toAdd.add((NonTerminalSign) sign);
					} else {
						startsWith.add((TerminalSign) sign);
					}
				}
				toDelete.add(current);
			}
 			
 			uncheckedSigns.removeAll(toDelete);
 			uncheckedSigns.addAll(toAdd);
		}
		
		return startsWith;
	}
	
	public static Set<TerminalSign> getStartsWithForLineSufix(List<Sign> sufix) {
		Set<TerminalSign> result = new HashSet<>();
		if (sufix.isEmpty()) {
			return result;
		}
		
		boolean allEmpty = true;
		
		for (Sign sign : sufix) {
			if (sign instanceof TerminalSign) {
				result.add((TerminalSign) sign);
				allEmpty = false;
				break;
			} else {
				NonTerminalSign ntsign = (NonTerminalSign) sign;
				result.addAll(ntsign.getStartsWith());
				if (!ntsign.isEmpty()) {
					allEmpty = false;
					break;
				}
			}
		}
		
		if (allEmpty) {
			result.add(TerminalSign.END);
		}
		
		return result;
	}
}
