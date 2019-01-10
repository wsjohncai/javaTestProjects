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
        String temp;
        StringBuilder match = new StringBuilder();
        while (!(temp = input.nextLine()).equals("quit")) {
            match.append(temp).append("\n");
        }
        input.close();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(match.toString());
        int found = 0;
        while (matcher.find()) {
            found++;
            System.out.println(matcher.group(0));
        }
        if (found == 0)
            System.out.println("no match found !");
    }
}
