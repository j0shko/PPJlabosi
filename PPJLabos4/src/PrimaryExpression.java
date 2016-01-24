import java.util.List;

public class PrimaryExpression extends TreeNode implements ICheckable, IGeneratable {

	private String type;
	private boolean lExpression;
	
	private static int labelCounter = 0;
	
	public PrimaryExpression(TreeNodeData data) {
		super(data);
	}
	
	public String getType() {
		return type;
	}

	public boolean islExpression() {
		return lExpression;
	}

	@Override
	public void check() {
		String errorMessage;
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// IDN | BROJ | ZNAK | NIZ_ZNAKOVA

			TreeNode first = children.get(0);
			errorMessage = "<primarni_izraz> ::= " + first.toString();
			String value = ((TerminalSignData) first.getData()).getValue();
			switch (first.getData().getName()) {
			case "IDN":
				Checker.throwException(Checker.isNameDeclared(value), errorMessage);
				String indetificatorType = Checker.getTypeForName(value);
				type = indetificatorType;
				if (Checker.isFunction(type)) {
					lExpression = false;
				} else {
					lExpression = Checker.getIdentificator(value).islExpression(); 
				}
				
				break;
			case "BROJ":
				Checker.throwException(Checker.checkInteger(value), errorMessage);
				type = "int";
				lExpression = false;
				break;
			case "ZNAK":
				Checker.throwException(Checker.checkChar(value), errorMessage);
				type = "char";
				lExpression = false;
				break;
			case "NIZ_ZNAKOVA":
				Checker.throwException(Checker.checkString(value), errorMessage);
				if (Initialisator.initialisatorCalled) {
					value = Checker.removeEscapes(value);
					Initialisator.string = value.substring(1, value.length() - 1);
				}
				type = "const(char)[]";
				lExpression = false;
			}
		} else if (children.size() == 3) {
			// L_ZAGRADA <izraz> D_ZAGRADA
			
			errorMessage = "<primarni_izraz> ::= " + children.get(0) + " <izraz> " + children.get(2);
			
			Expression expression = (Expression) children.get(1);
			expression.check();
			
			type = expression.getType();
			lExpression = expression.islExpression();
		}
	} 

	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		
		// TODO sve osim broja

		if (children.size() == 1) {
			// IDN | BROJ | ZNAK | NIZ_ZNAKOVA

			TreeNode first = children.get(0);
			String value = ((TerminalSignData) first.getData()).getValue();
			switch (first.getData().getName()) {
			case "IDN":
				String indetificatorType = Checker.getTypeForName(value);
				type = indetificatorType;
				if (Checker.isFunction(type)) {
					GeneratorKoda.lines.add("\tCALL F_" + value.toUpperCase());
					GeneratorKoda.lines.add("\tPUSH R6");
					lExpression = false;
				} else {
					Scope.IdentificatorData identificator = Checker.getIdentificator(value);
					lExpression = identificator.islExpression(); 
					GeneratorKoda.lines.add("\tLOAD R0, (" + identificator.getLabel()+ ")");
					GeneratorKoda.lines.add("\tPUSH R0");
				}
				
				break;
			case "BROJ":
				int num = Checker.getNumberValue(value);
				if (UnaryOperator.negated != null && UnaryOperator.negated == true) {
					num *= -1;
					UnaryOperator.negated = null;
				}
				
				if (Initialisator.initialisatorCalled) {
					Initialisator.value = Integer.toString(num);
				} else {
					if (num > 524287 || num < -524288) {
						String label = "HELPNUM" + labelCounter;
						GeneratorKoda.lines.add(label + "\tDW %D " + num);
						labelCounter++;
						GeneratorKoda.lines.add("\tLOAD R0, (" + label + ")" );
						GeneratorKoda.lines.add("\tPUSH R0");
					} else {
						GeneratorKoda.lines.add("\tMOVE %D " + num + ", R0");
						GeneratorKoda.lines.add("\tPUSH R0");
					}
				}
				
				break;
			case "ZNAK":
				type = "char";
				lExpression = false;
				break;
			case "NIZ_ZNAKOVA":
				if (Initialisator.initialisatorCalled) {
					value = Checker.removeEscapes(value);
					Initialisator.string = value.substring(1, value.length() - 1);
				}
				type = "const(char)[]";
				lExpression = false;
			}
		} else if (children.size() == 3) {
			// L_ZAGRADA <izraz> D_ZAGRADA
			
			Expression expression = (Expression) children.get(1);
			expression.generateCode();
		}
	}
}
