import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rule extends AutomatRegex implements Serializable {

	private static final long serialVersionUID = 4055L;
	private String regex;
	private List<Action> actions;
	
	//private AutomatRegex regexAut;
	
	private int index;
	
	public Rule(String regex, int index) {
		super(regex);
		this.regex = regex;
		this.index = index;
		//regexAut = new AutomatRegex(regex);
		actions = new ArrayList<>();
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean accepts(String string) {
		return super.accepts(string);
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

	public List<Action> getActions() {
		return actions;
	}	
}