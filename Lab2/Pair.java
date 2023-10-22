public class Pair<K, V> {
    int bucketIndex;
    int position;

    Pair(int bucketIndex, int position) {
        this.bucketIndex = bucketIndex;
        this.position = position;
    }

    int getBucketIndex(){
        return bucketIndex;
    }

    int getPosition(){
        return position;
    }

    @Override
    public String toString(){
        return "(" + bucketIndex + ", " + position + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (bucketIndex != pair.bucketIndex) return false;
        return position == pair.position;
    }

}

