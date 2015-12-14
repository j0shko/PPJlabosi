import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GenerativeTree {

	public static class TreeNode {
		private List<TreeNode> children = new ArrayList<>();
		
		private TreeNodeData data;
		
		public TreeNode(TreeNodeData data) {
			this.data = data;
		}
		
		public TreeNodeData getData() {
			return data;
		}
		
		public void addChild(TreeNode child) {
			children.add(child);
		}
		
		public List<TreeNode> getChildren() {
			return children;
		}
		
		@Override
		public String toString() {
			return data.toString();
		}
	}
	
	private TreeNode root;
	
	public GenerativeTree(TreeNode root) {
		this.root = root;
	}
	
	public GenerativeTree(String text) {
		this(createFromText(splitByLines(text), 0));
	}	
	
	public TreeNode getRoot() {
		return root;
	}
	
	public void printTree() {
		printTreeRecursive(root, 0);
	}
	
	private void printTreeRecursive(TreeNode root, int spaceCount) {
		//print spaces
		for (int i = 0; i < spaceCount; i++) {
			System.out.print(" ");
		}
		System.out.println(root);
		for (TreeNode node : root.children) {
			printTreeRecursive(node, spaceCount + 1);
		}
	}

	private static TreeNode createFromText(List<String> lines, int spaceCount) {
		String currentLine = lines.get(0);
		currentLine = currentLine.trim();
		
		// create node for first line data
		TreeNodeData newNodeData;
		if (currentLine.startsWith("<")) {
			newNodeData = new NonTerminalSignData(currentLine);
		} else {
			String[] lineStuff = currentLine.split(" ");
			newNodeData = new TerminalSignData(lineStuff[0], Integer.parseInt(lineStuff[1]), lineStuff[2]);
		}
		TreeNode currentNode = new TreeNode(newNodeData); 
		
		// call recursively for all lines with exactly one more space than first line (current node)
		int start = 1;
		int end = 2;
		for (int i = 1, stop = lines.size(); i < stop; i++) {
			if (countLeadingSpaces(lines.get(i)) == spaceCount + 1) {
				end = i;
				if (end > start && end <= lines.size()) {
					currentNode.addChild(createFromText(lines.subList(start, end), spaceCount + 1));
				}
				start = i;
			}
		}
		
		// don't try to call if current list is only one line long
		if (lines.size() > 1) {
			currentNode.addChild(createFromText(lines.subList(start, lines.size()), spaceCount + 1));
		}
		
		return currentNode;
	}
	
	
	private static int countLeadingSpaces(String line) {
		int count = 0;
		for (int i = 0, end = line.length(); i < end; i++) {
			if (line.charAt(i) == ' ') {
				count++;
			} else {
				break;
			}
		}
		
		return count;
	}
	
	private static List<String> splitByLines(String text) {
		return new ArrayList<>(Arrays.asList(text.split("\n")));
	}
}
