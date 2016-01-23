import java.util.List;

public class LogicalOrExpression  extends TreeNode implements ICheckable, IGeneratable {

	private String type;
	private boolean lExpression;
	
	public LogicalOrExpression(TreeNodeData data) {
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
			// <log_i_izraz>
			
			LogicalAndExpression logicalAndExpression = (LogicalAndExpression) children.get(0);
			
			logicalAndExpression.check();
			
			type = logicalAndExpression.getType();
			lExpression = logicalAndExpression.islExpression();
		} else {
			// <log_ili_izraz> OP_ILI <log_i_izraz>
			String errorMessage = "<log_ili_izraz> ::= <log_ili_izraz> " + children.get(1) + " <log_i_izraz>";
			
			LogicalOrExpression logicalOrExpression = (LogicalOrExpression) children.get(0);
			
			logicalOrExpression.check();
			Checker.throwException(Checker.checkTildaOperator(logicalOrExpression.getType(), "int"), errorMessage);
			
			LogicalAndExpression logicalAndExpression = (LogicalAndExpression) children.get(2);
			
			logicalAndExpression.check();
			Checker.throwException(Checker.checkTildaOperator(logicalAndExpression.getType(), "int"), errorMessage);
			
			type = "int";
			lExpression = false;
		}
	}
	
	@Override
	public void generateCode() {
		// TODO Auto-generated method stub
		
	}
}
