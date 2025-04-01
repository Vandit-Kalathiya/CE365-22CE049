import java.util.*;

public class Pr_12 {
    private static final Set<String> operators = new HashSet<>(Arrays.asList("+", "-", "*", "/", "^"));

    // Tokenize the input expression, including parentheses
    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        expression = expression.replaceAll("\\s+", ""); // Remove spaces

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (operators.contains(String.valueOf(c)) || c == '(' || c == ')') {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken = new StringBuilder();
                }
                if (operators.contains(String.valueOf(c)) || c == '(' || c == ')') {
                    tokens.add(String.valueOf(c));
                }
            } else {
                currentToken.append(c);
            }
        }
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        // Validate tokens
        int parenCount = 0;
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.equals("(")) {
                parenCount++;
            } else if (token.equals(")")) {
                parenCount--;
                if (parenCount < 0) {
                    throw new IllegalArgumentException("Invalid expression: unmatched closing parenthesis");
                }
            } else if (operators.contains(token)) {
                // Ensure operators are not at the start or end, and not consecutive
                if (i == 0 || i == tokens.size() - 1 ||
                        (i < tokens.size() - 1
                                && (operators.contains(tokens.get(i + 1)) || tokens.get(i + 1).equals(")")))
                        ||
                        (i > 0 && tokens.get(i - 1).equals("("))) {
                    throw new IllegalArgumentException("Invalid expression: malformed operator usage at " + token);
                }
            }
        }
        if (parenCount != 0) {
            throw new IllegalArgumentException("Invalid expression: unmatched parentheses");
        }
        return tokens;
    }

    // Check if a string is a number (integer or decimal)
    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check if a string is a variable
    private boolean isVariable(String token) {
        return token.matches("[a-zA-Z]+[a-zA-Z0-9]*");
    }

    // Get operator precedence
    private int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }

    // Build expression tree using Shunting Yard algorithm with parentheses support
    private Node buildExpressionTree(List<String> tokens) {
        Stack<Node> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token) || isVariable(token)) {
                operandStack.push(new Node(token));
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    if (operandStack.size() < 2) {
                        throw new IllegalArgumentException(
                                "Invalid expression: not enough operands for operator " + operatorStack.peek());
                    }
                    String op = operatorStack.pop();
                    Node right = operandStack.pop();
                    Node left = operandStack.pop();
                    Node newNode = new Node(op);
                    newNode.left = left;
                    newNode.right = right;
                    operandStack.push(newNode);
                }
                if (!operatorStack.isEmpty() && operatorStack.peek().equals("(")) {
                    operatorStack.pop(); // Remove the '('
                } else {
                    throw new IllegalArgumentException("Invalid expression: unmatched parenthesis");
                }
            } else if (operators.contains(token)) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(") &&
                        getPrecedence(operatorStack.peek()) >= getPrecedence(token)) {
                    if (operandStack.size() < 2) {
                        throw new IllegalArgumentException(
                                "Invalid expression: not enough operands for operator " + operatorStack.peek());
                    }
                    String op = operatorStack.pop();
                    Node right = operandStack.pop();
                    Node left = operandStack.pop();
                    Node newNode = new Node(op);
                    newNode.left = left;
                    newNode.right = right;
                    operandStack.push(newNode);
                }
                operatorStack.push(token);
            }
        }

        while (!operatorStack.isEmpty()) {
            String op = operatorStack.peek();
            if (op.equals("(")) {
                throw new IllegalArgumentException("Invalid expression: unmatched opening parenthesis");
            }
            if (operandStack.size() < 2) {
                throw new IllegalArgumentException("Invalid expression: not enough operands for operator " + op);
            }
            operatorStack.pop();
            Node right = operandStack.pop();
            Node left = operandStack.pop();
            Node newNode = new Node(op);
            newNode.left = left;
            newNode.right = right;
            operandStack.push(newNode);
        }

        if (operandStack.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression: no operands found");
        }
        return operandStack.pop();
    }

    // Evaluate a constant expression
    private double evaluate(double left, double right, String operator) {
        switch (operator) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "*":
                return left * right;
            case "/":
                if (right == 0)
                    throw new ArithmeticException("Division by zero");
                return left / right;
            case "^":
                return Math.pow(left, right);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    // Perform constant folding on the expression tree
    private Node foldConstants(Node node) {
        if (node == null)
            return null;

        // Recursively fold children
        node.left = foldConstants(node.left);
        node.right = foldConstants(node.right);

        // If the current node is an operator and both children are numbers, evaluate
        if (operators.contains(node.value) && node.left != null && node.right != null) {
            if (isNumber(node.left.value) && isNumber(node.right.value)) {
                double leftValue = Double.parseDouble(node.left.value);
                double rightValue = Double.parseDouble(node.right.value);
                double result = evaluate(leftValue, rightValue, node.value);
                return new Node(String.valueOf(result));
            }
        }
        return node;
    }

    // Convert the expression tree back to a string with proper parentheses
    private String treeToString(Node node) {
        if (node == null)
            return "";
        if (node.left == null && node.right == null) {
            return node.value;
        }
        String left = treeToString(node.left);
        String right = treeToString(node.right);
        // Add parentheses around sub-expressions to maintain precedence
        if (operators.contains(node.value)) {
            return "(" + left + " " + node.value + " " + right + ")";
        }
        return left + " " + node.value + " " + right;
    }

    // Optimize the expression using constant folding
    public String optimizeExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be empty");
        }

        // Tokenize the expression
        List<String> tokens = tokenize(expression);
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression: no tokens found");
        }

        // Build the expression tree
        Node root = buildExpressionTree(tokens);

        // Perform constant folding
        root = foldConstants(root);

        // Convert back to string
        String result = treeToString(root);

        // Clean up the result (remove unnecessary outer parentheses for simple
        // expressions)
        result = result.trim();
        while (result.startsWith("(") && result.endsWith(")") && isBalanced(result.substring(1, result.length() - 1))) {
            result = result.substring(1, result.length() - 1).trim();
        }
        return result;
    }

    // Check if a string has balanced parentheses
    private boolean isBalanced(String expr) {
        int count = 0;
        for (char c : expr.toCharArray()) {
            if (c == '(')
                count++;
            if (c == ')')
                count--;
            if (count < 0)
                return false;
        }
        return count == 0;
    }

    public static void main(String[] args) {
        Pr_12 cf = new Pr_12();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter arithmetic expressions (one per line, type 'exit' to stop):");
        while (true) {
            System.out.print("Expression: ");
            String expression = scanner.nextLine();
            if (expression.equalsIgnoreCase("exit"))
                break;

            try {
                String optimized = cf.optimizeExpression(expression);
                System.out.println("Optimized expression: " + optimized);
            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}

class Node {
    String value; // Can be an operator (+, -, *, /, ^), a number, a variable, or parentheses
    Node left, right;

    Node(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}