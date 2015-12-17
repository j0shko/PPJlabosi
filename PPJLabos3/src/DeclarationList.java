import java.util.List;


public class DeclarationList extends TreeNode implements ICheckable {

	public DeclarationList(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		// TODO NAPRAVI DeclarationList

	}

}
