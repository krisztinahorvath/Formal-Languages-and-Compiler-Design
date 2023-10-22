public class SymbolTable {
    private HashTable<String> identifiersTable;
    private HashTable<String> constantsTable;

    public SymbolTable(int _nrBuckets){

        identifiersTable = new HashTable<>(_nrBuckets);
        constantsTable = new HashTable<>(_nrBuckets);
    }

    public Pair<Integer, Integer> addIdentifier(String identifier){
        return identifiersTable.add(identifier);
    }

    public Pair<Integer, Integer> addConstant(String constant) {
        return constantsTable.add(constant);
    }

    public boolean hasIdentifier(String identifier){
        return identifiersTable.contains(identifier);
    }

    public boolean hasConstant(String constant){
        return constantsTable.contains(constant);
    }

    public Pair<Integer, Integer> getIdentifierPosition(String identifier){
        return identifiersTable.getPosition(identifier);
    }

    public Pair<Integer, Integer> getConstantPosition(String constant){
        return constantsTable.getPosition(constant);
    }

    @Override
    public String toString(){
        return "\nSymbolTable:" +
                "\nidentifiersTable: " + identifiersTable +
                "\nconstantsTable: " + constantsTable;
    }


}
