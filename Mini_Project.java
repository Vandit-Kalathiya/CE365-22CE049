import java.util.regex.*;

public class Mini_Project {

    private static final Pattern TYPE_PATTERN = Pattern
            .compile("^(int|float|double|char|short|long|void|unsigned|signed)\\b");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*");
    private static final Pattern ARRAY_PATTERN = Pattern.compile("^\\[[0-9]*\\]");
    private static final Pattern POINTER_PATTERN = Pattern.compile("^\\*+");
    private static final Pattern INITIALIZATION_PATTERN = Pattern.compile("^=\\s*([^,;]+)");
    private static final Pattern SEMICOLON_PATTERN = Pattern.compile("^\\s*;");

    public static boolean analyzeDeclaration(String declaration) {
        String remaining = declaration.trim();

        // Check for type specifier
        Matcher typeMatcher = TYPE_PATTERN.matcher(remaining);
        if (!typeMatcher.find()) {
            System.out.println("Error: Missing or invalid type specifier");
            return false;
        }
        remaining = remaining.substring(typeMatcher.end()).trim();

        boolean hasVariables = false;

        // Process all variables in the declaration
        while (!remaining.isEmpty()) {
            hasVariables = true;

            // Check for pointer notation
            Matcher pointerMatcher = POINTER_PATTERN.matcher(remaining);
            if (pointerMatcher.find()) {
                remaining = remaining.substring(pointerMatcher.end()).trim();
            }

            // Check for identifier
            Matcher idMatcher = IDENTIFIER_PATTERN.matcher(remaining);
            if (!idMatcher.find()) {
                System.out.println("Error: Missing or invalid identifier");
                return false;
            }
            remaining = remaining.substring(idMatcher.end()).trim();

            // Check for array notation
            Matcher arrayMatcher = ARRAY_PATTERN.matcher(remaining);
            if (arrayMatcher.find()) {
                remaining = remaining.substring(arrayMatcher.end()).trim();
            }

            // Check for initialization
            Matcher initMatcher = INITIALIZATION_PATTERN.matcher(remaining);
            if (initMatcher.find()) {
                remaining = remaining.substring(initMatcher.end()).trim();
            }

            // Check for comma separator
            if (remaining.startsWith(",")) {
                remaining = remaining.substring(1).trim();
                continue;
            }

            // Must end with semicolon
            Matcher semiMatcher = SEMICOLON_PATTERN.matcher(remaining);
            if (semiMatcher.find()) {
                remaining = "";
                break;
            }

            System.out.println("Error: Expected ',' or ';' after variable declaration");
            return false;
        }

        if (!hasVariables) {
            System.out.println("Error: No variables declared");
            return false;
        }

        if (!remaining.isEmpty()) {
            System.out.println("Error: Invalid syntax near '" + remaining + "'");
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        String[] testCases = {
                "int x = 10;", // Valid
                "double value = 5.5;", // Valid
                "char *name;", // Valid
                "int numbers[10];", // Valid
                "float a, b, c;", // Valid
                "int x", // Invalid - missing semicolon
                "double ;", // Invalid - missing identifier
                "float 123var;", // Invalid - bad identifier
                "int * ptr;", // Invalid - space between * and identifier
                "long array[5", // Invalid - missing semicolon and bracket
                "boolean flag = true;" // Invalid - boolean not a basic C type
        };

        for (String testCase : testCases) {
            boolean isValid = analyzeDeclaration(testCase);
            System.out.printf("%-25s => %s%n", testCase, isValid ? "Valid" : "Invalid");
            if (!isValid) {
                System.out.println("---");
            }
        }
    }
}