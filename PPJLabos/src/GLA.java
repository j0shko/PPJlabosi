import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GLA {

	public static String resolveRegex(Map<String, String> regexes, String regDef) {
		String ans = new String(regDef);
		for (String def : regexes.keySet()) {
			if (ans.contains(def)) {
				ans.replace(def, "(" + regexes.get(def));
			}
		}
		return ans;
	}
	
	public static void main(String[] args) throws IOException {
		Map<String, String> regexs = new HashMap<>();
		Map<String, State> states = new HashMap<>();
		Map<String, Unit> lexUnits = new HashMap<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		// read regular definitions
		
		String current = br.readLine();
		while(!current.startsWith("%X")) {
			String[] line = current.split("\\s");
			for (String regexName : regexs.keySet()) {
				if (line[1].contains(regexName)) {
					line[1] = line[1].replace(regexName, "("+regexs.get(regexName)+")");
				}
			}
			regexs.put(line[0], line[1]);
			current = br.readLine();
		}
	
		// read LA states
		
		if (current.startsWith("%X")) {
			String[] line = current.split("\\s");
			for (int i = 1; i < line.length; ++i) {
				states.put(line[i], new State(line[i]));
			}
		}
		
		current = br.readLine();
		
		// read lexical units
		
		if (current.startsWith("%L")) {
			String[] line = current.split("\\s");
			for (int i = 1; i < line.length; ++i) {
				lexUnits.put(line[i], new Unit(line[i]));
			}
		}
		
		// read LA rule
		Rule currentRule = null;
		while ((current = br.readLine()) != null) {
			if (current.startsWith("<")) {
				String stateName = current.split("<|>")[1];
				String regex = resolveRegex(regexs, current.substring(stateName.length() + 2));
				State currentState = states.get(stateName);
				currentRule = currentState.addRule(regex);
			} else if (current.startsWith("{")) {
				continue;
			} else if (current.startsWith("}")) {
				currentRule = null;
			} else if (currentRule != null) {
				if (current.startsWith("UDJI_U_STANJE ")) {
					String line[] = current.split("\\s");
					currentRule.addAction(new EnterStateAction(states.get(line[1])));
				} else if (current.startsWith("VRATI_SE ")) {
					String line[] = current.split("\\s");
					currentRule.addAction(new ReturnAction(Integer.parseInt(line[1])));
				} else if (current.equals("NOVI_REDAK")) {
					currentRule.addAction(new NewLineAction());
				} else if (current.equals("-")) {
					// TODO: Odbaci
				} else {
					if (lexUnits.containsKey(current)) {
						currentRule.addAction(lexUnits.get(current));
					}
				}
			}
		}
		System.out.println("test");
	}
}
