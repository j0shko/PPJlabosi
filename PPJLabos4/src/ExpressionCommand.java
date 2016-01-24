import java.util.List;


public class ExpressionCommand extends TreeNode implements ICheckable, IGeneratable {

	private String type;
	
	public ExpressionCommand(TreeNodeData data) {
		super(data);
	}

	public String getType() {
		return type;
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// TOCKAZAREZ
			
			type = "int";
		} else {
			// <izraz> TOCKAZAREZ
			
			Expression expression = (Expression) children.get(0);
			
			expression.check();
			
			type = expression.getType();
		}
	}

	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// TOCKAZAREZ
			
			type = "int";
		} else {
			// <izraz> TOCKAZAREZ
			
			Expression expression = (Expression) children.get(0);
			
			expression.generateCode();
			
			type = expression.getType();
		}
	}
}
