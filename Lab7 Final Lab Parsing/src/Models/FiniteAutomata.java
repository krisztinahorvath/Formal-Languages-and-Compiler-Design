package Models;

import Utils.Transition;

import java.io.*;
import java.util.*;

public class FiniteAutomata {
    private String initialState;
    private List<String> states;
    private List<String> finalStates;
    private List<String> alphabet;
    private final List<Transition> transitions;

    public FiniteAutomata(String inputFile) {
        this.initialState = "";
        this.states = new ArrayList<>();
        this.finalStates = new ArrayList<>();
        this.alphabet = new ArrayList<>();
        this.transitions = new ArrayList<>();

        initializeElements(inputFile);
    }

    public String getInitialState() {
        return this.initialState;
    }

    public List<String> getStates() {
        return this.states;
    }

    public List<String> getFinalStates() {
        return this.finalStates;
    }

    public List<String> getAlphabet() {
        return this.alphabet;
    }

    public List<Transition> getTransitions() {
        return this.transitions;
    }

    private void initializeElements(String inputFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            this.initialState = br.readLine().trim();

            String statesLine = br.readLine().trim();
            this.states = Arrays.asList(statesLine.split(",\\s*"));

            String finalStatesLine = br.readLine().trim();
            this.finalStates = Arrays.asList(finalStatesLine.split(",\\s*"));

            String alphabetLine = br.readLine().trim();
            this.alphabet = Arrays.asList(alphabetLine.split(",\\s*"));

            String transitionsLine;
            while ((transitionsLine = br.readLine()) != null) {
                String[] transitionParts = transitionsLine.split(",\\s*");
                Transition transition = new Transition(transitionParts[0], transitionParts[1], transitionParts[2]);
                this.transitions.add(transition);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Transition findNextTransition(String currentState, String inputSymbol) {
        for (Transition transition : this.transitions) {
            if (transition.getSourceState().equals(currentState) && transition.getAlphabetValue().equals(inputSymbol))
                return transition;
        }

        return null;
    }

    public boolean isAcceptedByFA(String input) {
        String currentState = this.initialState;

        for (char c : input.toCharArray()) {
            String inputSymbol = String.valueOf(c);

            Transition validTransition = findNextTransition(currentState, inputSymbol);
            if (validTransition == null)
                return false;

            currentState = validTransition.getDestinationState();
        }

        return this.finalStates.contains(currentState);
    }
}
