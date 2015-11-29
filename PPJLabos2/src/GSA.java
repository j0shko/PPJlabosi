import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
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
		 
		//System.err.println("Creating NKA...");
		NKA nkAutomat = new NKA(newInitial);
		//System.err.println("NKA created");
		//System.err.println("NKA has " + nkAutomat.getStates().size()+ " states.");

		//System.err.println("Creating DKA...");
		DKA dkaAutomat = new DKA(nkAutomat);
		//System.err.println("DKA created");
		//System.err.println("DKA has " + dkaAutomat.getStates().size()+ " states.");
		
		//System.err.println("Creating parser...");
		Parser parser = new Parser(dkaAutomat, newInitial);
		//System.err.println("Parser created...");
		
		/*
		parser.inputLine(terminalSigns.get("a"), 1, "x x x");
		parser.inputLine(terminalSigns.get("b"), 2, "y y");
		parser.inputLine(terminalSigns.get("a"), 3, "xx xx");
		parser.inputLine(terminalSigns.get("a"), 4, "xx xx xx");
		parser.inputLine(terminalSigns.get("b"), 4, "y");

		boolean result = parser.parse();
		System.out.println(result);
		parser.writeTree();
		*/
		
		FileOutputStream ntsOut = new FileOutputStream("analizator/nts.ser");
		ObjectOutputStream nOut = new ObjectOutputStream(ntsOut);
		nOut.writeObject(nonTerminalSigns);
		nOut.close();
		ntsOut.close();
		
		FileOutputStream tsOut = new FileOutputStream("analizator/ts.ser");
		ObjectOutputStream tOut = new ObjectOutputStream(tsOut);
		tOut.writeObject(terminalSigns);
		tOut.close();
		tsOut.close();
		
		FileOutputStream parserOut = new FileOutputStream("analizator/parser.ser");
		ObjectOutputStream pOut = new ObjectOutputStream(parserOut);
		pOut.writeObject(parser);
		pOut.close();
		parserOut.close();
	}
}
