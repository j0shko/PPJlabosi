import java.util.List;

public class LogicalAndExpression extends TreeNode implements ICheckable {

	private String type;
	private boolean lExpression;
	
	public LogicalAndExpression(TreeNodeData data) {
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
			// <bin_ili_izraz>
			
			BinaryOrExpression binaryOrExpression = (BinaryOrExpression) children.get(0);
			
			binaryOrExpression.check();
			
			type = binaryOrExpression.getType();
			lExpression = binaryOrExpression.islExpression();
		} else {
			// <log_i_izraz> OP_I <bin_ili_izraz>
			String errorMessage = "<log_i_izraz> ::= <log_i_izraz> " + children.get(1) + " <bin_ili_izraz>";
			
			LogicalAndExpression logicalAndExpression = (LogicalAndExpression) children.get(0);
			
			logicalAndExpression.check();
			Checker.throwException(Checker.checkTildaOperator(logicalAndExpression.getType(), "int"), errorMessage);
			
			BinaryOrExpression binaryOrExpression = (BinaryOrExpression) children.get(2);
			
			binaryOrExpression.check();
			Checker.throwException(Checker.checkTildaOperator(binaryOrExpression.getType(), "int"), errorMessage);
			
			type = "int";
			lExpression = false;
		}

	}
}
