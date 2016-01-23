import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GeneratorKoda {
	
	public static List<String> lines;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String text = "";
		
		String line;
		
		while ((line = br.readLine()) != null) {
			text += line + '\n';
		}

		GenerativeTree tree = new GenerativeTree(text);

		lines = new ArrayList<>();
		
		lines.add("\tMOVE 40000, R7");
		lines.add("\tCALL F_MAIN");
		lines.add("\tHALT");

		((IGeneratable) tree.getRoot()).generateCode();
		
		Path outputFile = Paths.get("a.frisc");
		Files.write(outputFile, lines, Charset.forName("UTF-8"));
	}
}
