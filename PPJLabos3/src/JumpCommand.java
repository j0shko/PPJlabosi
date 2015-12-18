import java.util.List;


public class JumpCommand extends TreeNode implements ICheckable {

	public JumpCommand(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		if (children.size() == 2) {
			String errorMessage = "<naredba_skoka> ::= " + children.get(0) + " " + children.get(1);
			if (children.get(0).getData().getName().equals("KR_RETURN")) {
				// KR_RETURN TOCKAZAREZ
				
				Checker.throwException(Checker.isInsideVoidFunction(), errorMessage);
			} else {
				// (KR_CONTINUE | KR_BREAK) TOCKAZAREZ
				
				Checker.throwException(Checker.isInsideLoop(), errorMessage);
			}
		} else {
			// KR_RETURN <izraz> TOCKAZAREZ
			String errorMessage = "<naredba_skoka> ::= " + children.get(0) + " <izraz> " + children.get(2);
			
			Expression expression = (Expression) children.get(1);
			
			expression.check();
			
			Checker.throwException(Checker.isInsideNonVoidFunction(), errorMessage);
			
			String functionType = Checker.getReturnValueOfScopeFunction();
			Checker.throwException(Checker.checkTildaOperator(expression.getType(), functionType), errorMessage);
		}

	}
}
