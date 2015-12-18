import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Scope {
	public static Scope currentScope;
	public static Scope globalScope;
	
	private boolean isFunction;
	private boolean isLoop;
	
	private String functionType;
	
	private Scope parentScope;
	private List<Scope> childScopes = new ArrayList<>();
	
	private Map<String, IdentificatorData> identificatorMap = new HashMap<>();
	private Map<String, FunctionData> functionMap = new HashMap<>();
	
	public class IdentificatorData {
		private String name;
		private String type;
		private boolean lExpression;
		
		public IdentificatorData(String name, String type, boolean lExpression) {
			this.name = name;
			this.type = type;
			this.lExpression = lExpression;
		}
		
		public String getName() {
			return name;
		}
		
		public String getType() {
			return type;
		}
		
		public boolean islExpression() {
			return lExpression;
		}
	}
	
	public class FunctionData {
		private String name;
		private String type;
		private boolean isDefined;
		
		public FunctionData(String name, String type, boolean isDefined) {
			this.name = name;
			this.type = type;
			this.isDefined = isDefined;
		}
		
		public String getName() {
			return name;
		}
		
		public String getType() {
			return type;
		}
		
		public boolean isDefined() {
			return isDefined;
		}
		
		public void setDefined(boolean isDefined) {
			this.isDefined = isDefined;
		}
	}
	
	public Scope(Scope parentScope) {
		isFunction = false;
		isLoop = false;
		this.parentScope = parentScope;
	}
	
	public void setFunction(boolean isFunction) {
		this.isFunction = isFunction;
	}
	
	public void setLoop(boolean isLoop) {
		this.isLoop = isLoop;
	}
	
	public boolean isFunction() {
		return isFunction;
	}
	
	public boolean isLoop() {
		return isLoop;
	}
	
	public Scope getParentScope() {
		return parentScope;
	}
	
	public List<Scope> getChildScopes() {
		return childScopes;
	}
	
	public void addChildScope(Scope scope) {
		childScopes.add(scope);
	}
	
	public String getFunctionType() {
		return functionType;
	}
	
	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}
	
	public boolean containsIdentificator(String name) {
		return identificatorMap.containsKey(name);
	}
	
	public IdentificatorData getIdentificator(String name) {
		return identificatorMap.get(name);
	}
	
	public boolean containsFunction(String name) {
		return functionMap.containsKey(name);
	}
	
	public FunctionData getFunction(String name) {
		return functionMap.get(name);
	}
	
	public void addFunction(String name, String type, boolean isDefined) {
		functionMap.put(name, new FunctionData(name, type, isDefined));
	}
	
	public void addIdentificator(String name, String type, boolean lExpression) {
		identificatorMap.put(name, new IdentificatorData(name, type, lExpression));
	}
}
