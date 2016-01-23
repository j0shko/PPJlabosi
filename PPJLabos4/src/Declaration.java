import java.util.List;


public class Declaration extends TreeNode implements ICheckable, IGeneratable {

	public Declaration(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		
		// <ime_tipa> <lista_init_deklaratora> TOCKAZAREZ
		
		TypeName typeName = (TypeName) children.get(0);
		
		typeName.check();
		
		InitDeclaratorList initDeclaratorList = (InitDeclaratorList) children.get(1);
		initDeclaratorList.setnType(typeName.getType());
		
		initDeclaratorList.check();
	}
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		
		// <ime_tipa> <lista_init_deklaratora> TOCKAZAREZ
		
		TypeName typeName = (TypeName) children.get(0);
		
		typeName.generateCode();
		
		InitDeclaratorList initDeclaratorList = (InitDeclaratorList) children.get(1);
		initDeclaratorList.setnType(typeName.getType());
		
		initDeclaratorList.generateCode();
	}
}
