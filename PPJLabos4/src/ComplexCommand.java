import java.util.List;


public class ComplexCommand extends TreeNode implements ICheckable, IGeneratable {

	public ComplexCommand(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 3) {
			// L_VIT_ZAGRADA <lista_naredbi> D_VIT_ZAGRADA
			
			CommandList commandList = (CommandList) children.get(1);
			
			commandList.check();
		} else {
			// L_VIT_ZAGRADA <lista_deklaracija> <lista_naredbi> D_VIT_ZAGRADA
			
			DeclarationList declarationList = (DeclarationList) children.get(1);
			
			declarationList.check();
			
			CommandList commandList = (CommandList) children.get(2);
			
			commandList.check();
		}
		
	}
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 3) {
			// L_VIT_ZAGRADA <lista_naredbi> D_VIT_ZAGRADA
			
			CommandList commandList = (CommandList) children.get(1);
			
			commandList.generateCode();
		} else {
			// L_VIT_ZAGRADA <lista_deklaracija> <lista_naredbi> D_VIT_ZAGRADA
			
			DeclarationList declarationList = (DeclarationList) children.get(1);
			
			declarationList.generateCode();
			
			CommandList commandList = (CommandList) children.get(2);
			
			commandList.generateCode();
		}
	}
}
