import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


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
		end = 0;
		current = 0;
		
		automat.resetAutomat();
		lineNum = 1;
		
		char currentChar;
		int size = code.length();
		List<Rule> currentRules = new ArrayList<>();
		currentRules.addAll(automat.getCurrentState().getRules());
		for (Rule cRule : currentRules) {
			cRule.resetAutomat();
		}
		
		Map<Rule, Integer> acceptableRules = new HashMap<>();
		Rule acceptedRule = null;
		while (current < size) {
			do {
				if (currentRules.isEmpty()) {
					break;
				} else {
					if (!currentRules.isEmpty() && acceptableRules.isEmpty()) {
						if (current == size) break;
						currentChar = code.charAt(current);
						current++;
						
						Iterator<Rule> it = currentRules.iterator();
						while(it.hasNext()) {
							Rule rule = it.next();
							if (!rule.canMakeTransitions(currentChar)) {
								it.remove();
							} else {
								rule.makeTransitions(currentChar);
							}
						}
						for (Rule rule : currentRules) {
							if (rule.hasAcceptableStates()) {
								acceptableRules.put(rule, current-1);
							}
						}
					} else if (!acceptableRules.isEmpty()) {
						int smallestIndex = Integer.MAX_VALUE;
						
						for (Rule rule : acceptableRules.keySet()) {
							if (acceptedRule == null) {
								acceptedRule = rule;
								smallestIndex = rule.getIndex();
							} else {
								int currentLenght = acceptableRules.get(rule);
								int maxLenght = acceptableRules.get(acceptedRule);
								if (currentLenght > maxLenght) {
									acceptedRule = rule;
									smallestIndex = rule.getIndex();
								} else if (currentLenght == maxLenght) {
									if (rule.getIndex() < smallestIndex) {
										smallestIndex = rule.getIndex();
										acceptedRule = rule;
									}
								}
							}
						}
						
						end =  acceptableRules.get(acceptedRule);
						
						if (current == size) break;
						currentChar = code.charAt(current);
						current++;
						
						Iterator<Rule> it = currentRules.iterator();
						while(it.hasNext()) {
							Rule rule = it.next();
							if (!rule.canMakeTransitions(currentChar)) {
								it.remove();
							} else {
								rule.makeTransitions(currentChar);
							}
						}
						for (Rule rule : currentRules) {
							if (rule.hasAcceptableStates()) {
								acceptableRules.put(rule, current-1);
							}
						}
					}
				}
			} while (1 == 1);
			
			if (acceptedRule == null) {
				// greska? 
				System.err.println("greska");
				
				start++;
				current = start;
				
				currentRules.clear();
				currentRules.addAll(automat.getCurrentState().getRules());
				for (Rule cRule : currentRules) {
					cRule.resetAutomat();
				}
				acceptableRules.clear();
			} else {
				executeRuleActions(acceptedRule);
				acceptedRule = null;
				
				currentRules.clear();
				currentRules.addAll(automat.getCurrentState().getRules());
				for (Rule cRule : currentRules) {
					cRule.resetAutomat();
				}
				acceptableRules.clear();
			}
		}
	}
	

	private static void executeRuleActions(Rule rule) {
		List<Action> actions = rule.getActions();
		
		boolean hasSkip = false;
		boolean hasPrint = false;
		boolean hasReturn = false;
		int returnFactor = 0;
		String unitName = "";
		for (Action ac : actions) {
			if (ac instanceof NewLineAction) {
				lineNum++;
			} else if (ac instanceof RejectAction) {
				hasSkip = true;
			} else if (ac instanceof ReturnAction) {
				ReturnAction ra = (ReturnAction) ac;
				hasReturn = true;
				returnFactor = ra.getIndex();
			} else if (ac instanceof EnterStateAction) {
				EnterStateAction esAc = (EnterStateAction) ac;
				automat.setCurrentState(esAc.getState());
			} else if (ac instanceof Unit) {
				Unit unit = (Unit) ac;
				hasPrint = true;
				hasSkip = true;
				unitName = unit.getName();
			}
		}
		
		if (hasReturn) {
			if (hasPrint) {
				System.out.println(unitName + " " + lineNum + " " + code.substring(start, start + returnFactor));
			}
			start = start + returnFactor;
			current = start;
		} else if (hasSkip) {
			if (hasPrint) {
				System.out.println(unitName + " " + lineNum + " " + code.substring(start, end + 1));
			}
			start = end + 1;
			current = start;
		}
	}
}
