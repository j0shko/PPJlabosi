import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


public class LA {

	private static LAAutomat automat;
	private static int lineNum;
	private static int startSub;
	private static int endSub;
	private static int end;
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
			code += currentLine;
		}
		
		startSub = 0;
		endSub = 1;
		end = code.length();
		
		automat.resetAutomat();
		lineNum = 0;
		
		List<Rule> acceptableRules = new ArrayList<>();
		while(startSub != end) {
			String substring = code.substring(startSub, endSub);
			for(Rule current : automat.getCurrentState().getRules()) {
				if (current.accepts(substring)) {
					acceptableRules.add(current);
				}
			}
			//ako grupirano ne paše nièem
			if (acceptableRules.size() == 0) {
				endSub++;
				continue;
			}
			
			if (acceptableRules.size() == 1) {
				Rule rule = acceptableRules.get(0);
				if (rule.accepts(code.substring(startSub, endSub + 1))) {
					endSub++;
					continue;
				} else {
					executeRuleActions(rule);
					endSub++;
				}
			}
		}
	}

	private static void executeRuleActions(Rule rule) {
		List<Action> actions = rule.getActions();
		for (Action ac : actions) {
			if (ac instanceof NewLineAction) {
				lineNum++;
			} else if (ac instanceof RejectAction) {
				startSub = endSub;
			} else if (ac instanceof ReturnAction) {
				// TODO uèini da radi
			} else if (ac instanceof EnterStateAction) {
				EnterStateAction esAc = (EnterStateAction) ac;
				automat.setCurrentState(esAc.getState());
			} else if (ac instanceof Unit) {
				Unit unit = (Unit) ac;
				System.out.println(unit.getName() + lineNum + code.substring(startSub, endSub)) ;
			}
		}
	}
}
