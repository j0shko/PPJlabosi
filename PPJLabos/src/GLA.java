import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GLA {

	public static void main(String[] args) throws IOException {
		Map<String, String> regexs = new HashMap<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String current = br.readLine();
		while(!current.startsWith("%X")) {
			String[] line = current.split("\\s");
			for (String regexName : regexs.keySet()) {
				if (line[1].contains(regexName)) {
					line[1] = line[1].replace(regexName, "("+regexs.get(regexName)+")");
				}
			}
			regexs.put(line[0], line[1]);
			current = br.readLine();
		}
	}
}
