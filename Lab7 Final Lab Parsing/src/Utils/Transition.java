package Utils;

public class Transition {
    private final String sourceState;
    private final String alphabetValue;
    private final String destinationState;

    public Transition(String sourceState, String alphabetValue, String destinationState) {
        this.sourceState = sourceState;
        this.alphabetValue = alphabetValue;
        this.destinationState = destinationState;
    }

    public String getSourceState() {
        return this.sourceState;
    }

    public String getAlphabetValue() {
        return this.alphabetValue;
    }

    public String getDestinationState() {
        return this.destinationState;
    }

    @Override
    public String toString() {
        return "(" + sourceState + ", " + alphabetValue + ", " + destinationState + ")";
    }
}
