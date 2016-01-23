import java.util.List;


public class InitDeclarator extends TreeNode implements ICheckable, IGeneratable {

	private String nType;
	
	public InitDeclarator(TreeNodeData data) {
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
			// <izravni_deklarator>
			String errorMessage = "<init_deklarator> ::= <izravni_deklarator>";
			
			DirectDeclarator directDeclarator = (DirectDeclarator) children.get(0);
			directDeclarator.setnType(nType);
			
			directDeclarator.check();
			
			Checker.throwException(!Checker.isConstantType(directDeclarator.getType()), errorMessage);
		} else {
			// <izravni_deklarator> OP_PRIDRUZI <inicijalizator>
			String errorMessage = "<init_deklarator> ::= <izravni_deklarator> " + children.get(1) + " <inicijalizator>";
			
			DirectDeclarator directDeclarator = (DirectDeclarator) children.get(0);
			directDeclarator.setnType(nType);
			
			directDeclarator.check();
			
			Initialisator initialisator = (Initialisator) children.get(2);
			
			initialisator.check();
			
			String type = Checker.getNumberTypeFromConstantOrArray(directDeclarator.getType());
			if (Checker.isNumber(directDeclarator.getType())) {
				Checker.throwException(Checker.checkTildaOperator(initialisator.getType(), type), errorMessage);
			} else if (Checker.isNumberArray(directDeclarator.getType())) {
				Checker.throwException(initialisator.getType() == null, errorMessage);
				Checker.throwException(initialisator.getTypes().size() <= directDeclarator.getElementCount(), errorMessage);
				for (String initType : initialisator.getTypes()) {
					Checker.throwException(Checker.checkTildaOperator(initType, type), errorMessage);
				}
			} else {
				Checker.throwException(false, errorMessage);
			}
		}

	}
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// <izravni_deklarator>
			DirectDeclarator directDeclarator = (DirectDeclarator) children.get(0);
			directDeclarator.setnType(nType);
			
			directDeclarator.check();
		} else {
			// <izravni_deklarator> OP_PRIDRUZI <inicijalizator>
			DirectDeclarator directDeclarator = (DirectDeclarator) children.get(0);
			directDeclarator.setnType(nType);
			
			directDeclarator.generateCode();
			
			Initialisator initialisator = (Initialisator) children.get(2);
			
			initialisator.generateCode();
			
			String name = DirectDeclarator.lastName;
			if (name != null) {
				Scope.IdentificatorData identificator = Scope.currentScope.getIdentificator(name);
				identificator.setDefaultValue(Initialisator.value);
				
				DirectDeclarator.lastName = null;
			}
		}
	}

}
