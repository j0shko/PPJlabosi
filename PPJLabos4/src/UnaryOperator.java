import java.util.List;


public class UnaryOperator extends TreeNode implements ICheckable, IGeneratable {

	public static Boolean negated = null;
	
	public UnaryOperator(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		// PLUS | MINUS | OP_TILDA | OP_NEG
		return;
	}
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		String value = ((TerminalSignData) children.get(0).getData()).getValue();
		if (value.equals("-")) {
			negated = true;
		}
	}
}
