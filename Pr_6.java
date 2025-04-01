import java.util.Scanner;

public class Pr_6 {

    public static void S() {
        if (idx == s.length()) {
            if (c == 0) {
                flag = true;
            } else
                flag = false;
            return;
        }
        if (s.charAt(idx) == '(') {
            c++;
            idx++;
            L();
            if (idx == s.length()) {
                if (c == 0) {
                    flag = true;
                } else
                    flag = false;
                return;
            }
            if (s.charAt(idx) == ')') {
                c--;
                idx++;
                flag = true;
            } else
                flag = false;
        } else if (s.charAt(idx) == 'a') {
            idx++;
            flag = true;
            S();
        } else
            flag = false;
    }

    public static void L() {
        S();
        L_dash();
    }

    public static void L_dash() {
        if (idx == s.length()) {
            if (c == 0) {
                flag = true;
            } else
                flag = false;
            return;
        }
        if (s.charAt(idx) == ',') {
            idx++;
            S();
            L_dash();
        } else
            return;
    }

    public static int idx = 0;
    public static int c = 0;
    public static boolean flag = false;
    public static String s;

    public static void main(String[] args) {
        Pr_6 pr_6 = new Pr_6();
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the string: ");
        pr_6.s = s.nextLine();
        pr_6.S();
        System.out.println(flag ? "Valid" : "Invalid");
    }
}