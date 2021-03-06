import java.util.List;

public class TypeName extends TreeNode implements ICheckable, IGeneratable {
	
	private String type;
	
	public TypeName(TreeNodeData data) {
		super(data);
	}

	public String getType() {
		return type;
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// <specifikator_tipa>
			
			TypeSpecificator typeSpecificator = (TypeSpecificator) children.get(0);
			
			typeSpecificator.check();
			
			type = typeSpecificator.getType();
		} else {
			// KR_CONST <specifikator_tipa>
			String errorMessage = "<ime_tipa> ::= " + children.get(0) + " <specifikator_tipa>";
			
			TypeSpecificator typeSpecificator = (TypeSpecificator) children.get(1);
			
			typeSpecificator.check();
			Checker.throwException(!typeSpecificator.getType().equals("void"), errorMessage);
			
			type = "const(" + typeSpecificator.getType() + ")";
		}
	}

	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// <specifikator_tipa>
			
			TypeSpecificator typeSpecificator = (TypeSpecificator) children.get(0);
			
			typeSpecificator.generateCode();
			
			type = typeSpecificator.getType();
		} else {
			// KR_CONST <specifikator_tipa>
			TypeSpecificator typeSpecificator = (TypeSpecificator) children.get(1);
			
			typeSpecificator.generateCode();
			
			type = "const(" + typeSpecificator.getType() + ")";
		}
	}
}
