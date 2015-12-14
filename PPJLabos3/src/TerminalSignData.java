public class TerminalSignData implements TreeNodeData {

	private String name;
	private int lineNum;
	private String value;
	
	public TerminalSignData(String name, int lineNum, String value) {
		this.name = name;
		this.lineNum = lineNum;
		this.value = value;
	}

	@Override
	public String getData() {
		return name;
	}

	public int getLineNum() {
		return lineNum;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return name + " " + lineNum + " " + value;
	}
}
