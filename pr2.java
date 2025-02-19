import java.util.*;

public class pr2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        System.out.print("Number of input symbols : ");
        int ip = sc.nextInt();
        sc.nextLine(); 

        System.out.print("Enter symbols : ");
        List<Character> symbols = new ArrayList<>();
        for (int i = 0; i < ip; i++) {
            char s = sc.next().charAt(0);
            symbols.add(s);
        }

        System.out.print("Number of states : ");
        int states = sc.nextInt();

        System.out.print("Number of accepting states : ");
        int a_states = sc.nextInt();

        List<Integer> accepting_states = new ArrayList<>();
        for (int i = 0; i < a_states; i++) {
            int t = sc.nextInt();
            accepting_states.add(t);
        }

        int[][] transition_table = new int[states][ip];

        for (int i = 0; i < transition_table.length; i++) {
            for (int j = 0; j < transition_table[0].length; j++) {
                System.out.print((i + 1) + " to " + (j == 0 ? "a" : "b") + " : ");
                int t = sc.nextInt();
                transition_table[i][j] = t;
            }
        }
        sc.nextLine();

        while (!exit) {
            System.out.print("Enter input string (or 'exit' to quit) : ");
            String str = sc.nextLine();

            if (str.equalsIgnoreCase("exit")) {
                exit = true;
            } else {
                int curr = 1;
                if (str.isEmpty()) {
                    if (accepting_states.contains(curr)) {
                        System.out.println("Valid");
                    } else {
                        System.out.println("Invalid");
                    }
                } else {
                    for (int i = 0; i < str.length(); i++) {
                        if(symbols.contains(str.charAt(i))){
                            int from = curr - 1, to = str.charAt(i) - 'a';
                            curr = transition_table[from][to];
                        }else{
                            int from = curr - 1, to = ip;
                            curr = transition_table[from][to];
                        }
                    }
                    if (accepting_states.contains(curr)) {
                        System.out.println("Valid");
                    } else {
                        System.out.println("Invalid");
                    }
                }
            }
        }
    }
}


/* 1) Instead of using RE how would you impl the str validation logic using basic str manipulation techniques in C. 
 * 2) How the program would behave if the user enters a str containing characters other than a and b modify the program to handle such cases.
 * 3) In real world senarios where a similar str validation program we used. Can you think of an example where a specific pattern need to be an enforced.
 * 4) How will you handle null str as a test case.
*/