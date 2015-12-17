import java.util.List;


public class LoopCommand extends TreeNode implements ICheckable {

	public LoopCommand(TreeNodeData data) {
		super(data);
	}

	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		if (children.size() == 5) {
			// KR_WHILE L_ZAGRADA <izraz> D_ZAGRADA <naredba>
			String errorString = "<naredba_petlje> ::= " + children.get(0) + " " + children.get(1)
									+ " <izraz> " + children.get(3) + " <naredba>";
			
			Expression expression = (Expression) children.get(2);
			
			expression.check();
			Checker.throwException(Checker.checkTildaOperator(expression.getType(), "int"), errorString);
			
			// TODO možda bi vamo trebalo dodati dodavanje konteksta za djecu
			
			Command command = (Command) children.get(4);
			
			command.check();
		} else if (children.size() == 6) {
			// KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 D_ZAGRADA <naredba>
			String errorString = "<naredba_petlje> ::= " + children.get(0) + " " + children.get(1)
									+ "<izraz_naredba> <izraz_naredba> " + children.get(4) + " <naredba>";
			
			ExpressionCommand expressionCommand1 = (ExpressionCommand) children.get(2);
			
			expressionCommand1.check();
			
			ExpressionCommand expressionCommand2 = (ExpressionCommand) children.get(3);
			
			expressionCommand2.check();
			Checker.throwException(Checker.checkTildaOperator(expressionCommand2.getType(), "int"), errorString);
			
			// TODO možda bi vamo trebalo dodati dodavanje konteksta za djecu
			Command command = (Command) children.get(4);
			
			command.check();
		} else if (children.size() == 7) {
			// KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 <izraz> D_ZAGRADA <naredba>
			String errorString = "<naredba_petlje> ::= " + children.get(0) + " " + children.get(1)
									+ "<izraz_naredba> <izraz_naredba> <izraz> " + children.get(5) + " <naredba>";
			
			ExpressionCommand expressionCommand1 = (ExpressionCommand) children.get(2);
			
			expressionCommand1.check();
			
			ExpressionCommand expressionCommand2 = (ExpressionCommand) children.get(3);
			
			expressionCommand2.check();
			Checker.throwException(Checker.checkTildaOperator(expressionCommand2.getType(), "int"), errorString);
			
			Expression expression = (Expression) children.get(4);
			
			expression.check();
			
			// TODO možda bi vamo trebalo dodati dodavanje konteksta za djecu
			Command command = (Command) children.get(4);
						
			command.check();
		}
	}

}
