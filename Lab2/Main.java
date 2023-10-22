public class Main {
    public static void main(String[] args) {
        SymbolTable identifiersSymbolTable = new SymbolTable(10);
        SymbolTable constantsSymbolTable = new SymbolTable(10);

        Pair<Integer, Integer> position1 = identifiersSymbolTable.addSymbol("variable1");
        System.out.println("id variable1 - " + position1);

        Pair<Integer, Integer> position2 = constantsSymbolTable.addSymbol("'1'"); // string constant
        System.out.println("const '1' - " + position2);

        Pair<Integer, Integer> position3 = constantsSymbolTable.addSymbol("1"); // integer constant
        System.out.println("const 1 - " + position3);

        System.out.println("const 'abc' - " + constantsSymbolTable.addSymbol("'abc'")); // string constant
        System.out.println("const '123' - " + constantsSymbolTable.addSymbol("'123'")); // string constant
        System.out.println("const 123 - " + constantsSymbolTable.addSymbol("123")); // integer constant
        System.out.println("id b - " + identifiersSymbolTable.addSymbol("b"));

        System.out.println("Identifiers" +  identifiersSymbolTable);
        System.out.println("Constants" + constantsSymbolTable);

        try {
            assert identifiersSymbolTable.getSymbolPosition("variable1").equals(position1) : "variable1 doesn't have position " + position1;
            assert constantsSymbolTable.getSymbolPosition("'1'").equals(position2) : "string const 1 doesn't exist in the ST at position " + position2;
            assert constantsSymbolTable.hasSymbol("1"): "const 1 doesn't exist in the ST";
            assert !identifiersSymbolTable.hasSymbol("a") : "identifier a does not exist in the ST";
            assert constantsSymbolTable.hasSymbol("'abc'") : "string const 'abc' does not exist in the ST";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
