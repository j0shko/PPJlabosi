import java.util.List;


public class LoopCommand extends TreeNode implements ICheckable, IGeneratable {

	public static int loopCount = 0;
	
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
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope, "WHILE" + loopCount);
			loopCount++;
			parentScope.addChildScope(Scope.currentScope);
			Scope.currentScope.setLoop(true);
			
			Command command = (Command) children.get(4);
			
			command.check();
			
			Scope.currentScope = parentScope;
		} else if (children.size() == 6) {
			// KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 D_ZAGRADA <naredba>
			String errorString = "<naredba_petlje> ::= " + children.get(0) + " " + children.get(1)
									+ "<izraz_naredba> <izraz_naredba> " + children.get(4) + " <naredba>";
			
			ExpressionCommand expressionCommand1 = (ExpressionCommand) children.get(2);
			
			expressionCommand1.check();
			
			ExpressionCommand expressionCommand2 = (ExpressionCommand) children.get(3);
			
			expressionCommand2.check();
			Checker.throwException(Checker.checkTildaOperator(expressionCommand2.getType(), "int"), errorString);
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope, "FOR" + loopCount);
			loopCount++;
			parentScope.addChildScope(Scope.currentScope);
			Scope.currentScope.setLoop(true);
			
			Command command = (Command) children.get(5);
			
			command.check();
			
			Scope.currentScope = parentScope;
		} else if (children.size() == 7) {
			// KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 <izraz> D_ZAGRADA <naredba>
			String errorString = "<naredba_petlje> ::= " + children.get(0) + " " + children.get(1)
									+ " <izraz_naredba> <izraz_naredba> <izraz> " + children.get(5) + " <naredba>";
			
			ExpressionCommand expressionCommand1 = (ExpressionCommand) children.get(2);
			
			expressionCommand1.check();
			
			ExpressionCommand expressionCommand2 = (ExpressionCommand) children.get(3);
			
			expressionCommand2.check();
			Checker.throwException(Checker.checkTildaOperator(expressionCommand2.getType(), "int"), errorString);
			
			Expression expression = (Expression) children.get(4);
			
			expression.check();
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope, "FOR" + loopCount);
			loopCount++;
			parentScope.addChildScope(Scope.currentScope);
			Scope.currentScope.setLoop(true);
			
			Command command = (Command) children.get(6);
						
			command.check();
			
			Scope.currentScope = parentScope;
		}
	}

	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		if (children.size() == 5) {
			// KR_WHILE L_ZAGRADA <izraz> D_ZAGRADA <naredba>
			Expression expression = (Expression) children.get(2);
			
			String label = "WHILE" + loopCount;
			loopCount++;
			String labelBegin = label + "B";
			String labelEnd = label + "E";
			GeneratorKoda.lines.add(labelBegin + "\tMOVE R0, R0");
			
			PrimaryExpression.pushResult = true;
			expression.generateCode();
			PrimaryExpression.pushResult = false;
			
			GeneratorKoda.lines.add("\tPOP R0");
			GeneratorKoda.lines.add("\tCMP R0, 0");
			GeneratorKoda.lines.add("\tJP_Z " + labelEnd);
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope, label);
			loopCount++;
			parentScope.addChildScope(Scope.currentScope);
			Scope.currentScope.setLoop(true);
			
			Command command = (Command) children.get(4);
			
			command.generateCode();
			
			GeneratorKoda.lines.add("\tJP " + labelBegin);
			GeneratorKoda.lines.add(labelEnd + "\tMOVE R0, R0");
			
			Scope.currentScope = parentScope;
		} else if (children.size() == 6) {
			// KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 D_ZAGRADA <naredba>
			ExpressionCommand expressionCommand1 = (ExpressionCommand) children.get(2);
			
			expressionCommand1.generateCode();
			
			ExpressionCommand expressionCommand2 = (ExpressionCommand) children.get(3);
			
			String label = "FOR" + loopCount;
			loopCount++;
			String labelBegin = label + "B";
			String labelEnd = label + "E";
			GeneratorKoda.lines.add(labelBegin + "\tMOVE R0, R0");
			
			expressionCommand2.generateCode();
			
			GeneratorKoda.lines.add("\tPOP R0");
			GeneratorKoda.lines.add("\tCMP R0, 0");
			GeneratorKoda.lines.add("\tJP_Z " + labelEnd);
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope, label);
			loopCount++;
			parentScope.addChildScope(Scope.currentScope);
			Scope.currentScope.setLoop(true);
			
			Command command = (Command) children.get(5);
			
			command.generateCode();
			
			GeneratorKoda.lines.add("\tJP " + labelBegin);
			GeneratorKoda.lines.add(labelEnd + "\tMOVE R0, R0");
			
			Scope.currentScope = parentScope;
		} else if (children.size() == 7) {
			// KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 <izraz> D_ZAGRADA <naredba>
			ExpressionCommand expressionCommand1 = (ExpressionCommand) children.get(2);
			
			expressionCommand1.generateCode();
			
			ExpressionCommand expressionCommand2 = (ExpressionCommand) children.get(3);
			
			String label = "FOR" + loopCount;
			loopCount++;
			String labelBegin = label + "B";
			String labelEnd = label + "E";
			GeneratorKoda.lines.add(labelBegin + "\tMOVE R0, R0");
			
			PrimaryExpression.pushResult = true;
			expressionCommand2.generateCode();
			PrimaryExpression.pushResult = false;
			
			GeneratorKoda.lines.add("\tPOP R0");
			GeneratorKoda.lines.add("\tCMP R0, 0");
			GeneratorKoda.lines.add("\tJP_Z " + labelEnd);
			
			Expression expression = (Expression) children.get(4);
			
			PrimaryExpression.pushResult = false;
			expression.generateCode();
			
			Scope parentScope = Scope.currentScope;
			Scope.currentScope = new Scope(parentScope, "FOR" + loopCount);
			loopCount++;
			parentScope.addChildScope(Scope.currentScope);
			Scope.currentScope.setLoop(true);
			
			Command command = (Command) children.get(6);
						
			command.generateCode();
			
			GeneratorKoda.lines.add("\tJP " + labelBegin);
			GeneratorKoda.lines.add(labelEnd + "\tMOVE R0, R0");
			
			Scope.currentScope = parentScope;
		}
	}
}
