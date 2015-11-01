import java.util.ArrayList;
import java.util.List;

public class Rule {

	private String regex;
	private List<Action> actions;
	
	private AutomatRegex regexAut;
	
	public Rule(String regex) {
		this.regex = regex;
		regexAut = new AutomatRegex(regex);
		actions = new ArrayList<>();
	}
	
	public boolean accepts(String string) {
		return regexAut.accepts(string);
	}
	
	public Rule addAction(Action action) {
		actions.add(action);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actions == null) ? 0 : actions.hashCode());
		result = prime * result + ((regex == null) ? 0 : regex.hashCode());
		return result;
	}	
	
}

