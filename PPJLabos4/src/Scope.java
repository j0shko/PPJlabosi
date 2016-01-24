import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scope {
	public static Scope currentScope;
	public static Scope globalScope;
	
	public static boolean hasIndex = false;
	public static long index = 0L;
	
	private boolean isFunction;
	private boolean isLoop;
	
	private String functionType;
	
	private String scopeName;
	
	private Scope parentScope;
	private List<Scope> childScopes = new ArrayList<>();
	
	private Map<String, IdentificatorData> identificatorMap = new HashMap<>();
	private Map<String, FunctionData> functionMap = new HashMap<>();
	
	public class IdentificatorData {
		private String name;
		private String type;
		private boolean lExpression;
		
		private String defaultValue = "0";
		private String label;
		
		private List<String> array = new ArrayList<>();
		private long size = 0L;
		
		public IdentificatorData(String name, String type, boolean lExpression, String label) {
			this.name = name;
			this.type = type;
			this.lExpression = lExpression;
			this.label = label;
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
		
		public void setDefaultValue(String value) {
			this.defaultValue = value;
		}
		
		public String getDefaultValue() {
			return defaultValue;
		}
		
		public long getSize() {
			return size;
		}
		
		public void setSize(long size) {
			this.size = size;
			if (this.array.size() < size) {
				long difference = size - array.size();
				for (int i = 0; i < difference; i++) {
					array.add("0");
				}
			}
		}
		
		public void setArray(List<String> array) {
			this.array = new ArrayList<>(array);
		}
		
		public List<String> getArray() {
			return array;
		}
		
		public String getLabel() {
			return label;
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
	
	public Scope(Scope parentScope, String scopeName) {
		isFunction = false;
		isLoop = false;
		this.scopeName = scopeName; 
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
		String label = this.scopeName.toUpperCase() + "_" + name.toUpperCase();
		identificatorMap.put(name, new IdentificatorData(name, type, lExpression, label));
	}
	
	public boolean checkIfAllFunctionsAreDefined() {
		for (FunctionData function : functionMap.values()) {
			if (!function.isDefined) {
				return false;
			}
		} 
		
		boolean defined = true;
		for (Scope subScopes : childScopes) {
			defined = defined && subScopes.checkIfAllFunctionsAreDefined();
		}
		return defined;
	}
	
	public String getScopeName() {
		return scopeName;
	}
	
	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}
	
	public void generateDefinitionLines() {
		for (IdentificatorData identificator : identificatorMap.values()) {
			if (identificator.getArray().size() <=1) {
				int value = Integer.parseInt(identificator.defaultValue);
				GeneratorKoda.lines.add(identificator.label + "\t" + "DW %D " + value);
			} else {
				String label = new String(identificator.label);
				for (String arrayValue : identificator.getArray()) {
					int value = Integer.parseInt(arrayValue);
					GeneratorKoda.lines.add(label + "\t" + "DW %D " + value);
					label = "";
				}
				if (identificator.getArray().size() < identificator.getSize()) {
					for (int i = 0; i < identificator.getSize() - identificator.getArray().size(); ++i) {
						GeneratorKoda.lines.add("\t" + "DW 0");
					}
				}
			}
		}
		if (!childScopes.isEmpty()) {
			for (Scope scope : childScopes) {
				scope.generateDefinitionLines();
			}
		}
	}
}
