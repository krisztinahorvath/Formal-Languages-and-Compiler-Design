import java.util.*;

public class Parser {
    private HashMap<String, Set<String>> firstSets;
    private HashMap<String, Set<String>> followSets;
    Grammar grammar;
    ParseTable parseTable ;
    private Map<Pair<String, List<String>>, Integer> productionsNumbered;
    private Stack<String> alpha; // input stack
    private Stack<String> beta; // working stack
    private Stack<String> pi; // result stack
    private ParseTreeNode parseTreeRoot;

    public Parser(String filePath){
        firstSets = new HashMap<>();
        followSets = new HashMap<>();
        grammar = new Grammar(filePath);
        parseTable = new ParseTable();
        productionsNumbered = new HashMap<>();
        alpha = new Stack<>();
        beta = new Stack<>();
        pi = new Stack<>();
    }

    public ParseTable getParseTable(){
        return parseTable;
    }

    public ParseTreeNode getParseTreeRoot() {
        return parseTreeRoot;
    }
    private void setFirstSets() {
        for (String nonterminal : grammar.getSetOfNonterminals()) {
            firstSets.put(nonterminal, firstOf(nonterminal));
        }
    }

    private Set<String> firstOf(String nonterminal) {
        if (firstSets.containsKey(nonterminal))
            return firstSets.get(nonterminal);

        Set<String> result = new HashSet<>();
        List<String> terminals = grammar.getSetOfTerminals();

        List<List<String>> productions = grammar.getProductionsOfNonterminal(nonterminal);
        for (List<String> production : productions) {
            String firstSymbol = production.get(0);
            if (firstSymbol.equals("Є")) {
                result.add("Є");
            } else if (terminals.contains(firstSymbol)) {
                result.add(firstSymbol);
            } else {
                result.addAll(firstOf(firstSymbol));
            }
        }
        return result;
    }

    private void setFollowSets() {
        for (String nonterminal : grammar.getSetOfNonterminals()) {
            followSets.put(nonterminal, followOf(nonterminal));
        }
    }

    private Set<String> followOf(String nonterm) {
        if (followSets.containsKey(nonterm))
            return followSets.get(nonterm);

        Set<String> result = new HashSet<>();

        if (nonterm.equals(grammar.getStartingSymbol()))
            result.add("$");

        for (String symbol : grammar.getSetOfNonterminals()) {
            List<List<String>> productions = grammar.getProductionsOfNonterminal(symbol);
            for (List<String> production : productions) {
                int index = production.indexOf(nonterm);
                if (index != -1 && index < production.size() - 1) {
                    String nextSymbol = production.get(index + 1);

                    // Check if nextSymbol is a nonterminal before retrieving its firstSets
                    if (grammar.getSetOfNonterminals().contains(nextSymbol)) {
                        Set<String> firstOfNextSymbol = firstSets.get(nextSymbol);

                        // Add everything in FIRST(nextSymbol) except Є to FOLLOW(nonterm)
                        result.addAll(firstOfNextSymbol);
                        result.remove("Є");

                        // If Є is in FIRST(nextSymbol), add FOLLOW(symbol) to FOLLOW(nonterm)
                        if (firstOfNextSymbol.contains("Є")) {
                            Set<String> followOfSymbol = followOf(symbol);
                            result.addAll(followOfSymbol);
                        }
                    } else {
                        // If nextSymbol is a terminal, add it to FOLLOW(nonterm)
                        result.add(nextSymbol);
                    }
                } else if (index == production.size() - 1 && !symbol.equals(nonterm)) {
                    // If nonterm is at the end of a production, add FOLLOW(symbol) to FOLLOW(nonterm)
                    Set<String> followOfSymbol = followOf(symbol);
                    result.addAll(followOfSymbol);
                }
            }
        }

        // Cache the computed follow set for the nonterminal
        followSets.put(nonterm, result);

        return result;
    }

    private void createParseTable(){
        numberingProductions();
        List<String> columnSymbols = new LinkedList<>(grammar.getSetOfTerminals());
        columnSymbols.add("$");

        // M(a, a) = pop
        // M($, $) = acc
        parseTable.put(new Pair<>("$", "$"), new Pair<>(Collections.singletonList("acc"), -1));
        for (String terminal: grammar.getSetOfTerminals())
            parseTable.put(new Pair<>(terminal, terminal), new Pair<>(Collections.singletonList("pop"), -1));

//         1) M(A, a) = (α, i), if:
//            a) a ∈ first(α)
//            b) a != ε
//            c) A -> α production with index i
//
//        2) M(A, b) = (α, i), if:
//            a) ε ∈ first(α)
//            b) whichever b ∈ follow(A)
//            c) A -> α production with index i
        productionsNumbered.forEach((key, value) -> {
            String rowSymbol = key.getFirst();
            List<String> rule = key.getSecond();
            Pair<List<String>, Integer> parseTableValue = new Pair<>(rule, value);

            for (String columnSymbol : columnSymbols) {
                Pair<String, String> parseTableKey = new Pair<>(rowSymbol, columnSymbol);

                // if our column-terminal is exactly first of rule
                if (rule.get(0).equals(columnSymbol) && !columnSymbol.equals("Є"))
                    parseTable.put(parseTableKey, parseTableValue);

                    // if the first symbol is a non-terminal and it's first contain our column-terminal
                else if (grammar.getSetOfNonterminals().contains(rule.get(0)) && firstSets.get(rule.get(0)).contains(columnSymbol)) {
                    if (!parseTable.containsKey(parseTableKey)) {
                        parseTable.put(parseTableKey, parseTableValue);
                    }
                }
                else {
                    // if the first symbol is ε then everything if FOLLOW(rowSymbol) will be in parse table
                    if (rule.get(0).contains("Є")) {
                        for (String b : followSets.get(rowSymbol)) {
                            parseTableKey = new Pair<>(rowSymbol, b);
                            if (!parseTable.containsKey(parseTableKey)) {
                                parseTable.put(parseTableKey, parseTableValue);
                            }
                        }

                        // if Є is in FIRST(rule)
                    } else {
                        Set<String> firsts = new HashSet<>();
                        for (String symbol : rule) {
                            if (grammar.getSetOfNonterminals().contains(symbol)) {
                                firsts.addAll(firstSets.get(symbol));
                                if (!firsts.contains("Є")) {
                                    break;  // Stop adding to firsts if "Є" is not present
                                }
                            } else {
                                break;  // Stop adding to firsts if a terminal is encountered
                            }
                        }
                        if (firsts.contains("Є")) {
                            for (String b : followSets.get(rowSymbol)) {
                                parseTableKey = new Pair<>(rowSymbol, b);
                                if (!parseTable.containsKey(parseTableKey)) {
                                    parseTable.put(parseTableKey, parseTableValue);
                                }
                            }
                        }
                    }

                }
            }
        });
    }

    private void numberingProductions() {
        int index = 1;
        for (String nonTerminal : grammar.getSetOfNonterminals()) {
            List<List<String>> productions = grammar.getProductionsOfNonterminal(nonTerminal);
            for (List<String> rule : productions) {
                Pair<String, List<String>> productionKey = new Pair<>(nonTerminal, rule);
                productionsNumbered.put(productionKey, index++);
            }
        }
    }

    private void pushAsChars(List<String> sequence, Stack<String> stack) {
        for (int i = sequence.size() - 1; i >= 0; i--)
            stack.push(sequence.get(i));
    }

    private void pushIntoTree(List<String> sequence, ParseTreeNode parent){
        for(var symbol: sequence)
            parent.addChild(new ParseTreeNode(symbol));
    }

    private void initializeStacks(List<String> w) {
        alpha.clear();
        alpha.push("$");
        pushAsChars(w, alpha);

        beta.clear();
        beta.push("$");
        beta.push(grammar.getStartingSymbol());
        parseTreeRoot = new ParseTreeNode(grammar.getStartingSymbol());

        pi.clear();
        pi.push("Є");
    }

    // alpha - input
    // beta - working
    // pi - result
    public boolean parse(List<String> w) {
        initializeStacks(w);

        ParseTreeNode currentParent = parseTreeRoot;
        boolean go = true;
        boolean result = true;

        while (go) {
            String betaHead = beta.peek();
            String alphaHead = alpha.peek();

            if (betaHead.equals("$") && alphaHead.equals("$"))
                return result;

            Pair<String, String> heads = new Pair<>(betaHead, alphaHead);
            Pair<List<String>, Integer> parseTableEntry = parseTable.get(heads);

            if (parseTableEntry == null) {
                heads = new Pair<>(betaHead, "Є");
                parseTableEntry = parseTable.get(heads);
                if (parseTableEntry != null) {
                    beta.pop();
                    continue;
                }
            }

            if (parseTableEntry == null) {
                go = false;
                result = false;
            } else {
                List<String> production = parseTableEntry.getFirst();
                Integer productionPos = parseTableEntry.getSecond();

                if (productionPos == -1 && production.get(0).equals("acc")) {
                    go = false;
                } else if (productionPos == -1 && production.get(0).equals("pop")) {
                    beta.pop();
                    alpha.pop();
                } else {
                    beta.pop();
                    if (!production.get(0).equals("Є")){
                        pushAsChars(production, beta);
                        pushIntoTree(production, currentParent);
                    }

                    pi.push(productionPos.toString());
                    if (!currentParent.getChildren().isEmpty()) // get first non-term child
                        for (ParseTreeNode child : currentParent.getChildren())
                            if (grammar.getSetOfNonterminals().contains(child.getSymbol())) {
                                currentParent = child;
                                break;
                            }

                }
            }
        }

        return result;
    }

    public boolean parseSource(List<Pair<Integer, Integer>> pif) {
        List<String> sequence = new LinkedList<>();
        for (Pair<Integer, Integer> pifEntry : pif) {
            sequence.add(String.valueOf(pifEntry.getFirst()));
        }

        return this.parse(sequence);
    }


    public static void main(String[] args) {
        Parser parser = new Parser("src\\g1.txt");
        parser.setFirstSets();
        System.out.println("FIRST");
        for(String key: parser.firstSets.keySet())
            System.out.println(key + "=" + parser.firstSets.get(key));

        parser.setFollowSets();
        System.out.println("\n\nFOLLOW");
        for(String key: parser.followSets.keySet())
            System.out.println(key + "=" + parser.followSets.get(key));

        System.out.println("\n\nPARSE TABLE:");
        parser.createParseTable();
        System.out.println(parser.getParseTable());

        //List<String> sequence = List.of("a", "*", "(", "a", "+", "a", ")");
        List<String> sequence = List.of("a", "b", "b", "c");
        //List<String> sequence = List.of("program", "{","int", ":", "#a", "}");
        if (parser.parse(sequence))
            System.out.println("Sequence accepted!");
        else
            System.out.println("Sequence not accepted!");

        ParseTreeNode parseTreeRoot = parser.getParseTreeRoot();
        System.out.println("\n\nParsing tree: ");
        System.out.println(parseTreeRoot.toStringTree());

        // convert parsing tree to table
        ParserOutput parserOutput = new ParserOutput(parseTreeRoot);

        // display the resulting table
        parserOutput.displayTableRecords();
        parserOutput.writeToFile("src\\out1.txt");
    }
}