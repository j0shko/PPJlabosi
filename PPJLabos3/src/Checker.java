import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Checker {
	
	private static List<String> LEGAL_CONST_CHARS = Arrays.asList("\\t", "\\n", "\\0", "\\'", "\\\"", "\\\\");
	
	private static Map<String, List<String>> TILDA = new HashMap<>();
	private static Map<String, List<String>> CAST = new HashMap<>();
	static {
		// valid right side for ~ operator
		TILDA.put("char", Arrays.asList("char", "int", "const(char)", "const(int)"));
		TILDA.put("int", Arrays.asList("int", "const(int)"));
		TILDA.put("const(char)", Arrays.asList("const(char)", "const(int)", "char", "int"));
		TILDA.put("const(int)", Arrays.asList("const(int)", "int"));
		TILDA.put("void", Arrays.asList("void"));
		TILDA.put("int[]", Arrays.asList("int[]", "const(int)[]"));
		TILDA.put("char[]", Arrays.asList("char[]", "const(char)[]"));
		TILDA.put("const(int)[]", Arrays.asList("const(int)[]", "int[]"));
		TILDA.put("const(char)[]", Arrays.asList("const(char)[]", "char[]"));
		
		CAST.put("char", Arrays.asList("char", "int", "const(char)", "const(int)"));
		CAST.put("int", Arrays.asList("char", "int", "const(char)", "const(int)"));
		CAST.put("const(int)", Arrays.asList("char", "int", "const(char)", "const(int)"));
		CAST.put("const(char)", Arrays.asList("char", "int", "const(char)", "const(int)"));
	}
	
	public static boolean checkInteger(String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}
	
	public static boolean checkArraySizeInteger(String value) {
		if (checkInteger(value)) {
			int num = Integer.parseInt(value);
			return num > 0 && num <= 1024;
		} else {
			return false;
		}
	}
	
	public static boolean checkChar(String value) {
		if (!(value.startsWith("'") && value.endsWith("'"))) {
			return false;
		}
		
		String val = value.substring(1, value.length() - 1);
		if (LEGAL_CONST_CHARS.contains(val)) {
			return true;
		}
		if (val.length() == 1) {
			return true;
		}
		return false;
	}
	
	public static boolean checkString(String value) {
		if (!(value.startsWith("\"") && value.endsWith("\""))) {
			return false;
		}
		String val = value.substring(1, value.length() - 1);
		for (int i = 0; i < val.length(); ++i) {
			char c = val.charAt(i);
			if (c == '\\') {
				String temp = val.substring(i, i + 2);
				if (!LEGAL_CONST_CHARS.contains(temp)) {
					return false;
				}
				i++;
			} 
		}
		return true;
	}
	
	// ------------------- type checks ----------------------
	
	public static boolean checkTildaOperator(String type1, String type2) {
		if (TILDA.containsKey(type1)) {
			return TILDA.get(type1).contains(type2);
		} else {
			return false;
		}
	}
	
	public static boolean isArrayType(String type) {
		return type.endsWith("[]");
	}
	
	public static boolean isConstantType(String type) {
		return type.startsWith("const");
	}
	
	public static boolean isNumber(String type) {
		return type == "char" || type == "int" || type == "const(char)" || type == "const(int)";
	}
	
	public static String getNumberTypeFromConstantOrArray(String type) {
		if (isArrayType(type)) {
			type = type.substring(0, type.length() - 2);
		}
		if (isConstantType(type)) {
			type = type.substring(6, type.length() -1);
		}
			
		return type;
	}
	
	public static boolean isNumberArray(String type) {
		return type == "char[]" || type == "int[]" || type == "const(char)[]" || type == "const(int)[]";
	}
	
	public static String getArrayType(String type) {
		if (isArrayType(type)) {
			return type.substring(0, type.length()-2);
		} else {
			throw new IllegalArgumentException("Given type is not array");
		}
	}
	
	// function must be in saved as f(arg1,arg2,arg3->returnValue)
	
	public static boolean isVoidFunction(String type) {
		return getFunctionReturnValue(type).equals("void");
	}
	
	public static boolean isFunctionWithParams(String functionType) {
		List<String> params = getFunctionParameters(functionType);
		return params.get(0) != "void";
	}
	
	public static String getFunctionReturnValue(String type) {
		if (isFunction(type)) {
			//remove f and ()
			type = type.substring(2, type.length()-1);
			String[] argsAndReturn = type.split("->");
			return argsAndReturn[1].trim();
		} else {
			throw new IllegalArgumentException("Argument is not a function.");
		}
	}
	
	public static List<String> getFunctionParameters(String type) {
		if (isFunction(type)) {
			//remove f and ()
			type = type.substring(2, type.length()-1);
			String[] argsAndReturn = type.split("->");
			String[] args = argsAndReturn[0].split(",");
			return Arrays.asList(args);
		} else {
			throw new IllegalArgumentException("Argument is not a function.");
		}
	}
	
	public static boolean isFunction(String type) {
		return type.startsWith("f(");
	}
	
	public static void throwException(boolean statement, String message) {
		if (!statement) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static boolean isCastable(String type1, String type2) {
		if (CAST.containsKey(type1)) {
			return CAST.get(type1).contains(type2);
		} else {
			return false;
		}
	}
	
	// ------------------------ Scope orienteded checks ----------------------------
	
	public static boolean isIdentificatorDeclared(String name) {
		Scope currentScope = Scope.currentScope;
		while (currentScope != null) {
			if (currentScope.containsIdentificator(name)) {
				return true;
			}
			currentScope = currentScope.getParentScope();
		}
		return false;
	}
	
	public static boolean isIdentificatorDeclaredLocaly(String name) {
		return Scope.currentScope.containsIdentificator(name);
	}
	
	public static Scope.IdentificatorData getIdentificator(String name) {
		Scope currentScope = Scope.currentScope;
		while (currentScope != null) {
			if (currentScope.containsIdentificator(name)) {
				return currentScope.getIdentificator(name);
			}
			currentScope = currentScope.getParentScope();
		}
		return null;
	}
	
	public static boolean isFunctionDeclared(String name) {
		Scope currentScope = Scope.currentScope;
		while (currentScope != null) {
			if (currentScope.containsFunction(name)) {
				return true;
			}
			currentScope = currentScope.getParentScope();
		}
		return false;
	}
	
	public static boolean isFunctionDefined(String name) {
		Scope currentScope = Scope.currentScope;
		while (currentScope != null) {
			if (currentScope.containsFunction(name) && currentScope.getFunction(name).isDefined()) {
				return true;
			}
			currentScope = currentScope.getParentScope();
		}
		return false;
	}
	
	public static Scope.FunctionData getFunction(String name) {
		Scope currentScope = Scope.currentScope;
		while (currentScope != null) {
			if (currentScope.containsFunction(name)) {
				return currentScope.getFunction(name);
			}
			currentScope = currentScope.getParentScope();
		}
		return null;
	}
	
	public static boolean isFunctionDeclaredGlobaly(String name) {
		return Scope.globalScope.containsFunction(name);
	}
	
	public static boolean isFunctionDeclaredLocaly(String name) {
		return Scope.currentScope.containsFunction(name);
	}
	
	public static boolean checkGlobalDeclaration(String name, String type) {
		if (Scope.globalScope.containsFunction(name)) {
			return Scope.globalScope.getFunction(name).getType().equals(type);
		} else {
			return false;
		}
	}
	
	public static boolean isInsideLoop() {
		Scope currentScope = Scope.currentScope;
		while (currentScope != null) {
			if (currentScope.isLoop()) {
				return true;
			}
			currentScope = currentScope.getParentScope();
		}
		return false;
	}
	
	public static boolean isInsideVoidFunction() {
		Scope currentScope = Scope.currentScope;
		boolean isFunction = false;
		while (currentScope != null) {
			if (currentScope.isFunction()) {
				isFunction = true;
				break;
			}
			currentScope = currentScope.getParentScope();
		}
		if (isFunction) {
			return isVoidFunction(currentScope.getFunctionType());
		} else {
			return false;
		}
	}
	
	public static String getReturnValueOfScopeFunction() {
		Scope currentScope = Scope.currentScope;
		boolean isFunction = false;
		while (currentScope != null) {
			if (currentScope.isFunction()) {
				isFunction = true;
				break;
			}
			currentScope = currentScope.getParentScope();
		}
		if (isFunction) {
			return getFunctionReturnValue(currentScope.getFunctionType());
		} else {
			return null;
		}
	}
	
	public static boolean isInsideNonVoidFunction() {
		String type = getReturnValueOfScopeFunction();
		return type != null && !type.equals("void");
	}
}