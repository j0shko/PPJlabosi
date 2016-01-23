import java.util.List;


public class OuterDeclaration extends TreeNode implements ICheckable, IGeneratable {

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

	@Override
	public void generateCode() {
		// <definicija_funkcije> | <deklaracija>
		List<TreeNode> children = getChildren();
		IGeneratable command = (IGeneratable) children.get(0);
				
		//TODO razlikovati ova dva sranja
		command.generateCode();
	}
}
