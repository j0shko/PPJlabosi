import java.util.List;

public class BinaryOrExpression extends TreeNode implements ICheckable {

	private String type;
	private boolean lExpression;
	
	public BinaryOrExpression(TreeNodeData data) {
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
			// <bin_xili_izraz>
			
			BinaryXorExpression binaryXorExpression = (BinaryXorExpression) children.get(0);
			
			binaryXorExpression.check();
			
			type = binaryXorExpression.getType();
			lExpression = binaryXorExpression.islExpression();
		} else {
			// <bin_ili_izraz> OP_BIN_ILI <bin_xili_izraz>
			String errorMessage = "<bin_ili_izraz> ::= <bin_ili_izraz> " + children.get(1) + " <bin_xili_izraz>";
			
			BinaryOrExpression binaryOrExpression = (BinaryOrExpression) children.get(0);
			
			binaryOrExpression.check();
			Checker.throwException(Checker.checkTildaOperator(binaryOrExpression.getType(), "int"), errorMessage);
			
			BinaryXorExpression binaryXorExpression = (BinaryXorExpression) children.get(2);
			
			binaryXorExpression.check();
			Checker.throwException(Checker.checkTildaOperator(binaryXorExpression.getType(), "int"), errorMessage);
			
			type = "int";
			lExpression = false;
		}

	}
}
