import java.util.List;

public class AssignmentExpression extends TreeNode implements ICheckable {

	private String type;
	private boolean lExpression;
	
	public AssignmentExpression(TreeNodeData data) {
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
			// <log_ili_izraz>
			
			LogicalOrExpression logicalOrExpression = (LogicalOrExpression) children.get(0);
			
			logicalOrExpression.check();
			
			type = logicalOrExpression.getType();
			lExpression = logicalOrExpression.islExpression();
		} else {
			// <postfiks_izraz> OP_PRIDRUZI <izraz_pridruzivanja>
			String errorMessage = "<izraz_pridruzivanja> ::= <postfiks_izraz> " + children.get(1) + " <izraz_pridruzivanja>";
			
			PostfixExpression postfixExpression = (PostfixExpression) children.get(0);
			
			postfixExpression.check();
			Checker.throwException(postfixExpression.islExpression(), errorMessage);
			
			AssignmentExpression assignmentExpression = (AssignmentExpression) children.get(2);
			
			assignmentExpression.check();
			Checker.throwException(
					Checker.checkTildaOperator(assignmentExpression.getType(), postfixExpression.getType()),
					errorMessage
			);
			
			type = postfixExpression.getType();
			lExpression = false;
		}
 
	}

}
