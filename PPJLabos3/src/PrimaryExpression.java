
public class PrimaryExpression extends TreeNode implements ICheckable {

	public PrimaryExpression(TreeNodeData data) {
		super(data);
		
	}

	@Override
	public boolean check() {
		if (getChildren().size() == 1) {
			TreeNode first = getChildren().get(0);
			String value = ((TerminalSignData) first.getData()).getValue();
			switch (first.getData().getData()) {
			case "IDN":
				// TODO: provjeri jel IDN.ime deklarirano
				break;
			case "BROJ":
				return Checker.checkInteger(value);
			case "ZNAK":
				return Checker.checkChar(value);
			case "NIZ_ZNAKOVA":
				return Checker.checkString(value);
			default:
				// TODO makni matijina sranja
				System.err.println("pojedi govno");	
			}
		} else if (getChildren().size() == 3) {
			TreeNode left = getChildren().get(0);
			TreeNode expression = getChildren().get(1);
			TreeNode right = getChildren().get(2);
			
			if (left.getData().getData().equals("L_ZAGRADA")
				&& expression.getData().getData().equals("<izraz>")
				&& right.getData().getData().equals("R_ZAGRADA")) {
					return ((ICheckable) expression).check();
			}
		}
		return false;
	} 

}
