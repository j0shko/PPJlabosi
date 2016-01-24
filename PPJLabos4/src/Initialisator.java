import java.util.ArrayList;
import java.util.List;


public class Initialisator extends TreeNode implements ICheckable, IGeneratable {

	public static String string = null;
	public static boolean initialisatorCalled = false;
	
	public static String value = null;
	public static List<String> array = new ArrayList<>();
	public static boolean expression = false;
	
	private String type;
	
	private List<String> types = new ArrayList<>();
	
	public Initialisator(TreeNodeData data) {
		super(data);
	}
	
	public String getType() {
		return type;
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
			
			initialisatorCalled = true;
			assignmentExpression.check();
			
			if (string != null) {
				for (int i = 0, end = string.length(); i < end; i++) {
					types.add("char");
				}
				types.add("char");
				string = null;
			} else {
				type = assignmentExpression.getType();
			}
			initialisatorCalled = false;
		} else {
			// L_VIT_ZAGRADA <lista_izraza_pridruzivanja> D_VIT_ZAGRADA
			
			AssignmentExpressionList assignmentExpressionList = (AssignmentExpressionList) children.get(1);
			
			assignmentExpressionList.check();
			
			types.addAll(assignmentExpressionList.getTypes());
		}
	}

	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <izraz_pridruzivanja>
			AssignmentExpression assignmentExpression = (AssignmentExpression) children.get(0);
			
			initialisatorCalled = true;
			assignmentExpression.generateCode();
			
			if (string != null) {
				for (int i = 0, end = string.length(); i < end; i++) {
					types.add("char");
				}
				types.add("char");
				string = null;
			} else {
				type = assignmentExpression.getType();
			}
			initialisatorCalled = false;
		} else {
			// L_VIT_ZAGRADA <lista_izraza_pridruzivanja> D_VIT_ZAGRADA
			
			AssignmentExpressionList assignmentExpressionList = (AssignmentExpressionList) children.get(1);
			
			initialisatorCalled = true;
			assignmentExpressionList.generateCode();
			initialisatorCalled = false;
			
			types.addAll(assignmentExpressionList.getTypes());
		}
	}
}
