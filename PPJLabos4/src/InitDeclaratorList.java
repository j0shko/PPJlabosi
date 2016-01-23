import java.util.List;


public class InitDeclaratorList extends TreeNode implements ICheckable, IGeneratable {

	private String nType;
	
	public InitDeclaratorList(TreeNodeData data) {
		super(data);
	}
	
	public void setnType(String nType) {
		this.nType = nType;
	}
	
	public String getnType() {
		return nType;
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// <init_deklarator>
			
			InitDeclarator initDeclarator = (InitDeclarator) children.get(0);
			initDeclarator.setnType(nType);
			
			initDeclarator.check();
		} else {
			// <lista_init_deklaratora>2 ZAREZ <init_deklarator>
			
			InitDeclaratorList initDeclaratorList = (InitDeclaratorList) children.get(0);
			initDeclaratorList.setnType(nType);
			
			initDeclaratorList.check();
			
			InitDeclarator initDeclarator = (InitDeclarator) children.get(2);
			initDeclarator.setnType(nType);
			
			initDeclarator.check();
		}
	}
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// <init_deklarator>
			
			InitDeclarator initDeclarator = (InitDeclarator) children.get(0);
			initDeclarator.setnType(nType);
			
			initDeclarator.generateCode();
		} else {
			// <lista_init_deklaratora>2 ZAREZ <init_deklarator>
			
			InitDeclaratorList initDeclaratorList = (InitDeclaratorList) children.get(0);
			initDeclaratorList.setnType(nType);
			
			initDeclaratorList.generateCode();
			
			InitDeclarator initDeclarator = (InitDeclarator) children.get(2);
			initDeclarator.setnType(nType);
			
			initDeclarator.generateCode();
		}
	}
}
