import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GenerativeTree {

	
	
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
		for (TreeNode node : root.getChildren()) {
			printTreeRecursive(node, spaceCount + 1);
		}
	}

	private static TreeNode createFromText(List<String> lines, int spaceCount) {
		String currentLine = lines.get(0);
		currentLine = currentLine.trim();
		
		// create node for first line data
		TreeNode currentNode = getNodeForLine(currentLine);
		
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
	
	private static TreeNode getNodeForLine(String line) {
		TreeNodeData data;
		if (line.startsWith("<")) {
			data = new NonTerminalSignData(line);
		} else {
			String[] lineStuff = line.split(" ");
			String name = lineStuff[0];
			int lineNum = Integer.parseInt(lineStuff[1]);
			String value = "";
			for (int i = 2; i < lineStuff.length; i++) {
				value += lineStuff[i] + " ";
			}
			value = value.substring(0, value.length() - 1);
			data = new TerminalSignData(name, lineNum, value);
		}
		switch (line) {
		case "<primarni_izraz>" : return new PrimaryExpression(data);
		case "<postfiks_izraz>" : return new PostfixExpression(data);
		case "<lista_argumenata>" : return new ArgumentList(data);
		case "<unarni_izraz>" : return new UnaryExpression(data);
		case "<unarni_operator>" : return new UnaryOperator(data);
		case "<cast_izraz>" : return new CastExpression(data);
		case "<ime_tipa>" : return new TypeName(data);
		case "<specifikator_tipa>" : return new TypeSpecificator(data);
		case "<multiplikativni_izraz>" : return new MultiplicativeExpression(data);
		case "<aditivni_izraz>" : return new AditiveExpression(data);
		case "<odnosni_izraz>" : return new RelationsExpression(data);
		case "<jednakosni_izraz>" : return new EqualsExpression(data);
		case "<bin_i_izraz>" : return new BinaryAndExpression(data);
		case "<bin_xili_izraz>" : return new BinaryXorExpression(data);
		case "<bin_ili_izraz>" : return new BinaryOrExpression(data);
		case "<log_i_izraz>" : return new LogicalAndExpression(data);
		case "<log_ili_izraz>" : return new LogicalOrExpression(data);
		case "<izraz_pridruzivanja>" : return new AssignmentExpression(data);
		case "<izraz>" : return new Expression(data);
		case "<slozena_naredba>" : return new ComplexCommand(data);
		case "<lista_naredbi>" : return new CommandList(data);
		case "<naredba>" : return new Command(data);
		case "<izraz_naredba>" : return new ExpressionCommand(data);
		case "<naredba_grananja>" : return new BranchCommand(data);
		case "<naredba_petlje>" : return new LoopCommand(data);
		case "<naredba_skoka>" : return new JumpCommand(data);
		case "<prijevodna_jedinica>" : return new TranslationUnit(data);
		case "<vanjska_deklaracija>" : return new OuterDeclaration(data);
		case "<definicija_funkcije>" : return new FunctionDefinition(data);
		case "<lista_parametara>" : return new ParameterList(data);
		case "<deklaracija_parametra>" : return new ParameterDeclaration(data);
		case "<lista_deklaracija>" : return new DeclarationList(data);
		case "<deklaracija>": return new Declaration(data);
		case "<lista_init_deklaratora>" : return new InitDeclaratorList(data);
		case "<init_deklarator>" : return new InitDeclarator(data);
		case "<izravni_deklarator>" : return new DirectDeclarator(data);
		case "<inicijalizator>" : return new Initialisator(data);
		case "<lista_izraza_pridruzivanja>" : return new AssignmentExpressionList(data);
		default: return new TreeNode(data);
		}
	}
}
