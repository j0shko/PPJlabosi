import java.util.List;

public class EqualsExpression extends TreeNode implements ICheckable, IGeneratable {

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
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <odnosni_izraz>
			
			RelationsExpression relationsExpression = (RelationsExpression) children.get(0);
			
			relationsExpression.generateCode();
		} else {
			// <jednakosni_izraz> (OP_EQ | OP_NEQ) <odnosni_izraz>
			EqualsExpression equalsExpression = (EqualsExpression) children.get(0);
			
			equalsExpression.generateCode();
			
			RelationsExpression relationsExpression = (RelationsExpression) children.get(2);
			
			relationsExpression.generateCode();
			
			GeneratorKoda.lines.add("\tPOP R0"); // load aditiveExpression result
			GeneratorKoda.lines.add("\tPOP R1"); // load relationsExpression result
			GeneratorKoda.lines.add("\tCMP R1, R0");
			
			String labelTrue = "REL_T" + RelationsExpression.relationsCounter;
			String labelEnd = "REL_E" + RelationsExpression.relationsCounter;
			RelationsExpression.relationsCounter ++;
			String operation = children.get(1).getData().getName();
			if (operation.equals("OP_EQ")) {
				GeneratorKoda.lines.add("\tJP_EQ " + labelTrue);
			} else {
				GeneratorKoda.lines.add("\tJP_NE " + labelTrue);
			}
			
			GeneratorKoda.lines.add("\tMOVE 0, R0");
			GeneratorKoda.lines.add("\tJP " + labelEnd);
			GeneratorKoda.lines.add(labelTrue + "\tMOVE 1, R0");
			GeneratorKoda.lines.add(labelEnd + "\tPUSH R0");
		}
	}
}
