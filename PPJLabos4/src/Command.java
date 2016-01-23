import java.util.List;

public class Command extends TreeNode implements ICheckable {

	public Command(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		// <slozena_naredba> | <izraz_naredba> | <naredba_grananja>	| <naredba_petlje> | <naredba_skoka>
		
		List<TreeNode> children = getChildren();
		ICheckable command = (ICheckable) children.get(0);
		
		if (children.get(0).getData().getName().equals("<slozena_naredba>")) {
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope);
			parentScope.addChildScope(Scope.currentScope);
			
			command.check();
			
			Scope.currentScope = parentScope;
		} else {
			command.check();
		}
	}
}
