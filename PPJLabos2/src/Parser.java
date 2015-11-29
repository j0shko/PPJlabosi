import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser implements Serializable {

	
	private static final long serialVersionUID = -3211749069087582070L;
	
	private Deque<Integer> stateStack = new ArrayDeque<>();
	private Deque<SignData> signStack = new ArrayDeque<>();
	
	List<Map<Sign, Action>> actions;
	
	private int pointer; 
	private List<SignData> input = new ArrayList<>();
	
	private boolean stop;
	private boolean valid;
	
	private SignData treeRoot;
	
	public class SignData implements Serializable {
		private static final long serialVersionUID = 8648835406412924843L;
		private Sign sign;
		private int lineNum;
		private String data;
		
		private List<SignData> children = new ArrayList<>();
		
		public SignData(Sign sign, int lineNum, String data) {
			this.sign = sign;
			this.lineNum = lineNum;
			this.data = data;
		}
		
		public void addChild(SignData sign) {
			children.add(0, sign);
		}
		
		public List<SignData> getChildren() {
			return children;
		}
		
		@Override
		public String toString() {
			if (lineNum == -1 && data.equals("")) {
				return sign.toString();
			} else {
				return sign.toString() + " " + lineNum + " " + data;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((data == null) ? 0 : data.hashCode());
			result = prime * result + lineNum;
			result = prime * result + ((sign == null) ? 0 : sign.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SignData other = (SignData) obj;
			if (data == null) {
				if (other.data != null)
					return false;
			} else if (!data.equals(other.data))
				return false;
			if (lineNum != other.lineNum)
				return false;
			if (sign == null) {
				if (other.sign != null)
					return false;
			} else if (!sign.equals(other.sign))
				return false;
			return true;
		}

	}
	
	public interface Action {
		public void doAction(SignData sign);
	}
	
	public class MoveAction implements Action, Serializable {
		private static final long serialVersionUID = 1408827613429602634L;
		private int destinationStateNum;
		
		public MoveAction(int num) {
			destinationStateNum = num;
		}

		@Override
		public void doAction(SignData sign) {
			signStack.push(sign);
			pointer++;
			stateStack.push(destinationStateNum);
		}
		
		@Override
		public String toString() {
			return "Move(" + destinationStateNum + ")";
		}
	}
	
	public class ReduceAction implements Action, Serializable {
		private static final long serialVersionUID = -1677779706508906895L;
		private LLProduction production;
		
		public ReduceAction(LLProduction production) {
			this.production = production;
		}

		@Override
		public void doAction(SignData sign) {
			List<Sign> rightSide = production.getRightSide();
			SignData newSign = new SignData(production.getLeftSide(), -1, "");
			if (!rightSide.get(0).equals(TerminalSign.EPSILON)) {
				for (int i = 0, end = rightSide.size(); i < end; i++) {
					stateStack.pop();
					newSign.addChild(signStack.pop());
				}
			} else {
				newSign.addChild(new SignData(TerminalSign.EPSILON, -1, ""));
			}
			signStack.push(newSign);
		}
		
		@Override
		public String toString() {
			return "Reduce(" + production + ")";
		}
	}
	
	public class PutAction implements Action,Serializable {
		private static final long serialVersionUID = -5786920715300179228L;
		private int stateNum;
		
		public PutAction(int num) {
			stateNum = num;
		}

		@Override
		public void doAction(SignData sign) {
			stateStack.push(stateNum);
		}
		
		@Override
		public String toString() {
			return "Put(" + stateNum + ")";
		}
	}
	
	public class AcceptAction implements Action,Serializable {
		private static final long serialVersionUID = -1874687219879468038L;

		@Override
		public void doAction(SignData sign) {
			stop = true;
			valid = true;
		}
		
		@Override
		public String toString() {
			return "Accept()";
		}
	}
	
	public Parser(DKA dka, NonTerminalSign initialSign) {
		this.actions = new ArrayList<>();
		pointer = 0;
		
		createFromDka(dka, initialSign);
	}
	
	public void inputLine(Sign sign, int lineNum, String data) {
		input.add(new SignData(sign, lineNum, data));
	}
	
	public boolean parse() {
		valid = false;
		stop = false;
		pointer = 0;
		stateStack.push(0);
		signStack.clear();
		
		while (!stop) {
			SignData currentSign = null;
			if (pointer < input.size()) {
				currentSign = input.get(pointer);
			} else {
				currentSign = new SignData(TerminalSign.END, -1, "");
			}
			int currentState = stateStack.peek();
			SignData currentTop = signStack.peek();
			if (stateStack.size() == signStack.size()) {
				Action action = actions.get(currentState).get(currentTop.sign);
				if (action != null) {
					action.doAction(currentSign);
				} else {
					System.err.println("Error in line "+ currentSign.lineNum);
					System.err.println(">PutAction error.");
				}
			} else {
				Action action = actions.get(currentState).get(currentSign.sign);
				if (action != null) {
					action.doAction(currentSign);
				} else {
					System.err.println("Error in line " + currentSign.lineNum);
					System.err.print("Expected sign/s: ");
					for (Sign sign : actions.get(currentState).keySet()) {
						if (sign instanceof TerminalSign) {
							System.err.print(sign+ " ");
						}
					}
					System.err.println("\nRead sign: " + currentSign.sign);
					
					if (pointer < input.size()) {
						TerminalSign sign = (TerminalSign) input.get(pointer).sign;
						
						while(!sign.isSynchronSign()) {
							pointer++;
							if (pointer < input.size()) {
								sign = (TerminalSign) input.get(pointer).sign;
							} else {
								stop = true;
								break;
							}
						}
						
						currentState = stateStack.peek();
						while(!actions.get(currentState).containsKey(sign) && !stateStack.isEmpty() && !signStack.isEmpty()) {
							stateStack.pop();
							signStack.pop();
							//mislim da mozda treba paziti jos na signstack al ne znam sad
							currentState = stateStack.peek();
						}
					} else {
						stop = true;
					}
				}
			}
		}
		
		treeRoot = signStack.pop();
		
		return valid;
	}

	public void writeTree() {
		writeTreeRecursion(treeRoot, 0);
	}
	
	private void writeTreeRecursion(SignData root, int spaceCount) {
		for (int i = 0; i < spaceCount; i++) {
			System.out.print(" ");
		}
		System.out.println(root);
		for (SignData sign : root.children) {
			writeTreeRecursion(sign, spaceCount + 1);
		}
	}
	
	private void createFromDka(DKA dka, NonTerminalSign initialSign) {
		for (DKA.State state : dka.getStates()) {
			Map<Sign, Action> currentActions = new HashMap<>();
			
			Map<Sign, DKA.State> transits = state.getAllTransits();
			for (Sign sign : transits.keySet()) {
				if (sign instanceof TerminalSign) {
					currentActions.put(sign, new MoveAction(transits.get(sign).getNum()));
				} else {
					currentActions.put(sign, new PutAction(transits.get(sign).getNum()));
				}
			}
			List<LLProduction> productions = state.getProductions();
			for (LLProduction production : productions) {
				if (production.isDotOnEnd()) {
					if (production.getLeftSide().equals(initialSign)) {
						currentActions.put(TerminalSign.END, new AcceptAction());
						continue;
					}
					
					Set<TerminalSign> nextSigns = production.getNextSigns();
					for (TerminalSign sign : nextSigns) {
						if (currentActions.get(sign) != null) {
							// rjesi proturjecja
							Action takenAction = currentActions.get(sign);
							if (!(takenAction instanceof MoveAction)) {
								ReduceAction rAction = (ReduceAction) takenAction;
								int takenProductionPriority = rAction.production.getLeftSide()
														.getPriorityForProduction(rAction.production);
								int myPriority = production.getLeftSide()
														.getPriorityForProduction(production.withoutDot());
								
								if (myPriority > takenProductionPriority) {
									currentActions.put(sign, new ReduceAction(production.withoutDot()));
								}
							}
						} else {
							currentActions.put(sign, new ReduceAction(production.withoutDot()));
						}
					}
				}
			}
			
			actions.add(currentActions);
		}
	}
}
