import java.util.Scanner;

public class RDP {

    static int idx = 0;
    static String s;
    static boolean flag = false;

    static void X() {
        if (idx == s.length()) {
            flag = true;
            return;
        }
        if (s.charAt(idx) == 'y') {
            idx++;
            P();
            Q();
            X_Dash();
        } else
            flag = false;
    }

    static void X_Dash() {
        if (idx == s.length()) {
            flag = true;
            return;
        }
        if (s.charAt(idx) == 'a') {
            idx++;
            if (s.charAt(idx) == 'b') {
                idx++;
                X_Dash();
            } else {
                flag = false;
                return;
            }
        } else
            return;
    }

    static void P() {
        if (idx == s.length()) {
            flag = true;
            return;
        }
        if (s.charAt(idx) == 'g') {
            idx++;
            if (s.charAt(idx) == 'h' || s.charAt(idx) == 'i') {
                idx++;
                P();
            } else {
                flag = false;
                return;
            }
        } else if (s.charAt(idx) == 'q') {
            idx++;
        } else
            return;
    }

    static void Q() {
        if (idx == s.length()) {
            flag = true;
            return;
        }
        if (s.charAt(idx) == 'x') {
            idx++;
            if (s.charAt(idx) == 'y') {
                idx++;
            } else {
                flag = false;
                return;
            }
        } else if (s.charAt(idx) == 'm') {
            idx++;
            if (s.charAt(idx) == 'l') {
                idx++;
            } else {
                flag = false;
                return;
            }
        } else
            flag = false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RDP.s = sc.nextLine();
        RDP.X();
        System.out.println(RDP.flag ? "Valid" : "Invalid");
        sc.close();
    }
}
