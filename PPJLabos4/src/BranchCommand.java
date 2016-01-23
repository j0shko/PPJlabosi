import java.util.List;


public class BranchCommand extends TreeNode implements ICheckable {

	public BranchCommand(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		if (children.size() == 5) {
			// KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba>
			String errorMessage = "<naredba_grananja> ::= " + children.get(0) + " " + children.get(1)
										+ " <izraz> " + children.get(3) + " <naredba>";
			
			Expression expression = (Expression) children.get(2);
			
			expression.check();
			Checker.throwException(Checker.checkTildaOperator(expression.getType(), "int"), errorMessage);

			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope);
			parentScope.addChildScope(Scope.currentScope);
			
			Command command = (Command) children.get(4);
			
			command.check();
			
			Scope.currentScope = parentScope;
		} else {
			// KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba>1 KR_ELSE <naredba>2
			String errorMessage = "<naredba_grananja> ::= " + children.get(0) + " " + children.get(1)
										+ " <izraz> " + children.get(3) + " <naredba> " + children.get(5) 
										+ " <naredba>";
			Expression expression = (Expression) children.get(2);
			
			expression.check();
			Checker.throwException(Checker.checkTildaOperator(expression.getType(), "int"), errorMessage);
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope);
			parentScope.addChildScope(Scope.currentScope);
			
			Command command1 = (Command) children.get(4);
			
			command1.check();
			
			Scope.currentScope = parentScope;
			
			parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope);
			parentScope.addChildScope(Scope.currentScope);
			
			Command command2 = (Command) children.get(6);
						
			command2.check();
			
			Scope.currentScope = parentScope;
		}

	}

}
