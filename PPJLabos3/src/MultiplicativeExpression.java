import java.util.List;


public class MultiplicativeExpression extends TreeNode implements ICheckable {

	private String type;
	private boolean lExpression;
	
	public MultiplicativeExpression(TreeNodeData data) {
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
			// <cast_izraz>
			
			CastExpression castExpression = (CastExpression) children.get(0);
			
			castExpression.check();
			
			type = castExpression.getType();
			lExpression = castExpression.islExpression();
		} else {
			// <multiplikativni_izraz> (OP_PUTA | OP_DIJELI | OP_MOD) <cast_izraz>
			String errorMessage = "<multiplikativni_izraz> ::= <multiplikativni_izraz> "
										+ children.get(1) + " <cast_izraz>";
			
			MultiplicativeExpression multiplicativeExpression = (MultiplicativeExpression) children.get(0);
			
			multiplicativeExpression.check();
			Checker.throwException(Checker.checkTildaOperator(multiplicativeExpression.getType(), "int"), errorMessage);
			
			CastExpression castExpression = (CastExpression) children.get(2);
			
			castExpression.check();
			Checker.throwException(Checker.checkTildaOperator(castExpression.getType(), "int"), errorMessage);
			
			type = "int";
			lExpression = false;
		}
	}
}
