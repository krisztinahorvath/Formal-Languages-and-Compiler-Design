package Utils;

public class Pair<K, V> {
    private K first;
    private V second;

    public Pair(K _left, V _right) {
        this.first = _left;
        this.second = _right;
    }

    public K getFirst(){
        return first;
    }

    public V getSecond(){
        return second;
    }

    @Override
    public String toString(){
        return "(" + first + ", " + second + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (first != pair.first) return false;
        return second == pair.second;
    }

}

