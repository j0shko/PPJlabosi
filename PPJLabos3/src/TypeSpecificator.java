import java.util.List;

public class TypeSpecificator extends TreeNode implements ICheckable {

	private String type;
	
	public TypeSpecificator(TreeNodeData data) {
		super(data);
	}

	public String getType() {
		return type;
	}
	
	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		switch (children.get(0).getData().getName()) {
		case "KR_VOID": 
			type = "void";
			break;
		case "KR_CHAR":
			type = "char";
			break;
		case "KR_INT":
			type = "int";
			break;
		}
	}
}
