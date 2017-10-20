import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
	public static void main(String[] args) {
		Scanner input;
		System.out.println("input regular expression: ");
		input = new Scanner(System.in);
		String regex = input.nextLine();
		// inputReg.next();
		System.out.println("input the sentence to be match(quit to end input): ");
		String temp, match = "";
		while (!(temp = input.nextLine()).equals("quit")) {
			System.out.println("Æ¥ÅäµÄÎÄ±¾£º" + temp);
			match += temp + "\n";
		}
		input.close();

		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(match);
		int found = 0;
		while(matcher.find()) {
			found++;
			for (int i = 0; i < matcher.groupCount(); i++) {
				System.out.println(matcher.group(i));
			}
		}
		if(found == 0) 
			System.out.println("no match found !");
	}
}
