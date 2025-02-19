import java.io.*;
import java.util.*;

public class pr3 {
    private static final Set<String> keywords = new HashSet<>(Arrays.asList(
        "auto", "break", "case", "char", "const", "continue", "default", "do",
        "double", "else", "enum", "extern", "float", "for", "goto", "if",
        "int", "long", "register", "return", "short", "signed", "sizeof", "static",
        "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while"
    ));

    private static final Set<String> operators = new HashSet<>(Arrays.asList(
        "+", "-", "*", "/", "%", "=", "+=", "-=", "*=", "/=", "%=",
        "==", "!=", ">", "<", ">=", "<=", "&&", "||", "!", "&", "|",
        "^", "~", "<<", ">>", "++", "--"
    ));

    private static final Set<Character> punctuation = new HashSet<>(Arrays.asList(
        '(', ')', '{', '}', '[', ']', ';', ',', '.'
    ));

    private final String sourceCode;
    private int currentPosition;
    private final List<Token> tokens;

    public pr3(String sourceCode) {
        this.sourceCode = sourceCode;
        this.currentPosition = 0;
        this.tokens = new ArrayList<>();
    }

    public static void analyzeFile(String filename) {
        try {
            String sourceCode = readFile(filename);
            pr3 analyzer = new pr3(sourceCode);
            analyzer.analyze();
            analyzer.printTokens();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private void analyze() {
        while (currentPosition < sourceCode.length()) {
            char currentChar = sourceCode.charAt(currentPosition);

            if (Character.isWhitespace(currentChar)) {
                currentPosition++;
                continue;
            }

            Token token = null;
            if (Character.isLetter(currentChar) || currentChar == '_') {
                token = processIdentifierOrKeyword();
            } else if (Character.isDigit(currentChar)) {
                token = processNumber();
            } else if (currentChar == '"' || currentChar == '\'') {
                token = processString();
            } else if (isOperatorStart(currentChar)) {
                token = processOperator();
            } else if (isPunctuation(currentChar)) {
                token = processPunctuation();
            } else {
                currentPosition++; // Skip unrecognized characters
            }

            if (token != null) {
                tokens.add(token);
            }
        }
    }

    private Token processIdentifierOrKeyword() {
        StringBuilder builder = new StringBuilder();
        while (currentPosition < sourceCode.length() && 
               (Character.isLetterOrDigit(sourceCode.charAt(currentPosition)) || 
                sourceCode.charAt(currentPosition) == '_')) {
            builder.append(sourceCode.charAt(currentPosition++));
        }
        
        String word = builder.toString();
        return new Token(
            keywords.contains(word) ? TokenType.KEYWORD : TokenType.IDENTIFIER,
            word
        );
    }

    private Token processNumber() {
        StringBuilder builder = new StringBuilder();
        while (currentPosition < sourceCode.length() && 
               (Character.isDigit(sourceCode.charAt(currentPosition)) || 
                sourceCode.charAt(currentPosition) == '.')) {
            builder.append(sourceCode.charAt(currentPosition++));
        }
        return new Token(TokenType.CONSTANT, builder.toString());
    }

    private Token processString() {
        StringBuilder builder = new StringBuilder();
        char quote = sourceCode.charAt(currentPosition);
        builder.append(quote);
        currentPosition++;

        while (currentPosition < sourceCode.length() && 
               sourceCode.charAt(currentPosition) != quote) {
            if (sourceCode.charAt(currentPosition) == '\\') {
                builder.append(sourceCode.charAt(currentPosition++));
                if (currentPosition < sourceCode.length()) {
                    builder.append(sourceCode.charAt(currentPosition++));
                }
            } else {
                builder.append(sourceCode.charAt(currentPosition++));
            }
        }

        if (currentPosition < sourceCode.length()) {
            builder.append(quote);
            currentPosition++;
        }

        return new Token(TokenType.STRING, builder.toString());
    }

    private Token processOperator() {
        if (currentPosition + 1 < sourceCode.length()) {
            String possibleOperator = sourceCode.substring(currentPosition, currentPosition + 2);
            if (operators.contains(possibleOperator)) {
                currentPosition += 2;
                return new Token(TokenType.OPERATOR, possibleOperator);
            }
        }

        String singleOperator = String.valueOf(sourceCode.charAt(currentPosition++));
        if (operators.contains(singleOperator)) {
            return new Token(TokenType.OPERATOR, singleOperator);
        }
        return null;
    }

    private Token processPunctuation() {
        char punct = sourceCode.charAt(currentPosition++);
        return new Token(TokenType.PUNCTUATION, String.valueOf(punct));
    }

    private boolean isOperatorStart(char c) {
        return operators.stream().anyMatch(op -> op.charAt(0) == c);
    }

    private boolean isPunctuation(char c) {
        return punctuation.contains(c);
    }

    private void printTokens() {
        System.out.print("TOKENS ");
        for (Token token : tokens) {
            System.out.println(token + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CLexicalAnalyzer <input-file>");
            return;
        }
        analyzeFile(args[0]);
    }
}

class Token {
    private final TokenType type;
    private final String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return type + " : " + value;
    }
}

enum TokenType {
    KEYWORD,
    IDENTIFIER,
    OPERATOR,
    CONSTANT,
    STRING,
    PUNCTUATION
}