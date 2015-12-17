import java.util.List;


public class RelationsExpression extends TreeNode implements ICheckable {

	private String type;
	private boolean lExpression;
	
	public RelationsExpression(TreeNodeData data) {
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
			// <aditivni_izraz>
			
			AditiveExpression aditiveExpression = (AditiveExpression) children.get(0);
			
			aditiveExpression.check();
			
			type = aditiveExpression.getType();
			lExpression = aditiveExpression.islExpression();
		} else {
			// <odnosni_izraz> (OP_LT | OP_GT | OP_LTE | OP_GTE) <aditivni_izraz>
			String errorMessage = "<odnosni_izraz> ::= <odnosni_izraz> " + children.get(1) + " <aditivni_izraz>";
			
			RelationsExpression relationsExpression = (RelationsExpression) children.get(0);
			
			relationsExpression.check();
			Checker.throwException(Checker.checkTildaOperator(relationsExpression.getType(), "int"), errorMessage);
			
			AditiveExpression aditiveExpression = (AditiveExpression) children.get(2);
			
			aditiveExpression.check();
			Checker.throwException(Checker.checkTildaOperator(aditiveExpression.getType(), "int"), errorMessage);
			
			type = "int";
			lExpression = false;
		}

	}

}
