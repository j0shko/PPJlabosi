public class NonTerminalSignData implements TreeNodeData {

	private String name;
	
	public NonTerminalSignData(String name) {
		this.name = name;
	}
	
	@Override
	public String getData() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
