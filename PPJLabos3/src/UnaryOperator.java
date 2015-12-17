
public class UnaryOperator extends TreeNode implements ICheckable {

	public UnaryOperator(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		// PLUS | MINUS | OP_TILDA | OP_NEG
		return;
	}
}
