import java.util.List;


public class ParameterDeclaration extends TreeNode implements ICheckable {

	private String name;
	private String type;
	
	public ParameterDeclaration(TreeNodeData data) {
		super(data);
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		if (children.size() == 2) {
			// <ime_tipa> IDN
			String errorMessage = "<deklaracija_parametra> ::= <ime_tipa> " + children.get(1);
			
			TypeName typeName = (TypeName) children.get(0);
			Checker.throwException(typeName.getType() != "void", errorMessage);
			
			name = ((TerminalSignData) children.get(1).getData()).getValue();
			type = typeName.getType();
		} else {
			// <ime_tipa> IDN L_UGL_ZAGRADA D_UGL_ZAGRADA
			String errorMessage = "<deklaracija_parametra> ::= <ime_tipa> " + children.get(1) 
									+ " " + children.get(2) + " " + children.get(3);
			
			TypeName typeName = (TypeName) children.get(0);
			Checker.throwException(typeName.getType() != "void", errorMessage);
			
			name = ((TerminalSignData) children.get(1).getData()).getValue();
			type = typeName.getType() + "[]";
		}
	}
}
