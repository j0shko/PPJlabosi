import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class State implements Serializable {

	private static final long serialVersionUID = 365L;
	private String name;
	
	private List<Rule> rules;
	
	public State(String name){
		this.name = name;
		rules = new ArrayList<>();
	}
	
	public Rule addRule(String regex) {
		Rule newRule = new Rule(regex);
		rules.add(newRule);
		return newRule;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public List<Rule> getRules() {
		return rules;
	}
}
