import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Map;


public class SA {
	
	@SuppressWarnings("all")
	public static void main(String[] args) throws IOException {
		Parser parser = null;
		Map<String, NonTerminalSign> nonTerminalSigns = null;
		Map<String, TerminalSign> terminalSigns = null;
		
		try {
			FileInputStream fileIn = new FileInputStream("analizator/parser.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			parser = (Parser) in.readObject();
			in.close();
			fileIn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		try {
			FileInputStream fileIn = new FileInputStream("analizator/ts.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			terminalSigns = (Map<String, TerminalSign>) in.readObject();
			in.close();
			fileIn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			FileInputStream fileIn = new FileInputStream("analizator/nts.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			nonTerminalSigns = (Map<String, NonTerminalSign>) in.readObject();
			in.close();
			fileIn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = br.readLine();
		
		while (line != null) {
			String[] lineStuff = line.split(" ");
			String sign = lineStuff[0];
			String lineNum = lineStuff[1];
			StringBuilder data = new StringBuilder("");
			for (int i = 2; i < lineStuff.length; i++) {
				data.append(lineStuff[i]).append(" ");
			}
			data.deleteCharAt(data.length() -1);
			parser.inputLine(terminalSigns.get(sign), Integer.parseInt(lineNum), data.toString());
			
			line = br.readLine();
		}
		
		parser.parse();
		parser.writeTree();
	}
}
