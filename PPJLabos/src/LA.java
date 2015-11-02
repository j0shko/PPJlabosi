import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LA {

	private static LAAutomat automat;
	private static int lineNum;
	private static int start;
	private static int end;
	private static int current;
	private static String code;
	
	public static void main(String[] args) throws IOException {
		automat = null;
		
		try {
			FileInputStream fileIn = new FileInputStream("analizator/automat.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			automat = (LAAutomat) in.readObject();
			in.close();
			fileIn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		code = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String currentLine;
		while ((currentLine = br.readLine()) != null) {
			code += currentLine + "\n";
		}
		
		start = 0;
		end = 1;
		current = 0;
		
		automat.resetAutomat();
		lineNum = 1;
		
		char currentChar;
		
		//-------------------------------------
		
		int size = code.length();
		List<Rule> currentRules = new ArrayList<>();
		currentRules.addAll(automat.getCurrentState().getRules());
		for (Rule cRule : currentRules) {
			cRule.resetAutomat();
		}
		List<Rule> acceptableRules = new ArrayList<>();
		while(current < size) {
			if (!currentRules.isEmpty()) {
				currentChar = code.charAt(current);
				Iterator<Rule> it = currentRules.iterator();
				while(it.hasNext()) {
					Rule rule = it.next();
					if (!rule.canMakeTransitions(currentChar)) {
						if (rule.hasAcceptableStates()) {
							acceptableRules.add(rule);
							end = current;
						} 
						
						it.remove();
					} else {
						rule.makeTransitions(currentChar);
					}
				}
				if (!currentRules.isEmpty()) {
					current++;
				}
			} else if (!acceptableRules.isEmpty()) {
				int smallestIndex = Integer.MAX_VALUE;
				int listIndex = 0;
				for (int i = 0; i < acceptableRules.size(); i++) {
					Rule rule = acceptableRules.get(i);
					if (rule.getIndex() < smallestIndex) {
						listIndex = i;
					}
				}
				Rule rule = acceptableRules.get(listIndex);
				executeRuleActions(rule);
				currentRules.clear();
				currentRules.addAll(automat.getCurrentState().getRules());
				for (Rule cRule : currentRules) {
					cRule.resetAutomat();
				}
				acceptableRules.clear();
			} else {
				System.err.println("greska");
			}
		}
	}
	

	private static void executeRuleActions(Rule rule) {
		List<Action> actions = rule.getActions();
		for (Action ac : actions) {
			if (ac instanceof NewLineAction) {
				lineNum++;
			} else if (ac instanceof RejectAction) {
				start = end;
				current = start;
			} else if (ac instanceof ReturnAction) {
				ReturnAction ra = (ReturnAction) ac;
				start += ra.getIndex();
				current = start;
			} else if (ac instanceof EnterStateAction) {
				EnterStateAction esAc = (EnterStateAction) ac;
				automat.setCurrentState(esAc.getState());
			} else if (ac instanceof Unit) {
				Unit unit = (Unit) ac;
				System.out.println(unit.getName() + " " + lineNum + " " + code.substring(start, end));
				start = end;
				current = start;
			}
		}
	}
}
