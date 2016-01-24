import java.util.List;


public class RelationsExpression extends TreeNode implements ICheckable, IGeneratable {

	private String type;
	private boolean lExpression;
	
	public static int relationsCounter = 0;
	
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

	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <aditivni_izraz>
			
			AditiveExpression aditiveExpression = (AditiveExpression) children.get(0);
			
			aditiveExpression.generateCode();
		} else {
			// <odnosni_izraz> (OP_LT | OP_GT | OP_LTE | OP_GTE) <aditivni_izraz>
			RelationsExpression relationsExpression = (RelationsExpression) children.get(0);
			
			relationsExpression.generateCode();
			
			AditiveExpression aditiveExpression = (AditiveExpression) children.get(2);
			
			aditiveExpression.generateCode();
			
			GeneratorKoda.lines.add("\tPOP R0"); // load aditiveExpression result
			GeneratorKoda.lines.add("\tPOP R1"); // load relationsExpression result
			GeneratorKoda.lines.add("\tCMP R1, R0");
			
			String labelTrue = "REL_T" + relationsCounter;
			String labelEnd = "REL_E" + relationsCounter;
			relationsCounter ++;
			String operation = children.get(1).getData().getName();
			if (operation.equals("OP_LT")) {
				GeneratorKoda.lines.add("\tJP_SLT " + labelTrue);
			} else if (operation.equals("OP_GT")) {
				GeneratorKoda.lines.add("\tJP_SGT " + labelTrue);
			} else if (operation.equals("OP_LTE")) {
				GeneratorKoda.lines.add("\tJP_SLE " + labelTrue);
			} else {
				GeneratorKoda.lines.add("\tJP_SGE " + labelTrue);
			}
			
			GeneratorKoda.lines.add("\tMOVE 0, R0");
			GeneratorKoda.lines.add("\tJP " + labelEnd);
			GeneratorKoda.lines.add(labelTrue + "\tMOVE 1, R0");
			GeneratorKoda.lines.add(labelEnd + "\tPUSH R0");
		}
	}
	
}
