package Utils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParseTable {
    private Map<Pair<String, String>, Pair<List<String >, Integer>> table = new HashMap<>();

    public void put(Pair<String, String> key, Pair<List<String>, Integer> value) {
        table.put(key, value);
    }

    public Pair<List<String>, Integer> get(Pair<String, String> key) {
        for (Map.Entry<Pair<String, String>, Pair<List<String>, Integer>> entry : table.entrySet()) {
            if (entry.getValue() != null) {
                Pair<String, String> currentKey = entry.getKey();
                Pair<List<String>, Integer> currentValue = entry.getValue();

                if (currentKey.getFirst().equals(key.getFirst()) && currentKey.getSecond().equals(key.getSecond())) {
                    return currentValue;
                }
            }
        }

        return null;
    }

    public boolean containsKey(Pair<String, String> key) {
        boolean result = false;
        for (Pair<String, String> currentKey : table.keySet()) {
            if (currentKey.getFirst().equals(key.getFirst()) && currentKey.getSecond().equals(key.getSecond())) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Pair<String, String>, Pair<List<String>, Integer>> entry : table.entrySet()) {
            if (entry.getValue() != null) {
                Pair<String, String> key = entry.getKey();
                Pair<List<String>, Integer> value = entry.getValue();

                sb.append("M[").append(key.getFirst()).append(",").append(key.getSecond()).append("] = [")
                        .append(value.getFirst()).append(",").append(value.getSecond()).append("]\n");
            }
        }

        return sb.toString();
    }

}
