import java.util.List;

public class DeclarationList extends TreeNode implements ICheckable {

	public DeclarationList(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <deklaracija>
			Declaration declaration = (Declaration) children.get(0);
			
			declaration.check();
		} else {
			// <lista_deklaracija> <deklaracija>
			DeclarationList declarationList = (DeclarationList) children.get(0);
			
			declarationList.check();
			
			Declaration declaration = (Declaration) children.get(1);
			
			declaration.check();
		}
	}
}
