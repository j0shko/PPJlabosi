import java.util.List;


public class FunctionDefinition extends TreeNode implements ICheckable {

	public FunctionDefinition(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		if (children.get(3).getData().getName().equals("KR_VOID")) {
			// <ime_tipa> IDN L_ZAGRADA KR_VOID D_ZAGRADA <slozena_naredba>
			String errorMessage = "<definicija_funkcije> ::= <ime_tipa> " + children.get(1) + " " 
									+ children.get(2) + " " + children.get(3) + " " + children.get(4) 
									+ " <slozena_naredba>";
			
			TypeName typeName = (TypeName) children.get(0);
			
			typeName.check();
			Checker.throwException(!Checker.isConstantType(typeName.getType()), errorMessage);
			
			String functionName = ((TerminalSignData) children.get(1).getData()).getValue();
			Checker.throwException(Checker.isFunctionDefined(functionName), errorMessage);
			
			String functionType = "f(void->" + typeName.getType() + ")";
			if (Checker.isFunctionDeclaredGlobaly(functionName)) {
				Checker.throwException(Checker.checkGlobalDeclaration(functionName, functionType), errorMessage);
			}
			Scope.currentScope.addFunction(functionName, functionType, true);
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope);
			parentScope.addChildScope(Scope.currentScope);
			Scope.currentScope.setFunction(true);
			Scope.currentScope.setFunctionType(functionType);
			
			ComplexCommand complexCommand = (ComplexCommand) children.get(5);
			
			complexCommand.check();
			
			Scope.currentScope = parentScope;
		} else {
			// <ime_tipa> IDN L_ZAGRADA <lista_parametara> D_ZAGRADA <slozena_naredba>
			String errorMessage = "<definicija_funkcije> ::= <ime_tipa> " + children.get(1) + " " 
					+ children.get(2) + " <lista_parametara> " + children.get(4) + " <slozena_naredba>";
			
			TypeName typeName = (TypeName) children.get(0);
			
			typeName.check();
			Checker.throwException(!Checker.isConstantType(typeName.getType()), errorMessage);
			
			String functionName = ((TerminalSignData) children.get(1).getData()).getValue();
			Checker.throwException(Checker.isFunctionDefined(functionName), errorMessage);
			
			ParameterList parameterList = (ParameterList) children.get(3);
			
			parameterList.check();
			
			StringBuffer functionTypeBuffer = new StringBuffer("f(");
			for (ParameterList.Parameter parameter : parameterList.getParameters()) {
				functionTypeBuffer.append(parameter.getType());
				functionTypeBuffer.append(",");
			}
			functionTypeBuffer.deleteCharAt(functionTypeBuffer.length() - 1);
			functionTypeBuffer.append("->").append(typeName.getType()).append(")");
			String functionType = functionTypeBuffer.toString();
			
			if (Checker.isFunctionDeclaredGlobaly(functionName)) {
				Checker.throwException(Checker.checkGlobalDeclaration(functionName, functionType), errorMessage);
			}
			
			Scope.currentScope.addFunction(functionName, functionType, true);
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope);
			parentScope.addChildScope(Scope.currentScope);
			Scope.currentScope.setFunction(true);
			Scope.currentScope.setFunctionType(functionType);
			
			for (ParameterList.Parameter parameter : parameterList.getParameters()) {
				Scope.currentScope.addIdentificator(parameter.getName(), parameter.getType(), true);
			}
			
			ComplexCommand complexCommand = (ComplexCommand) children.get(5);
			
			complexCommand.check();
			
			Scope.currentScope = parentScope;
		}
	}

}
