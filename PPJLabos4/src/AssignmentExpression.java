import java.util.List;

public class AssignmentExpression extends TreeNode implements ICheckable, IGeneratable {

	private String type;
	private boolean lExpression;
	
	public static boolean isAssignment = false;
	public static String identificatorName;
	public static long index = 0;
	
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
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <log_ili_izraz>
			LogicalOrExpression logicalOrExpression = (LogicalOrExpression) children.get(0);
			
			logicalOrExpression.generateCode();
		} else {
			// <postfiks_izraz> OP_PRIDRUZI <izraz_pridruzivanja>
			PostfixExpression postfixExpression = (PostfixExpression) children.get(0);
			
			isAssignment = true;
			postfixExpression.generateCode();
			isAssignment = false;
			
			Scope.IdentificatorData identificator = Checker.getIdentificator(identificatorName);
			AssignmentExpression assignmentExpression = (AssignmentExpression) children.get(2);
			
			PrimaryExpression.pushResult = true;
			assignmentExpression.generateCode();
			PrimaryExpression.pushResult = false;
			
			String address = identificator.getLabel();
			
			GeneratorKoda.lines.add("\tPOP R0");
			
			if (index > 0) {
				GeneratorKoda.lines.add("\tMOVE " + address + ", R4");
				address = "R4 + " + Scope.index * 4;
			}
			GeneratorKoda.lines.add("\tSTORE R0, (" + address + ")");
			
			identificatorName = null;
		}
	}

}
