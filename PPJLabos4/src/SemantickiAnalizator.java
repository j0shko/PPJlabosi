import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class SemantickiAnalizator {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String text = "";
		
		String line;
		
		while ((line = br.readLine()) != null) {
			text += line + '\n';
		}

		GenerativeTree tree = new GenerativeTree(text);
		
		Scope.currentScope = new Scope(null);
		Scope.globalScope = Scope.currentScope;
		try {
			((ICheckable) tree.getRoot()).check();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		if (!Scope.globalScope.containsFunction("main") || !Scope.globalScope.getFunction("main").getType().equals("f(void->int)")) {
			System.out.println("main");
		} else {
			if (!Scope.globalScope.checkIfAllFunctionsAreDefined()) {
				System.out.println("funkcija");
			}
		}
	}
}
