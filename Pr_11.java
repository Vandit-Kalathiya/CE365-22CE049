import java.util.ArrayList;
import java.util.Scanner;

public class Pr_11 {
    private String input;
    private int pos;
    private int tempCount;
    private ArrayList<Quadruple> quadruples;

    public Pr_11() {
        pos = 0;
        tempCount = 1;
        quadruples = new ArrayList<>();
    }

    private char peek() {
        if (pos < input.length()) {
            return input.charAt(pos);
        }
        return '\0';
    }

    private char next() {
        if (pos < input.length()) {
            return input.charAt(pos++);
        }
        return '\0';
    }

    private String parseDigit() {
        StringBuilder num = new StringBuilder();
        while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
            num.append(next());
        }
        return num.toString();
    }

    private String parseF() {
        char ch = peek();
        if (Character.isDigit(ch)) {
            return parseDigit();
        } else if (ch == '(') {
            next();
            String result = parseE();
            if (peek() == ')') {
                next();
                return result;
            }
            throw new IllegalArgumentException("Missing closing parenthesis");
        }
        throw new IllegalArgumentException("Invalid factor at position " + pos);
    }

    private String parseT() {
        String left = parseF();
        while (peek() == '*' || peek() == '/') {
            char op = next();
            String right = parseF();
            String temp = "t" + tempCount++;
            quadruples.add(new Quadruple(String.valueOf(op), left, right, temp));
            left = temp;
        }
        return left;
    }

    private String parseE() {
        String left = parseT();
        while (peek() == '+' || peek() == '-') {
            char op = next();
            String right = parseT();
            String temp = "t" + tempCount++;
            quadruples.add(new Quadruple(String.valueOf(op), left, right, temp));
            left = temp;
        }
        return left;
    }

    public void generate(String expression) {
        this.input = expression.replaceAll("\\s+", ""); // Remove spaces
        pos = 0;
        quadruples.clear();
        tempCount = 1;
        parseE();
    }

    public void printQuadruples() {
        System.out.println("Operator  Operand1  Operand2  Result");
        for (Quadruple q : quadruples) {
            System.out.println(q);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Pr_11 generator = new Pr_11();

        System.out.print("Enter an arithmetic expression: ");
        String expression = scanner.nextLine();

        try {
            generator.generate(expression);
            generator.printQuadruples();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}

class Quadruple {
    String operator;
    String operand1;
    String operand2;
    String result;

    Quadruple(String operator, String operand1, String operand2, String result) {
        this.operator = operator;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-8s %-8s %-8s", operator, operand1, operand2, result);
    }
}