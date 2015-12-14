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
	}
}
