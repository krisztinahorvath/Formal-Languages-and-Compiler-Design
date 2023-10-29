public class SymbolTable {
    private HashTable<String> symbolTable;

    public SymbolTable(int _nrBuckets){
        symbolTable = new HashTable<>(_nrBuckets);
    }

    public Pair<Integer, Integer> addSymbol(String identifier){
        return symbolTable.add(identifier);
    }

    public boolean hasSymbol(String identifier){
        return symbolTable.contains(identifier);
    }

    public Pair<Integer, Integer> getSymbolPosition(String identifier){
        return symbolTable.getPosition(identifier);
    }

    public void clear(){
        symbolTable.clear();
    }
    @Override
    public String toString(){
        return symbolTable.toString();
    }


}
