import java.util.List;

public class DirectDeclarator extends TreeNode implements ICheckable, IGeneratable {

	private String nType;
	private String type;
	
	public static String lastName = null;
	
	private int elementCount;

	public DirectDeclarator(TreeNodeData data) {
		super(data);
	}
	
	public void setnType(String nType) {
		this.nType = nType;
	}
	
	public String getnType() {
		return nType;
	}
	
	public String getType() {
		return type;
	}
	
	public int getElementCount() {
		return elementCount;
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// IDN
			String errorMessage = "<izravni_deklarator> ::= " + children.get(0);
			
			Checker.throwException(!nType.equals("void"), errorMessage);
			
			String name = ((TerminalSignData) children.get(0).getData()).getValue();
			Checker.throwException(!Checker.isNameDeclaredLocaly(name), errorMessage);
			
			boolean lExpression = Checker.canBeLExpression(nType);
			Scope.currentScope.addIdentificator(name, nType, lExpression);
			
			type = nType;
		} else {
			if (children.get(1).getData().getName().equals("L_UGL_ZAGRADA")) {
				// IDN L_UGL_ZAGRADA BROJ D_UGL_ZAGRADA
				String errorMessage = "<izravni_deklarator> ::= " + children.get(0) + " " + children.get(1)
										+ " " + children.get(2) + " " + children.get(3);
					
				Checker.throwException(!nType.equals("void"), errorMessage);
						
				String name = ((TerminalSignData) children.get(0).getData()).getValue();
				Checker.throwException(!Checker.isNameDeclaredLocaly(name), errorMessage);
				
				String sizeNumber = ((TerminalSignData) children.get(2).getData()).getValue();
				Checker.throwException(Checker.checkArraySizeInteger(sizeNumber), errorMessage);
				
				elementCount = Checker.getNumberValue(sizeNumber);
				
				this.type = nType + "[]";
				Scope.currentScope.addIdentificator(name, type, false);
			} else if (children.get(2).getData().getName().equals("KR_VOID")) {
				// IDN L_ZAGRADA KR_VOID D_ZAGRADA
				String errorMessage = "<izravni_deklarator> ::= " + children.get(0) + " " + children.get(1)
										+ " " + children.get(2) + " " + children.get(3);
				
				String name = ((TerminalSignData) children.get(0).getData()).getValue();
				this.type = "f(void->" + nType + ")";
				if (Checker.isFunctionDeclaredLocaly(name)) {
					Checker.throwException(Checker.getFunction(name).getType().equals(type), errorMessage);
				} else {
					boolean defined = Checker.isFunctionDefined(name);
					Scope.currentScope.addFunction(name, type, defined);
				}
			} else {
				// IDN L_ZAGRADA <lista_parametara> D_ZAGRADA
				String errorMessage = "<izravni_deklarator> ::= " + children.get(0) + " " + children.get(1)
											+ " <lista_parametara> " + children.get(3);
				
				ParameterList parameterList = (ParameterList) children.get(2);
				
				parameterList.check();
				
				String name = ((TerminalSignData) children.get(0).getData()).getValue();
				StringBuffer typeBuffer = new StringBuffer("f(");
				for (ParameterList.Parameter parameter : parameterList.getParameters()) {
					typeBuffer.append(parameter.getType() + ",");
				}
				typeBuffer.deleteCharAt(typeBuffer.length() - 1);
				typeBuffer.append("->").append(nType).append(")");
				this.type = typeBuffer.toString();
				
				if (Checker.isFunctionDeclaredLocaly(name)) {
					Checker.throwException(Checker.getFunction(name).getType().equals(type), errorMessage);
				} else {
					boolean defined = Checker.isFunctionDefined(name);
					Scope.currentScope.addFunction(name, type, defined);
				}
			}
		}
	}

	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// IDN
			String name = ((TerminalSignData) children.get(0).getData()).getValue();
			
			boolean lExpression = Checker.canBeLExpression(nType);
			Scope.currentScope.addIdentificator(name, nType, lExpression);
			
			lastName = name;
			
			type = nType;
		} else {
			if (children.get(1).getData().getName().equals("L_UGL_ZAGRADA")) {
				// IDN L_UGL_ZAGRADA BROJ D_UGL_ZAGRADA
				String errorMessage = "<izravni_deklarator> ::= " + children.get(0) + " " + children.get(1)
										+ " " + children.get(2) + " " + children.get(3);
					
				Checker.throwException(!nType.equals("void"), errorMessage);
						
				String name = ((TerminalSignData) children.get(0).getData()).getValue();
				Checker.throwException(!Checker.isNameDeclaredLocaly(name), errorMessage);
				
				String sizeNumber = ((TerminalSignData) children.get(2).getData()).getValue();
				Checker.throwException(Checker.checkArraySizeInteger(sizeNumber), errorMessage);
				
				elementCount = Checker.getNumberValue(sizeNumber);
				
				this.type = nType + "[]";
				Scope.currentScope.addIdentificator(name, type, false);
			} else if (children.get(2).getData().getName().equals("KR_VOID")) {
				// IDN L_ZAGRADA KR_VOID D_ZAGRADA
				String errorMessage = "<izravni_deklarator> ::= " + children.get(0) + " " + children.get(1)
										+ " " + children.get(2) + " " + children.get(3);
				
				String name = ((TerminalSignData) children.get(0).getData()).getValue();
				this.type = "f(void->" + nType + ")";
				if (Checker.isFunctionDeclaredLocaly(name)) {
					Checker.throwException(Checker.getFunction(name).getType().equals(type), errorMessage);
				} else {
					boolean defined = Checker.isFunctionDefined(name);
					Scope.currentScope.addFunction(name, type, defined);
				}
			} else {
				// IDN L_ZAGRADA <lista_parametara> D_ZAGRADA
				String errorMessage = "<izravni_deklarator> ::= " + children.get(0) + " " + children.get(1)
											+ " <lista_parametara> " + children.get(3);
				
				ParameterList parameterList = (ParameterList) children.get(2);
				
				parameterList.check();
				
				String name = ((TerminalSignData) children.get(0).getData()).getValue();
				StringBuffer typeBuffer = new StringBuffer("f(");
				for (ParameterList.Parameter parameter : parameterList.getParameters()) {
					typeBuffer.append(parameter.getType() + ",");
				}
				typeBuffer.deleteCharAt(typeBuffer.length() - 1);
				typeBuffer.append("->").append(nType).append(")");
				this.type = typeBuffer.toString();
				
				if (Checker.isFunctionDeclaredLocaly(name)) {
					Checker.throwException(Checker.getFunction(name).getType().equals(type), errorMessage);
				} else {
					boolean defined = Checker.isFunctionDefined(name);
					Scope.currentScope.addFunction(name, type, defined);
				}
			}
		}
	}
}
