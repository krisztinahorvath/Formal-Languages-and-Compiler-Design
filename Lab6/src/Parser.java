import java.util.*;

public class Parser {
    private HashMap<String, Set<String>> firstSets;
    private HashMap<String, Set<String>> followSets;
    Grammar grammar;


    public Parser(String filePath){
        firstSets = new HashMap<>();
        followSets = new HashMap<>();
        grammar = new Grammar(filePath);
    }

    private void setFirstSets(){
        for(String nonterm: grammar.getSetOfNonterminals()){

            firstSets.put(nonterm, firstOf(nonterm));
            System.out.println(nonterm);
        }
    }
    private Set<String> firstOf(String nonterminal){
        if(firstSets.containsKey(nonterminal))
            return firstSets.get(nonterminal);

        Set<String> result = new HashSet<>();
        List<String> terminals = grammar.getSetOfTerminals();

        List<List<String>> productions = grammar.getProductionsOfNonterminal(nonterminal);
        for(List<String> production: productions){
            String firstSymbol = production.get(0);
            if(firstSymbol.equals("epsilon"))
                result.add("epsilon");
            else if(terminals.contains(firstSymbol))
                result.add(firstSymbol);
            else{
                result.addAll(firstOf(firstSymbol));
            }

        }
        return result;
    }

    private void setFollowSets() {
        for (String nonterm : grammar.getSetOfNonterminals()) {
            followSets.put(nonterm, followOf(nonterm));
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

                        // Add everything in FIRST(nextSymbol) except epsilon to FOLLOW(nonterm)
                        result.addAll(firstOfNextSymbol);
                        result.remove("epsilon");

                        // If epsilon is in FIRST(nextSymbol), add FOLLOW(symbol) to FOLLOW(nonterm)
                        if (firstOfNextSymbol.contains("epsilon")) {
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




    public static void main(String[] args) {
        Parser parser = new Parser("src\\g1.txt");
        parser.setFirstSets();

        System.out.println("first" + parser.firstSets);

        parser.setFollowSets();
        System.out.println("follows: " + parser.followSets);

    }

}
