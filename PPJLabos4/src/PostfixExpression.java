import java.util.List;


public class PostfixExpression extends TreeNode implements ICheckable, IGeneratable {

	private String type;
	private boolean lExpression;
	
	public PostfixExpression(TreeNodeData data) {
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
		List<TreeNode> children = getChildren();
		String errorMessage;
		if (children.size() == 1) {
			// <primarni_izraz>
			
			errorMessage = "<postfiks_izraz> ::= <primarni_izraz>";
			PrimaryExpression primaryExpression = (PrimaryExpression) children.get(0);
			
			primaryExpression.check();
			
			type = primaryExpression.getType();
			lExpression = primaryExpression.islExpression();
		} else if (children.size() == 2) {
			// <postfiks_izraz> (OP_INC | OP_DEC)
			
			errorMessage = "<postfiks_izraz> ::= <postfiks_izraz> "+ children.get(1);
			PostfixExpression postfixExpression = (PostfixExpression) children.get(0);
			
			postfixExpression.check();
			
			Checker.throwException(postfixExpression.islExpression(), errorMessage);
			Checker.throwException(Checker.checkTildaOperator(postfixExpression.getType(), "int"), errorMessage);
			
			type = "int";
			lExpression = false;
		} else if (children.size() == 3) {
			// <postfiks_izraz> L_ZAGRADA D_ZAGRADA
			
			errorMessage = "<postfiks_izraz> ::= <postfiks_izraz> " + children.get(1) + " " + children.get(2);
			PostfixExpression postfixExpression = (PostfixExpression) children.get(0);
			
			postfixExpression.check();
			
			Checker.throwException(Checker.isFunction(postfixExpression.getType()), errorMessage);
			Checker.throwException(!Checker.isFunctionWithParams(postfixExpression.getType()), errorMessage);
			
			type = Checker.getFunctionReturnValue(postfixExpression.getType());
			lExpression = false;
		} else if (children.size() == 4) {
			if (children.get(1).getData().getName().equals("L_ZAGRADA")) {
				// <postfiks_izraz> L_ZAGRADA <lista_argumenata> D_ZAGRADA
				
				errorMessage = "<postfiks_izraz> ::= <postfiks_izraz> " + children.get(1)
									+ " <lista_argumenata> " + children.get(3); 
				PostfixExpression postfixExpression = (PostfixExpression) children.get(0);
				
				postfixExpression.check();
				
				ArgumentList argumentList = (ArgumentList) children.get(2);
				argumentList.check();
				
				Checker.throwException(Checker.isFunction(postfixExpression.getType()), errorMessage);
				Checker.throwException(Checker.isFunctionWithParams(postfixExpression.getType()), errorMessage);
				
				List<String> params = Checker.getFunctionParameters(postfixExpression.getType());
				List<String> args = argumentList.getTypes();
				
				Checker.throwException(params.size() == args.size(), errorMessage);
				for (int i = 0; i < params.size(); i++) {
					Checker.throwException(Checker.checkTildaOperator(args.get(i), params.get(i)), errorMessage);
				}
				
				type = Checker.getFunctionReturnValue(postfixExpression.getType());
				lExpression = false;
			} else {
				// <postfiks_izraz> L_UGL_ZAGRADA <izraz> D_UGL_ZAGRADA
				
				errorMessage = "<postfiks_izraz> ::= <postfiks_izraz> " + children.get(1) 
									+ " <izraz> " + children.get(3);
				PostfixExpression postfixExpression = (PostfixExpression) children.get(0);
				
				postfixExpression.check();
				
				boolean isArray = Checker.isArrayType(postfixExpression.getType());
				Checker.throwException(isArray, errorMessage);
				
				Expression expression = (Expression) children.get(2);
				expression.check();
				Checker.throwException(Checker.checkTildaOperator(expression.getType(), "int"), errorMessage);
				String arrayType = Checker.getArrayType(postfixExpression.getType());
				type = arrayType;
				lExpression = !Checker.isConstantType(arrayType);
			}
		}
	}

}
