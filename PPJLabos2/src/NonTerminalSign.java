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

		public int getPriority() {
			return priority;
		}
		
		@Override
		public String toString() {
			return "#" + priority + " " + line.toString();
		}
	}
	
	List<GrammarLine> grammar;
	
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
