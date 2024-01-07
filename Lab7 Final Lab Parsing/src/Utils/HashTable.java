package Utils;

import java.util.ArrayList;

public class HashTable<T> {
    private final int size;
    private final ArrayList<ArrayList<T>> hashTable;

    public HashTable(int size) {
        this.size = size;
        this.hashTable = new ArrayList<>();
        for (int i = 0; i < size; i++)
            this.hashTable.add(new ArrayList<>());
    }

    public T searchByPosition(Pair<Integer, Integer> position) {
        int row = position.getFirst();
        int col = position.getSecond();

        if (row >= 0 && row < this.size && col >= 0 && col < this.hashTable.get(row).size()) {
            return this.hashTable.get(row).get(col);
        }
        return null;  // Or throw an exception, depending on your requirements
    }

    private int hash(T key) {
        if (key instanceof Integer)
            return ((Integer) key) % this.size;
        else
            return Math.abs(key.hashCode()) % this.size;
    }

    public void add(T key) {
        int hashIndex = hash(key);
        if (!this.hashTable.get(hashIndex).contains(key))
            this.hashTable.get(hashIndex).add(key);
    }

    public Pair<Integer, Integer> search(T key) {
        int hashIndex = hash(key);
        if (this.hashTable.get(hashIndex).contains(key))
            return new Pair<>(hashIndex, this.hashTable.get(hashIndex).indexOf(key));
        return new Pair<>(-1, -1);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.size; i++) {
            ArrayList<T> list = this.hashTable.get(i);
            for (int j = 0; j < list.size(); j++) {
                T key = list.get(j);
                stringBuilder.append(key).append(" -> [").append(i).append(",").append(j).append("]\n");
            }
        }
        return stringBuilder.toString();
    }
}
