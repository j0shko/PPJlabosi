import java.util.List;
import java.util.Set;


public class LLProduction {

	private NonTerminalSign leftSide;
	private List<Sign> rightSide;
	private Set<TerminalSign> nextSigns;
	
	public LLProduction(NonTerminalSign leftSide, List<Sign> rightSide, Set<TerminalSign> nextSigns) {
		this.leftSide = leftSide;
		this.rightSide = rightSide;
		this.nextSigns = nextSigns;
	}
	
	public NonTerminalSign getLeftSide() {
		return leftSide;
	}

	public List<Sign> getRightSide() {
		return rightSide;
	}

	public Set<TerminalSign> getNextSigns() {
		return nextSigns;
	}

	@Override
	public String toString() {
		StringBuilder name = new StringBuilder();
		name.append(leftSide).append(" -> ");
		for (Sign sign : rightSide) {
			name.append(sign).append(" ");
		}
		name.append("{");
		for (TerminalSign sign : nextSigns) {
			name.append(sign).append(" ");
		} 
		name.deleteCharAt(name.length() -1);
		name.append("}");
		return name.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((leftSide == null) ? 0 : leftSide.hashCode());
		result = prime * result
				+ ((nextSigns == null) ? 0 : nextSigns.hashCode());
		result = prime * result
				+ ((rightSide == null) ? 0 : rightSide.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LLProduction other = (LLProduction) obj;
		if (leftSide == null) {
			if (other.leftSide != null)
				return false;
		} else if (!leftSide.equals(other.leftSide))
			return false;
		if (nextSigns == null) {
			if (other.nextSigns != null)
				return false;
		} else if (!nextSigns.equals(other.nextSigns))
			return false;
		if (rightSide == null) {
			if (other.rightSide != null)
				return false;
		} else if (!rightSide.equals(other.rightSide))
			return false;
		return true;
	}
}
