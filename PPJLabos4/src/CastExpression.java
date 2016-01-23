import java.util.List;

public class CastExpression extends TreeNode implements ICheckable, IGeneratable {

	private String type;
	private boolean lExpression;
	
	public CastExpression(TreeNodeData data) {
		super(data);
	}

	public String getType() {
		return type;
	}

	public boolean islExpression() {
		return lExpression;
	}
	
	@Override
	public void check() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// <unarni_izraz>
			
			UnaryExpression unaryExpression = (UnaryExpression) children.get(0);
			unaryExpression.check();
			
			type = unaryExpression.getType();
			lExpression = unaryExpression.islExpression();
		} else {
			// L_ZAGRADA <ime_tipa> D_ZAGRADA <cast_izraz>
			String errorMessage = "<cast_izraz> ::= "+ children.get(0) + " <ime_tipa> " 
									+ children.get(2) + " <cast_izraz>";
			
			TypeName typeName = (TypeName) children.get(1);
			typeName.check();
			
			CastExpression castExpression = (CastExpression) children.get(3);
			castExpression.check();
			
			Checker.throwException(Checker.isCastable(castExpression.getType(), typeName.getType()), errorMessage);
			
			type = typeName.getType();
			lExpression = false;
		}
		
	}
	
	@Override
	public void generateCode() {
		List<TreeNode> children = getChildren();
		
		if (children.size() == 1) {
			// <unarni_izraz>
			
			UnaryExpression unaryExpression = (UnaryExpression) children.get(0);
			unaryExpression.generateCode();
		} else {
			// L_ZAGRADA <ime_tipa> D_ZAGRADA <cast_izraz>
			TypeName typeName = (TypeName) children.get(1);
			typeName.generateCode();
			
			CastExpression castExpression = (CastExpression) children.get(3);
			castExpression.generateCode();
		}
	}
}
