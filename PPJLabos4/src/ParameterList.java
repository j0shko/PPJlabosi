import java.util.ArrayList;
import java.util.List;

public class ParameterList extends TreeNode implements ICheckable, IGeneratable {

	private List<Parameter> parameters = new ArrayList<>();
	
	public class Parameter {
		private String name;
		private String type;
		
		public Parameter(String name, String type) {
			this.name = name;
			this.type = type;
		}
		
		public String getName() {
			return name;
		}
		
		public String getType() {
			return type;
		}
	}
	
	public ParameterList(TreeNodeData data) {
		super(data);
	}
	
	public List<Parameter> getParameters() {
		return parameters;
	}

	public void addParameter(String name, String type) {
		parameters.add(new Parameter(name, type));
	}
	
	public boolean containsParameter(String name) {
		for (Parameter parameter : parameters) {
			if (parameter.name.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <deklaracija_parametra>
			
			ParameterDeclaration parameterDeclaration = (ParameterDeclaration) children.get(0);
			
			parameterDeclaration.check();
			
			parameters.add(new Parameter(parameterDeclaration.getName(), parameterDeclaration.getType()));
		} else {
			// <lista_parametara> ZAREZ <deklaracija_parametra>
			String errorMessage = "<lista_parametara> ::= <lista_parametara> " + children.get(1) + " <deklaracija_parametra>";
			
			ParameterList parameterList = (ParameterList) children.get(0);
			
			parameterList.check();
			
			ParameterDeclaration parameterDeclaration = (ParameterDeclaration) children.get(2);
			
			parameterDeclaration.check();
			
			Checker.throwException(!parameterList.containsParameter(parameterDeclaration.getName()), errorMessage);
			
			parameters.addAll(parameterList.getParameters());
			parameters.add(new Parameter(parameterDeclaration.getName(), parameterDeclaration.getType()));
		}
	}

	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 1) {
			// <deklaracija_parametra>
			
			ParameterDeclaration parameterDeclaration = (ParameterDeclaration) children.get(0);
			
			parameterDeclaration.generateCode();
			
			parameters.add(new Parameter(parameterDeclaration.getName(), parameterDeclaration.getType()));
		} else {
			// <lista_parametara> ZAREZ <deklaracija_parametra>
			ParameterList parameterList = (ParameterList) children.get(0);
			
			parameterList.generateCode();
			
			ParameterDeclaration parameterDeclaration = (ParameterDeclaration) children.get(2);
			
			parameterDeclaration.generateCode();
			
			parameters.addAll(parameterList.getParameters());
			parameters.add(new Parameter(parameterDeclaration.getName(), parameterDeclaration.getType()));
		}
	}
}
