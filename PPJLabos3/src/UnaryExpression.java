import java.util.List;


public class UnaryExpression extends TreeNode implements ICheckable {

	private String type;
	private boolean lExpression;
	
	public UnaryExpression(TreeNodeData data) {
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
		
		if (children.size() == 1) {
			// <postfiks_izraz>
			PostfixExpression postfixExpression = (PostfixExpression) children.get(0);
			
			postfixExpression.check();
			
			type = postfixExpression.getType();
			lExpression = postfixExpression.islExpression();
		} else {
			if (children.get(0).getData().getName() == "<unarni_operator>") {
				// <unarni_operator> <cast_izraz>
				String errorMessage = "<unarni_izraz> ::= <unarni_operator> <cast_izraz>";
				
				CastExpression castExpression = (CastExpression) children.get(1);
				
				castExpression.check();
				Checker.throwException(Checker.checkTildaOperator(castExpression.getType(), "int"), errorMessage);
				
				type = "int";
				lExpression = false;
			} else {
				// (OP_INC | OP_DEC) <unarni_izraz>
				String errorMessage = "<unarni_izraz> ::= " + children.get(0) + " <unarni_izraz>";
				UnaryExpression unaryExpression = (UnaryExpression) children.get(1);
				
				unaryExpression.check();
				Checker.throwException(unaryExpression.islExpression(), errorMessage);
				Checker.throwException(Checker.checkTildaOperator(unaryExpression.getType(), "int"), errorMessage);
				
				type = "int";
				lExpression = false;
			}
		}

	}

}
