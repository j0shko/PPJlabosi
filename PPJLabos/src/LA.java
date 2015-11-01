import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;


public class LA {

	public static void main(String[] args) throws IOException {
		LAAutomat automat = null;
		
		try {
			FileInputStream fileIn = new FileInputStream("analizator/automat.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			automat = (LAAutomat) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		String code = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String currentLine;
		while ((currentLine = br.readLine()) != null) {
			code += currentLine;
		}
		
		
	}
}
