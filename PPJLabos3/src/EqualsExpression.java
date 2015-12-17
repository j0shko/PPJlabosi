import java.util.List;

public class EqualsExpression extends TreeNode implements ICheckable {

	private String type;
	private boolean lExpression;
	
	public EqualsExpression(TreeNodeData data) {
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
			// <odnosni_izraz>
			
			RelationsExpression relationsExpression = (RelationsExpression) children.get(0);
			
			relationsExpression.check();
			
			type = relationsExpression.getType();
			lExpression = relationsExpression.islExpression();
		} else {
			// <jednakosni_izraz> (OP_EQ | OP_NEQ) <odnosni_izraz>
			String errorMessage = "<jednakosni_izraz> ::= <jednakosni_izraz> " + children.get(1) + " <odnosni_izraz>";
			
			EqualsExpression equalsExpression = (EqualsExpression) children.get(0);
			
			equalsExpression.check();
			Checker.throwException(Checker.checkTildaOperator(equalsExpression.getType(), "int"), errorMessage);
			
			RelationsExpression relationsExpression = (RelationsExpression) children.get(2);
			
			relationsExpression.check();
			Checker.throwException(Checker.checkTildaOperator(relationsExpression.getType(), "int"), errorMessage);
			
			type = "int";
			lExpression = false;
		}

	}
}
