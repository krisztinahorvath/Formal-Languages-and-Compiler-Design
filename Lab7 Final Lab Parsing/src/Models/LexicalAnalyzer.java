package Models;

import Utils.HashTable;
import Utils.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private final ArrayList<String> reservedWords;
    private final ArrayList<String> operators;
    private final ArrayList<String> separators;
    private final HashTable<Object> symbolTable;
    private final ArrayList<Pair<Integer, Pair<Integer, Integer>>> programInternalForm;

    public LexicalAnalyzer() throws IOException {
        this.reservedWords = new ArrayList<>();
        this.operators = new ArrayList<>();
        this.separators = new ArrayList<>();
        this.symbolTable = new HashTable<>(10);
        this.programInternalForm = new ArrayList<>();

        initializeTokenLists();
    }

    public ArrayList<Pair<Integer, Pair<Integer, Integer>>> getProgramInternalForm() {
        return programInternalForm;
    }

    public ArrayList<String> getReservedWords() {
        return reservedWords;
    }

    public ArrayList<String> getOperators() {
        return operators;
    }

    public ArrayList<String> getSeparators() {
        return separators;
    }

    private void initializeTokenLists() throws IOException {
        FileReader fileReader = new FileReader("src\\resources\\token.in");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        int lineNumber = 1;

        while ((line = bufferedReader.readLine()) != null) {
            if (lineNumber < 12)
                this.reservedWords.add(line);
            else if (lineNumber < 24)
                this.operators.add(line);
            else
                this.separators.add(line);

            lineNumber++;
        }

        bufferedReader.close();

        this.separators.add(" ");
    }

    public HashTable<Object> getSymbolTable() {
        return symbolTable;
    }

    public String getSymbolTableToString() {
        return this.symbolTable.toString();
    }

    public String getProgramInternalFormToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Pair<Integer, Pair<Integer, Integer>> entry : this.programInternalForm)
            stringBuilder.append(entry.getFirst()).append(" -> ").append(entry.getSecond()).append("\n");
        return stringBuilder.toString();
    }

    private boolean isReservedWord(String token) {
        return this.reservedWords.contains(token);
    }

    private boolean isOperator(String token) {
        return this.operators.contains(token);
    }

    private boolean isSeparator(String token) {
        return this.separators.contains(token);
    }

    public boolean isIdentifier(String token) {
        /*Pattern pattern = Pattern.compile("#([A-Za-z]+)");
        Matcher matcher = pattern.matcher(token);
        return matcher.matches();*/

        FiniteAutomata fa = new FiniteAutomata("src\\resources\\FA_identifier.in");
        return fa.isAcceptedByFA(token);
    }

    public boolean isConstant(String token) {
        /*Pattern pattern = Pattern.compile("^0$|^-?[1-9][0-9]*$");
        Matcher matcher = pattern.matcher(token);
        if (matcher.matches())
            return true;*/

        FiniteAutomata fa = new FiniteAutomata("src\\resources\\/FA_integer_constant.in");
        if (fa.isAcceptedByFA(token))
            return true;

        Pattern pattern = Pattern.compile("^'[a-zA-Z0-9]'$");
        Matcher matcher = pattern.matcher(token);
        if (matcher.matches())
            return true;

        pattern = Pattern.compile("^\"[a-zA-Z0-9]+\"$");
        matcher = pattern.matcher(token);
        return matcher.matches();
    }

    int getTokenCode(String token) {
        if (isIdentifier(token))
            return -1;
        else if (isConstant(token))
            return -2;
        else {
            if (Objects.equals(token, " "))
                return 32;
            else if (this.reservedWords.contains(token))
                return this.reservedWords.indexOf(token) + 1;
            else if (this.operators.contains(token))
                return this.operators.indexOf(token) + 12;
            else if (this.separators.contains(token))
                return this.separators.indexOf(token) + 24;
        }
        return 0;
    }

    public ArrayList<Pair<String, Integer>> detectTokensByChar(String inputFile) throws IOException {
        FileReader fileReader = new FileReader(inputFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        int lineNumber = 1;
        ArrayList<Pair<String, Integer>> tokens = new ArrayList<>();

        while ((line = bufferedReader.readLine()) != null) {
            int i = 0;
            String previousCharacters = "";
            String currentCharacter = "";
            line = line.trim();

            while (i < line.length()) {
                currentCharacter = String.valueOf(line.charAt(i));

                if (isSeparator(currentCharacter)) {
                    if (previousCharacters.length() > 0)
                        tokens.add(new Pair<>(previousCharacters, lineNumber));
                    if (!currentCharacter.equals(" "))
                        tokens.add(new Pair<>(currentCharacter, lineNumber));
                    i++;
                    previousCharacters = "";
                }

                else if (i + 1 < line.length() && isOperator(currentCharacter) && (!isOperator(String.valueOf(line.charAt(i + 1))) || String.valueOf(line.charAt(i + 1)).equals("-"))) {
                    if (isOperator(String.valueOf(line.charAt(i - 1)) + currentCharacter)) {
                        if (previousCharacters.length() > 1)
                            tokens.add(new Pair<>(previousCharacters.substring(0, previousCharacters.length() - 1), lineNumber));
                        tokens.add(new Pair<>(String.valueOf(line.charAt(i - 1)) + currentCharacter, lineNumber));
                        i++;
                        previousCharacters = "";
                    }
                    else {
                        if (previousCharacters.length() > 0)
                            tokens.add(new Pair<>(previousCharacters, lineNumber));
                        if (isIdentifier(previousCharacters) || isConstant(previousCharacters)) {
                            tokens.add(new Pair<>(currentCharacter, lineNumber));
                            i++;
                            previousCharacters = "";
                        }
                        else {
                            previousCharacters += currentCharacter;
                            i++;
                        }
                    }
                }
                else if (i == line.length() - 1) {
                    if (previousCharacters.length() > 0)
                        tokens.add(new Pair<>(previousCharacters + currentCharacter, lineNumber));
                    else
                        tokens.add(new Pair<>(currentCharacter, lineNumber));
                    i++;
                    previousCharacters = "";
                }
                else {
                    previousCharacters += currentCharacter;
                    i++;
                }
            }

            lineNumber++;
        }

        return tokens;
    }

    public void scanProgram(String inputFile, String outputFileST, String outputFilePIF) throws IOException {
        ArrayList<Pair<String, Integer>> tokens = detectTokensByChar(inputFile);
        StringBuilder lexicalError = new StringBuilder();

        for (Pair<String, Integer> t : tokens) {
            String token = t.getFirst();
            int line = t.getSecond();

            if (isReservedWord(token) || isOperator(token) || isSeparator(token))
                this.programInternalForm.add(new Pair<>(getTokenCode(token), new Pair<>(-1, -1)));
            else if (isIdentifier(token) || isConstant(token)) {
                this.symbolTable.add(token);
                this.programInternalForm.add(new Pair<>(getTokenCode(token), this.symbolTable.search(token)));
            }
            else
                lexicalError.append("Lexical error at line ").append(line).append(" for token ").append(token).append("\n");
        }

        if (lexicalError.length() != 0)
            System.out.println(lexicalError);
        else
            System.out.println("Lexically correct");

        BufferedWriter stWriter = new BufferedWriter(new FileWriter(outputFileST));
        stWriter.write(getSymbolTableToString());
        stWriter.close();

        BufferedWriter pifWriter = new BufferedWriter(new FileWriter(outputFilePIF));
        pifWriter.write(getProgramInternalFormToString());
        pifWriter.close();
    }
}
