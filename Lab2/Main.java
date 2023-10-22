public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(10);

        Pair<Integer, Integer> position1 = symbolTable.addIdentifier("variable1");
        System.out.println("id variable1 - " + position1);

        Pair<Integer, Integer> position2 = symbolTable.addConstant("'1'"); // string constant
        System.out.println("const '1' - " + position2);

        Pair<Integer, Integer> position3 = symbolTable.addConstant("1"); // integer constant
        System.out.println("const 1 - " + position3);

        System.out.println("const 'abc' - " + symbolTable.addConstant("'abc'")); // string constant
        System.out.println("const '123' - " + symbolTable.addConstant("'123'")); // string constant
        System.out.println("const 123 - " + symbolTable.addConstant("123")); // integer constant
        System.out.println("id b - " + symbolTable.addIdentifier("b"));

        System.out.println(symbolTable);

        try {
            assert symbolTable.getIdentifierPosition("variable1").equals(position1) : "variable1 doesn't have position " + position1;
            assert symbolTable.getConstantPosition("'1'").equals(position2) : "string const 1 doesn't exist in the ST at position " + position2;
            assert symbolTable.hasConstant("1"): "const 1 doesn't exist in the ST";
            assert !symbolTable.hasIdentifier("a") : "identifier a does not exist in the ST";
            assert symbolTable.hasConstant("'abc'") : "string const 'abc' does not exist in the ST";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
