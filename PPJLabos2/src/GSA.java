import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GSA {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Map<String, NonTerminalSign> nonTerminalSigns = new HashMap<>();
		Map<String, TerminalSign> terminalSigns = new HashMap<>();
		
		NonTerminalSign initial = null;
		
		terminalSigns.put(TerminalSign.EPSILON.getName(), TerminalSign.EPSILON);
		terminalSigns.put(TerminalSign.END.getName(), TerminalSign.END);
		
		//read nonterminal signs
		String line = br.readLine();
		if (line.startsWith("%V")) {
			String[] current = line.split("\\s");
			for (int i = 1; i < current.length; i++) {
				nonTerminalSigns.put(current[i], new NonTerminalSign(current[i]));
				if (i == 1) {
					initial = nonTerminalSigns.get(current[i]);
				}
			}
		}
		
		//read terminal signs
		line = br.readLine();
		if (line.startsWith("%T")) {
			String[] current = line.split("\\s");
			for (int i = 1 ; i < current.length; i++) {
				terminalSigns.put(current[i], new TerminalSign(current[i]));
			}
		}
		
		//read synchronization signs
		line = br.readLine();
		if (line.startsWith("%Syn")) {
			String[] current = line.split("\\s");
			for (int i = 1 ; i < current.length; i++) {
				terminalSigns.get(current[i]).setSynchronization();
			}
		}
		
		// reading grammar
		
		int priority = 1;
		
		line = br.readLine();
		NonTerminalSign current = null;
		while (line != null) {
			if (!line.startsWith(" ")) {
				current = nonTerminalSigns.get(line.trim());
			} else {
				List<Sign> currentRule = new ArrayList<>();
				String[] signs = line.split("\\s");
				for (int i = 1; i < signs.length; i++) {
					if (terminalSigns.containsKey(signs[i])) {
						currentRule.add(terminalSigns.get(signs[i]));
					} else {
						currentRule.add(nonTerminalSigns.get(signs[i]));
					}
					
				}
				current.addGrammarLine(currentRule, priority);
				priority++;
			}
			line = br.readLine();
		}
		priority++;
		
		// adding new nonterminal sign
		NonTerminalSign newInitial = new NonTerminalSign("<S\'>");
		List<Sign> newInitialLine = new ArrayList<>();
		newInitialLine.add(initial);
		newInitial.addGrammarLine(newInitialLine, priority);
		nonTerminalSigns.put(newInitial.getName(), newInitial);
		
		for (NonTerminalSign sign : nonTerminalSigns.values()) {
			sign.getStartsDirectlyWith();
		}
		
		for (NonTerminalSign sign : nonTerminalSigns.values()) {
			sign.getStartsWith();
		}
		 
		NKA nkAutomat = new NKA(newInitial);
		
		DKA dkaAutomat = new DKA(nkAutomat);
		System.out.println("burek");
	}
}
