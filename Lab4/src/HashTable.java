import java.util.ArrayList;
import java.util.Objects;

public class HashTable<T> {
    private int nrBuckets;
    private ArrayList<ArrayList<T>> table;

    public HashTable(int _nrBuckets){
        nrBuckets = _nrBuckets;
        table = new ArrayList<>();

        // create the empty chains
        for (int i = 0; i < nrBuckets; i++)
            table.add(new ArrayList<>());
    }

    public int hashFunction(T key) {
        try{
            int hashCode = Objects.hashCode(Integer.parseInt(key.toString()));
            return Math.abs(hashCode % nrBuckets);

        } catch (NumberFormatException err){
            int hashCode = Objects.hashCode(key);
            return Math.abs(hashCode % nrBuckets);
        }
    }

    public Pair<Integer, Integer> add(T key) {
        int hashValue = hashFunction(key);
        if (!table.get(hashValue).contains(key)) {
            table.get(hashValue).add(key);
            return new Pair<>(hashValue, table.get(hashValue).indexOf(key));
        }
        return null;
    }

    public boolean contains(T key) {
        int hashValue = hashFunction(key);
        return table.get(hashValue).contains(key);
    }

    public Pair<Integer, Integer> getPosition(T key) {
        if (this.contains(key)) {
            int hashValue = hashFunction(key);
            return new Pair<>(hashValue, table.get(hashValue).indexOf(key));
        }
        return new Pair<>(-1, -1);
    }



    public void clear() {
        for (ArrayList<T> chain : table) {
            chain.clear();
        }
    }

    @Override
    public String toString(){
        return "HashTable{items=" + table.toString() +"}";
    }
}
