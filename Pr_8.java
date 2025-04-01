import java.util.*;

public class Pr_8 {
    private Map<Character, List<List<Character>>> productions;
    private Set<Character> nonTerminals;
    private Set<Character> terminals;
    private Map<Character, Set<Character>> first;
    private Map<Character, Set<Character>> follow;
    private Map<Character, Map<Character, List<Character>>> parsingTable;

    public Pr_8() {
        // Initialize productions
        productions = new HashMap<>();
        productions.put('S', Arrays.asList(Arrays.asList('A', 'B', 'C'), Arrays.asList('D')));
        productions.put('A', Arrays.asList(Arrays.asList('a'), Arrays.asList('ε')));
        productions.put('B', Arrays.asList(Arrays.asList('b'), Arrays.asList('ε')));
        productions.put('C', Arrays.asList(Arrays.asList('(', 'S', ')'), Arrays.asList('c')));
        productions.put('D', Arrays.asList(Arrays.asList('A', 'C')));

        nonTerminals = new HashSet<>(productions.keySet());
        terminals = new HashSet<>(Arrays.asList('a', 'b', '(', ')', 'c', 'ε'));

        first = new HashMap<>();
        follow = new HashMap<>();
        for (Character nt : nonTerminals) {
            first.put(nt, new HashSet<>());
            follow.put(nt, new HashSet<>());
        }
        parsingTable = new HashMap<>();
        for (Character nt : nonTerminals) {
            parsingTable.put(nt, new HashMap<>());
        }
    }

    public void computeFirst() {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Character nt : productions.keySet()) {
                int oldSize = first.get(nt).size();
                for (List<Character> production : productions.get(nt)) {
                    for (Character symbol : production) {
                        if (terminals.contains(symbol)) {
                            first.get(nt).add(symbol);
                            break;
                        } else {
                            Set<Character> symbolFirst = new HashSet<>(first.get(symbol));
                            symbolFirst.remove('ε');
                            first.get(nt).addAll(symbolFirst);
                            if (!first.get(symbol).contains('ε')) {
                                break;
                            }
                            if (symbol == production.get(production.size() - 1)) {
                                first.get(nt).add('ε');
                            }
                        }
                    }
                    if (oldSize != first.get(nt).size()) {
                        changed = true;
                    }
                }
            }
        }
    }

    public void computeFollow() {
        follow.get('S').add('$');
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Character nt : productions.keySet()) {
                for (List<Character> production : productions.get(nt)) {
                    for (int i = 0; i < production.size(); i++) {
                        Character symbol = production.get(i);
                        if (nonTerminals.contains(symbol)) {
                            List<Character> rest = (i + 1 < production.size())
                                    ? production.subList(i + 1, production.size())
                                    : new ArrayList<>();
                            Set<Character> firstOfRest = new HashSet<>();
                            boolean hasEpsilon = true;
                            for (Character nextSymbol : rest) {
                                if (terminals.contains(nextSymbol)) {
                                    firstOfRest.add(nextSymbol);
                                    hasEpsilon = false;
                                    break;
                                }
                                Set<Character> nextFirst = new HashSet<>(first.get(nextSymbol));
                                firstOfRest.addAll(nextFirst);
                                if (!nextFirst.contains('ε')) {
                                    hasEpsilon = false;
                                    break;
                                }
                            }
                            firstOfRest.remove('ε');
                            int oldSize = follow.get(symbol).size();
                            follow.get(symbol).addAll(firstOfRest);
                            if (hasEpsilon || rest.isEmpty()) {
                                follow.get(symbol).addAll(follow.get(nt));
                            }
                            if (follow.get(symbol).size() > oldSize) {
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
    }

    public void constructParsingTable() {
        for (Character nt : productions.keySet()) {
            for (List<Character> production : productions.get(nt)) {
                // Compute First of the production
                Set<Character> firstOfProduction = new HashSet<>();
                boolean hasEpsilon = true;
                for (Character symbol : production) {
                    if (terminals.contains(symbol)) {
                        firstOfProduction.add(symbol);
                        hasEpsilon = false;
                        break;
                    } else {
                        Set<Character> symbolFirst = new HashSet<>(first.get(symbol));
                        firstOfProduction.addAll(symbolFirst);
                        if (!symbolFirst.contains('ε')) {
                            hasEpsilon = false;
                            break;
                        }
                    }
                }
                firstOfProduction.remove('ε');

                // Add production to parsing table for each terminal in First
                for (Character terminal : firstOfProduction) {
                    if (terminal != 'ε') {
                        parsingTable.get(nt).put(terminal, production);
                    }
                }

                // If production can derive ε, add production for each terminal in Follow
                if (hasEpsilon || production.size() == 1 && production.get(0) == 'ε') {
                    for (Character terminal : follow.get(nt)) {
                        parsingTable.get(nt).put(terminal, production);
                    }
                }
            }
        }
    }

    public boolean isLL1() {
        // Check for conflicts in the parsing table
        for (Character nt : parsingTable.keySet()) {
            Map<Character, List<Character>> row = parsingTable.get(nt);
            Set<Character> terminalsSeen = new HashSet<>();
            for (Character terminal : row.keySet()) {
                if (terminalsSeen.contains(terminal)) {
                    return false; // Conflict: multiple productions for the same terminal
                }
                terminalsSeen.add(terminal);
            }
        }
        return true;
    }

    public void printParsingTable() {
        System.out.println("Predictive Parsing Table:");
        Set<Character> allTerminals = new HashSet<>(Arrays.asList('a', 'b', '(', ')', 'c', '$'));
        System.out.print("NT\t");
        for (Character t : allTerminals) {
            System.out.print(t + "\t");
        }
        System.out.println();
        for (Character nt : parsingTable.keySet()) {
            System.out.print(nt + "\t");
            for (Character t : allTerminals) {
                List<Character> production = parsingTable.get(nt).get(t);
                if (production != null) {
                    System.out.print(
                            nt + "→" + (production.size() == 1 && production.get(0) == 'ε' ? "ε" : production) + "\t");
                } else {
                    System.out.print("-\t");
                }
            }
            System.out.println();
        }
    }

    public void validateString(String input) {
        if (!isLL1()) {
            System.out.println("Grammar is not LL(1). Cannot validate string.");
            return;
        }

        Stack<Character> stack = new Stack<>();
        stack.push('$');
        stack.push('S'); // Start symbol

        input = input + "$"; // Append end marker
        int index = 0;

        System.out.println("\nValidating string: " + input.substring(0, input.length() - 1));
        while (!stack.isEmpty()) {
            Character top = stack.peek();
            Character current = input.charAt(index);

            if (top == '$' && current == '$') {
                System.out.println("Valid string");
                return;
            }

            if (terminals.contains(top) || top == '$') {
                if (top == current) {
                    stack.pop();
                    index++;
                } else {
                    System.out.println("Invalid string");
                    return;
                }
            } else {
                List<Character> production = parsingTable.get(top).get(current);
                if (production == null) {
                    System.out.println("Invalid string");
                    return;
                }
                stack.pop();
                if (!(production.size() == 1 && production.get(0) == 'ε')) {
                    for (int i = production.size() - 1; i >= 0; i--) {
                        stack.push(production.get(i));
                    }
                }
            }
        }
        System.out.println("Invalid string");
    }

    public static void main(String[] args) {
        Pr_8 parser = new Pr_8();
        parser.computeFirst();
        parser.computeFollow();
        parser.constructParsingTable();
        parser.printParsingTable();

        boolean isLL1 = parser.isLL1();
        System.out.println("\nGrammar is " + (isLL1 ? "LL(1)" : "not LL(1)"));

        // Example input string to validate
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a string to validate: ");
        String input = scanner.nextLine();
        parser.validateString(input);
        scanner.close();
    }
}