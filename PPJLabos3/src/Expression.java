import java.util.List;

public class Expression extends TreeNode implements ICheckable {

	private String type;
	private boolean lExpression;
	
	public Expression(TreeNodeData data) {
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
			// <izraz_pridruzivanja>
			
			AssignmentExpression assignmentExpression = (AssignmentExpression) children.get(0);
			
			assignmentExpression.check();
			
			type = assignmentExpression.getType();
			lExpression = assignmentExpression.islExpression();
		} else {
			// <izraz> ZAREZ <izraz_pridruzivanja>
			Expression expression = (Expression) children.get(0);
			
			expression.check();
			
			AssignmentExpression assignmentExpression = (AssignmentExpression) children.get(2);
			
			assignmentExpression.check();
			
			type = assignmentExpression.getType();
			lExpression = false;
		}
	}
}
