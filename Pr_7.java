import java.util.*;

public class Pr_7 {
    private Map<Character, List<List<Character>>> productions;
    private Set<Character> nonTerminals;
    private Set<Character> terminals;
    private Map<Character, Set<Character>> first;
    private Map<Character, Set<Character>> follow;

    public Pr_7() {
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
                            int prevSize = first.get(nt).size();
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
        // Add $ to Follow(S)
        follow.get('S').add('$');

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Character nt : productions.keySet()) {
                for (List<Character> production : productions.get(nt)) {
                    for (int i = 0; i < production.size(); i++) {
                        Character symbol = production.get(i);
                        if (nonTerminals.contains(symbol)) {
                            // Get rest of production
                            List<Character> rest = (i + 1 < production.size())
                                    ? production.subList(i + 1, production.size())
                                    : new ArrayList<>();

                            // Compute First of remaining string
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
                            firstOfRest.remove('ε'); // Remove ε from First sets

                            int oldSize = follow.get(symbol).size();
                            // Add First of remaining symbols
                            follow.get(symbol).addAll(firstOfRest);

                            // If rest can derive ε or is empty, add Follow of left side
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

    public void printSets() {
        System.out.println("First Sets:");
        for (Character nt : first.keySet()) {
            List<Character> sorted = new ArrayList<>(first.get(nt));
            Collections.sort(sorted);
            System.out.printf("First(%c) = {%s}\n", nt, String.join(", ", sorted.toString()
                    .replace("[", "").replace("]", "").split(", ")));
        }
        System.out.println("\nFollow Sets:");
        for (Character nt : follow.keySet()) {
            List<Character> sorted = new ArrayList<>(follow.get(nt));
            Collections.sort(sorted);
            System.out.printf("Follow(%c) = {%s}\n", nt, String.join(", ", sorted.toString()
                    .replace("[", "").replace("]", "").split(", ")));
        }
    }

    public static void main(String[] args) {
        Pr_7 Pr_7 = new Pr_7();
        Pr_7.computeFirst();
        Pr_7.computeFollow();
        Pr_7.printSets();
    }
}