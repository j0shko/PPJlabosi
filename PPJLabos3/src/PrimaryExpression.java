import java.util.List;

public class PrimaryExpression extends TreeNode implements ICheckable {

	private String type;
	private boolean lExpression;
	
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
				// TODO vamo bi moglo još grešaka zapast
				boolean identificatorDeclared = Checker.isIdentificatorDeclared(value);
				boolean functionDeclared = Checker.isFunctionDeclared(value);
				Checker.throwException(identificatorDeclared || functionDeclared, errorMessage);
				if (identificatorDeclared) {
					Scope.IdentificatorData identificator = Checker.getIdentificator(value);
					type = identificator.getType();
					lExpression = identificator.islExpression();
				} else {
					Scope.FunctionData function = Checker.getFunction(value);
					type = function.getType();
					lExpression = false;
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
					Initialisator.string = value;
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

}
