import java.util.List;


public class BinaryAndExpression extends TreeNode implements ICheckable, IGeneratable{

	private String type;
	private boolean lExpression;
	
	public BinaryAndExpression(TreeNodeData data) {
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
			// <jednakosni_izraz>
			
			EqualsExpression equalsExpression = (EqualsExpression) children.get(0);
			
			equalsExpression.check();
			
			type = equalsExpression.getType();
			lExpression = equalsExpression.islExpression();
		} else {
			// <bin_i_izraz> OP_BIN_I <jednakosni_izraz>
			String errorMessage = "<bin_i_izraz> ::= <bin_i_izraz> " + children.get(1) + " <jednakosni_izraz>";
			
			BinaryAndExpression binaryAndExpression = (BinaryAndExpression) children.get(0);
			
			binaryAndExpression.check();
			Checker.throwException(Checker.checkTildaOperator(binaryAndExpression.getType(), "int"), errorMessage);
			
			EqualsExpression equalsExpression = (EqualsExpression) children.get(2);
			
			equalsExpression.check();
			Checker.throwException(Checker.checkTildaOperator(equalsExpression.getType(), "int"), errorMessage);
			
			type = "int";
			lExpression = false;
		}

	}
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <jednakosni_izraz>
			
			EqualsExpression equalsExpression = (EqualsExpression) children.get(0);
			
			equalsExpression.generateCode();
		} else {
			// <bin_i_izraz> OP_BIN_I <jednakosni_izraz>
			if (Initialisator.initialisatorCalled) {
				Initialisator.expression = true;
			}
			
			BinaryAndExpression binaryAndExpression = (BinaryAndExpression) children.get(0);
			
			binaryAndExpression.generateCode();
			
			EqualsExpression equalsExpression = (EqualsExpression) children.get(2);
			
			equalsExpression.generateCode();
			
			GeneratorKoda.lines.add("\tPOP R0"); // load equalsExpression result
			GeneratorKoda.lines.add("\tPOP R1"); // load binaryAndExpression result
			GeneratorKoda.lines.add("\tAND R1, R0, R0");
			GeneratorKoda.lines.add("\tPUSH R0");
		}
	}
}
