import java.util.ArrayList;
import java.util.List;


public class AssignmentExpressionList extends TreeNode implements ICheckable, IGeneratable {

	private List<String> types = new ArrayList<>();
	
	public AssignmentExpressionList(TreeNodeData data) {
		super(data);
	}
	
	public List<String> getTypes() {
		return types;
	}
	
	public void addType(String type) {
		types.add(type);
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
			// <lista_izraza_pridruzivanja> ZAREZ <izraz_pridruzivanja>
			
			AssignmentExpressionList assignmentExpressionList = (AssignmentExpressionList) children.get(0);
			
			assignmentExpressionList.check();
			
			AssignmentExpression assignmentExpression = (AssignmentExpression) children.get(2);
			
			assignmentExpression.check();
			
			types.addAll(assignmentExpressionList.getTypes());
			types.add(assignmentExpression.getType());
		}
	}

	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <izraz_pridruzivanja>
			
			AssignmentExpression assignmentExpression = (AssignmentExpression) children.get(0);
			
			assignmentExpression.generateCode();
			
			types.add(assignmentExpression.getType());
		} else {
			// <lista_izraza_pridruzivanja> ZAREZ <izraz_pridruzivanja>
			
			AssignmentExpressionList assignmentExpressionList = (AssignmentExpressionList) children.get(0);
			
			assignmentExpressionList.generateCode();
			
			AssignmentExpression assignmentExpression = (AssignmentExpression) children.get(2);
			
			assignmentExpression.generateCode();
			
			types.addAll(assignmentExpressionList.getTypes());
			types.add(assignmentExpression.getType());
		}
	}
}
