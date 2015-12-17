import java.util.List;


public class AditiveExpression extends TreeNode implements ICheckable {

	private String type;
	private boolean lExpression;
	
	public AditiveExpression(TreeNodeData data) {
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
			// <multiplikativni_izraz>
			MultiplicativeExpression multiplicativeExpression = (MultiplicativeExpression) children.get(0);
			
			multiplicativeExpression.check();
			
			type = multiplicativeExpression.getType();
			lExpression = multiplicativeExpression.islExpression();
		} else {
			// <aditivni_izraz> (PLUS | MINUS) <multiplikativni_izraz>
			String errorMessage = "<aditivni_izraz> ::= <aditivni_izraz> " + children.get(1) + " <multiplikativni_izraz>";
			
			AditiveExpression aditiveExpression = (AditiveExpression) children.get(0);
			
			aditiveExpression.check();
			Checker.throwException(Checker.checkTildaOperator(aditiveExpression.getType(), "int"), errorMessage);
			
			MultiplicativeExpression multiplicativeExpression = (MultiplicativeExpression) children.get(2);
			
			multiplicativeExpression.check();
			Checker.throwException(Checker.checkTildaOperator(multiplicativeExpression.getType(), "int"), errorMessage);
			
			type = "int";
			lExpression = false;
		}

	}

}
