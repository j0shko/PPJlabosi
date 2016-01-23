import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	private List<TreeNode> children = new ArrayList<>();
	
	private TreeNodeData data;
	
	public TreeNode(TreeNodeData data) {
		this.data = data;
	}
	
	public TreeNodeData getData() {
		return data;
	}
	
	public void addChild(TreeNode child) {
		children.add(child);
	}
	
	public List<TreeNode> getChildren() {
		return children;
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
}