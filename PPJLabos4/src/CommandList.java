import java.util.List;


public class CommandList extends TreeNode implements ICheckable, IGeneratable {

	public CommandList(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// 	<naredba>
			
			Command command = (Command) children.get(0);
			
			command.check();
		} else {
			// <lista_naredbi> <naredba>
			
			CommandList commandList = (CommandList) children.get(0);
			
			commandList.check();
			
			Command command = (Command) children.get(1);
			
			command.check();
		}
	}

	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// 	<naredba>
			
			Command command = (Command) children.get(0);
			
			command.generateCode();
		} else {
			// <lista_naredbi> <naredba>
			
			CommandList commandList = (CommandList) children.get(0);
			
			commandList.generateCode();
			
			Command command = (Command) children.get(1);
			
			command.generateCode();
		}
	}
}
