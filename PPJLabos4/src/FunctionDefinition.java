import java.util.List;


public class FunctionDefinition extends TreeNode implements ICheckable, IGeneratable {

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
			
			String functionType = "f(void->" + typeName.getType() + ")";
			
			if (Checker.isNameDeclaredLocaly(functionName)) {
				Checker.throwException(Checker.getTypeForName(functionName).equals(functionType), errorMessage);;
			}
			Checker.throwException(!Checker.isFunctionDefined(functionName), errorMessage);
			
			if (Checker.isFunctionDeclaredGlobaly(functionName)) {
				Checker.throwException(Checker.checkGlobalDeclaration(functionName, functionType), errorMessage);
			}
			Scope.currentScope.addFunction(functionName, functionType, true);
			Checker.setFunctionDefinition(functionName, functionType, Scope.currentScope);
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope, "F_" + functionName.toUpperCase());
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
			Checker.throwException(!Checker.isFunctionDefined(functionName), errorMessage);
			
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
			Checker.setFunctionDefinition(functionName, functionType, Scope.currentScope);
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope, "F_" + functionName.toUpperCase());
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
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.get(3).getData().getName().equals("KR_VOID")) {
			// <ime_tipa> IDN L_ZAGRADA KR_VOID D_ZAGRADA <slozena_naredba>
			TypeName typeName = (TypeName) children.get(0);
			
			typeName.generateCode();
			
			String functionName = ((TerminalSignData) children.get(1).getData()).getValue();
			String functionType = "f(void->" + typeName.getType() + ")";
			Scope.currentScope.addFunction(functionName, functionType, true);
			Checker.setFunctionDefinition(functionName, functionType, Scope.currentScope);
			
			functionName = functionName.toUpperCase();
			GeneratorKoda.lines.add("F_" + functionName + "\tMOVE R0, R0");
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope, "F_" + functionName.toUpperCase());
			parentScope.addChildScope(Scope.currentScope);
			Scope.currentScope.setFunction(true);
			Scope.currentScope.setFunctionType(functionType);
			
			ComplexCommand complexCommand = (ComplexCommand) children.get(5);
			
			complexCommand.generateCode();
			
			Scope.currentScope = parentScope;
		} else {
			// <ime_tipa> IDN L_ZAGRADA <lista_parametara> D_ZAGRADA <slozena_naredba>
			TypeName typeName = (TypeName) children.get(0);
			
			typeName.generateCode();
			
			String functionName = ((TerminalSignData) children.get(1).getData()).getValue();
			
			ParameterList parameterList = (ParameterList) children.get(3);
			
			parameterList.generateCode();
			
			StringBuffer functionTypeBuffer = new StringBuffer("f(");
			for (ParameterList.Parameter parameter : parameterList.getParameters()) {
				functionTypeBuffer.append(parameter.getType());
				functionTypeBuffer.append(",");
			}
			functionTypeBuffer.deleteCharAt(functionTypeBuffer.length() - 1);
			functionTypeBuffer.append("->").append(typeName.getType()).append(")");
			String functionType = functionTypeBuffer.toString();
			
			
			Scope.currentScope.addFunction(functionName, functionType, true);
			Checker.setFunctionDefinition(functionName, functionType, Scope.currentScope);
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope, "F_" + functionName.toUpperCase());
			parentScope.addChildScope(Scope.currentScope);
			Scope.currentScope.setFunction(true);
			Scope.currentScope.setFunctionType(functionType);
			
			functionName = functionName.toUpperCase();
			GeneratorKoda.lines.add("F_" + functionName + "\tPOP R5");
			
			for (ParameterList.Parameter parameter : parameterList.getParameters()) {
				Scope.currentScope.addIdentificator(parameter.getName(), parameter.getType(), true);
			}
			
			for (int i = parameterList.getParameters().size()-1, end = 0 ; i >= end; i--) {
				GeneratorKoda.lines.add("\tPOP R0");
				String label = Scope.currentScope.getScopeName().toUpperCase() + "_" 
									+ parameterList.getParameters().get(i).getName().toUpperCase();
				GeneratorKoda.lines.add("\tSTORE R0, (" + label + ")");
			}
			
			GeneratorKoda.lines.add("\tPUSH R5");
			ComplexCommand complexCommand = (ComplexCommand) children.get(5);
			
			complexCommand.generateCode();
			
			Scope.currentScope = parentScope;
		}
	}
}
