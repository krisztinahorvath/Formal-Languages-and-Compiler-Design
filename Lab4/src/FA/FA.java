package FA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FA {
    private List<String> states;
    private List<String> alphabet;
    private List<Transition> transitions;
    private String initialState;
    private List<String> finalStates;
    private String filePath;

    public FA(String _filePath) {
        this.filePath = _filePath;
        this.states = new ArrayList<>();
        this.alphabet = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.initialState = "";
        this.finalStates = new ArrayList<>();
        try {
            readFA(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFA(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        Pattern pattern = Pattern.compile("\\{([^}]*)\\}");

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("states=")) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String[] stateArray = matcher.group(1).split(", ");
                    states.addAll(Arrays.asList(stateArray));
                }
            } else if (line.startsWith("initial_state=")) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    initialState = matcher.group(1);
                }
            } else if (line.startsWith("final_states=")) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String[] finalStateArray = matcher.group(1).split(", ");
                    finalStates.addAll(Arrays.asList(finalStateArray));
                }
            } else if (line.startsWith("alphabet=")) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String[] alphabetArray = matcher.group(1).split(", ");
                    alphabet.addAll(Arrays.asList(alphabetArray));
                }
            } else if (line.startsWith("transitions=")) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String transitionsStr = matcher.group(1);
                    String[] transitionArray = transitionsStr.split(";");
                    for (String transition : transitionArray){
                        // remove parenthesis and split
                        String[] parts = transition.substring(1, transition.length() - 1).trim().split(", ");
                        if (parts.length == 3)
                            transitions.add(new Transition(parts[0], parts[1], parts[2]));
                    }
                }
            }
        }
        reader.close();
    }

    public boolean checkAccepted(String sequence){
        List<String> letterList= List.of(sequence.split(""));
        String currentState = initialState;

        for(String ch: letterList){
            boolean acceptedSequence = false;
            for(Transition transition: transitions)
                if(transition.getFrom().equals(currentState) && transition.getElement().equals(ch)){
                    currentState = transition.getTo();
                    acceptedSequence = true;
                    break;
                }
            if(!acceptedSequence)
                return false;
        }

        return finalStates.contains(currentState);
    }

    public void printStates(){
        System.out.println("States: " +  states);
    }

    public void printAlphabet(){
        System.out.println("Alphabet: " + alphabet);
    }

    public void printTransitions() {
        StringBuilder transitionStr = new StringBuilder("Transitions: {");

        for (int i = 0; i < transitions.size(); i++) {
            Transition transition = transitions.get(i);
            transitionStr
                    .append("(")
                    .append(transition.getFrom())
                    .append(", ")
                    .append(transition.getTo())
                    .append(", ")
                    .append(transition.getElement())
                    .append(")");

            if (i < transitions.size() - 1)
                transitionStr.append(";");
        }

        transitionStr.append("}");

        System.out.println(transitionStr);
    }

    public void printInitialState(){
        System.out.println("Initial state: " + initialState);
    }

    public void printFinalStates(){
        System.out.println("Final states: " + finalStates);
    }

}
