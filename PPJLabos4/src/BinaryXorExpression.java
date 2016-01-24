import java.util.List;


public class BinaryXorExpression extends TreeNode implements ICheckable, IGeneratable{

	private String type;
	private boolean lExpression;
	
	public BinaryXorExpression(TreeNodeData data) {
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
			// <bin_i_izraz>
			
			BinaryAndExpression binaryAndExpression = (BinaryAndExpression) children.get(0);
			
			binaryAndExpression.check();
			
			type = binaryAndExpression.getType();
			lExpression = binaryAndExpression.islExpression();
		} else {
			// <bin_xili_izraz> OP_BIN_XILI <bin_i_izraz>
			String errorMessage = "<bin_xili_izraz> ::= <bin_xili_izraz> " + children.get(1) + " <bin_i_izraz>";
			
			BinaryXorExpression binaryXorExpression = (BinaryXorExpression) children.get(0);
			
			binaryXorExpression.check();
			Checker.throwException(Checker.checkTildaOperator(binaryXorExpression.getType(), "int"), errorMessage);
			
			BinaryAndExpression binaryAndExpression = (BinaryAndExpression) children.get(2);
			
			binaryAndExpression.check();
			Checker.throwException(Checker.checkTildaOperator(binaryAndExpression.getType(), "int"), errorMessage);
			
			type = "int";
			lExpression = false;
		}

	}
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <bin_i_izraz>
			
			BinaryAndExpression binaryAndExpression = (BinaryAndExpression) children.get(0);
			
			binaryAndExpression.generateCode();
		} else {
			// <bin_xili_izraz> OP_BIN_XILI <bin_i_izraz>
			BinaryXorExpression binaryXorExpression = (BinaryXorExpression) children.get(0);
			
			binaryXorExpression.generateCode();
			
			BinaryAndExpression binaryAndExpression = (BinaryAndExpression) children.get(2);
			
			binaryAndExpression.generateCode();
			
			GeneratorKoda.lines.add("\tPOP R0"); // load binaryAndExpression result
			GeneratorKoda.lines.add("\tPOP R1"); // load binaryXorExpression result
			GeneratorKoda.lines.add("\tXOR R1, R0, R0");
			GeneratorKoda.lines.add("\tPUSH R0");
		}
	}
}
