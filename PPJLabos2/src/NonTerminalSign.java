import java.util.ArrayList;
import java.util.List;


public class NonTerminalSign extends Sign {

	public static class GrammarLine {
		
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

		public int getPriority() {
			return priority;
		}
		
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
			return "#" + priority + " " + line.toString();
		}
	}
	
	private List<GrammarLine> grammar;
	
	public NonTerminalSign(String name) {
		super(name);
		grammar = new ArrayList<>();
	}

	public void addGrammarLine(List<Sign> signs, int priority) {
		if (signs != null) {
			grammar.add(new GrammarLine(signs, priority));
		}
	}
	
	public List<GrammarLine> getGrammar() {
		return grammar;
	}
}
