import java.util.List;


public class TranslationUnit extends TreeNode implements ICheckable, IGeneratable {

	public TranslationUnit(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <vanjska_deklaracija>
			
			OuterDeclaration outerDeclaration = (OuterDeclaration) children.get(0);
			
			outerDeclaration.check();
		} else {
			// <prijevodna_jedinica> <vanjska_deklaracija>
			
			TranslationUnit translationUnit = (TranslationUnit) children.get(0);
			
			translationUnit.check();
			
			OuterDeclaration outerDeclaration = (OuterDeclaration) children.get(1);
			
			outerDeclaration.check();
		}
		
	}
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <vanjska_deklaracija>
			
			OuterDeclaration outerDeclaration = (OuterDeclaration) children.get(0);
			
			outerDeclaration.generateCode();
		} else {
			// <prijevodna_jedinica> <vanjska_deklaracija>
			
			TranslationUnit translationUnit = (TranslationUnit) children.get(0);
			
			translationUnit.generateCode();
			
			OuterDeclaration outerDeclaration = (OuterDeclaration) children.get(1);
			
			outerDeclaration.generateCode();
		}
	}
}
