import java.util.HashMap;
import java.util.Map;

public class State {

	private String name;
	
	private Map<String, Rule> rules;
	
	public State(String name){
		this.name = name;
		rules = new HashMap<>();
	}
	
	public Rule addRule(String regex) {
		Rule newRule = new Rule(regex);
		rules.put(regex, newRule);
		return newRule;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
