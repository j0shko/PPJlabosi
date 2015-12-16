import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Checker {
	
	private static List<String> LEGAL_CONST_CHARS = Arrays.asList("\\t", "\\n", "\\0", "\\'", "\\\"", "\\\\");
	
	public static boolean checkInteger(String value) {
		try {
			int x = Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}
	
	public static boolean checkChar(String value) {
		if (!(value.startsWith("'") && value.endsWith("'"))) {
			return false;
		}
		
		String val = value.substring(1, value.length() - 1);
		if (LEGAL_CONST_CHARS.contains(val)) {
			return true;
		}
		if (val.length() == 1) {
			return true;
		}
		return false;
	}
	
	public static boolean checkString(String value) {
		if (!(value.startsWith("\"") && value.endsWith("\""))) {
			return false;
		}
		String val = value.substring(1, value.length() - 1);
		for (int i = 0; i < val.length(); ++i) {
			char c = val.charAt(i);
			if (val.charAt(i) == '\\') {
				String temp = val.substring(i, i + 2);
				if (!LEGAL_CONST_CHARS.contains(temp)) {
					return false;
				}
				i++;
			} 
		}
		return true;
	}
}
