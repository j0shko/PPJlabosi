import java.util.ArrayList;
import java.util.List;

public class ArgumentList extends TreeNode implements ICheckable, IGeneratable {

	private List<String> types = new ArrayList<>();
	
	public ArgumentList(TreeNodeData data) {
		super(data);
	}
	
	public List<String> getTypes() {
		return types;
	}
	
	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// <izraz_pridruzivanja> 
			
			AssignmentExpression assignmentExpression = (AssignmentExpression) children.get(0);
			assignmentExpression.check();
			types.add(assignmentExpression.getType());
		} else {
			// <lista_argumenata> ZAREZ <izraz_pridruzivanja>
			
			ArgumentList argumentList = (ArgumentList) children.get(0);
			argumentList.check();
			
			AssignmentExpression assignmentExpression = (AssignmentExpression) children.get(2);
			assignmentExpression.check();
			types.addAll(argumentList.getTypes());
			types.add(assignmentExpression.getType());
		}
	}

	@Override
	public void generateCode() {
		// TODO Auto-generated method stub
		
	}
}
