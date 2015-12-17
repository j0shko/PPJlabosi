import java.util.List;


public class OuterDeclaration extends TreeNode implements ICheckable {

	public OuterDeclaration(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		// <definicija_funkcije> | <deklaracija>
		List<TreeNode> children = getChildren();
		ICheckable command = (ICheckable) children.get(0);
		
		command.check();
	}

}
