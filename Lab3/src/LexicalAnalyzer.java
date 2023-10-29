import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private SymbolTable identifiersSymbolTable;
    private SymbolTable constantsSymbolTable;
    private ArrayList<Pair<String, Integer>> reservedWords;
    private ArrayList<Pair<String, Integer>> operators;
    private  ArrayList<Pair<Character, Integer>> separators;
    private ArrayList<Pair<Integer, Pair<Integer, Integer>>> PIF;

    public LexicalAnalyzer() throws IOException {
        identifiersSymbolTable = new SymbolTable(15);
        constantsSymbolTable = new SymbolTable(15);
        reservedWords = new ArrayList<>();
        operators = new ArrayList<>();
        separators = new ArrayList<>();
        PIF = new ArrayList<>();

        readTokenFile("src\\files\\token.in");
    }

    private void readTokenFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNo = 1;

            separators.add(new Pair<>(' ', 11));
            while ((line = reader.readLine()) != null) {
                if (lineNo >= 3 && lineNo <= 11) {
                    if (!line.isEmpty()) {
                        // Separator
                        separators.add(new Pair<>(line.charAt(0), lineNo));
                    }
                } else if (lineNo >= 12 && lineNo <= 22) {
                    if (!line.isEmpty()) {
                        // Operator
                        operators.add(new Pair<>(line.trim(), lineNo));
                    }
                } else if (lineNo >= 23 && lineNo <= 31) {
                    if (!line.isEmpty()) {
                        // Reserved word
                        reservedWords.add(new Pair<>(line.trim(), lineNo));
                    }
                }
                lineNo++;
            }
        } catch (IOException ex) {
            throw ex;
        }
    }


    private int getReservedWordCode(String word){
        for(Pair<String, Integer> pair: reservedWords){
            if(pair.getFirst().equals(word))
                return pair.getSecond();
        }

        return -1;
    }

    private int getSeparatorsCode(char sep){
        for(Pair<Character, Integer> pair: separators)
            if (pair.getFirst().equals(sep))
                return pair.getSecond();

        return -1;
    }

    private int getOperatorsCode(String op){
        for(Pair<String, Integer> pair: operators)
            if(pair.getFirst().equals(op))
                return pair.getSecond();

        return -1;
    }

    public String writePIF(){
        return "Program Internal Form\n" + PIF.toString();
    }

    public String writeST(){

        return "\nIdentifiers symbol table " + identifiersSymbolTable.toString() +
                "\n\nConstants symbol table " + constantsSymbolTable.toString();
    }

    // todo move things to smaller functions
    public String processFile(String filePath) throws IOException {
        clear(); // clear PIF and STs

        FileReader inputStream = null;

        String identifierRegex = "^[A-Za-z][A-Za-z0-9]*$"; // must start with small/capital letter, can have any number
        Pattern identifierPattern = Pattern.compile(identifierRegex);

        String integerConstantRegex = "^[+-]?[0-9]\\d*$"; // can i have 02, -02??
        Pattern integerConstantPattern = Pattern.compile(integerConstantRegex);

        String stringConstantRegex = "^\"[\\w\\d+\\-=. ?!]*\"$";
        Pattern stringConstantPattern = Pattern.compile(stringConstantRegex);

        String whitespaceCharacters = "\t\n\r\f\u000B";
        StringBuilder lexicalErrors = new StringBuilder();

        try{
            inputStream = new FileReader(filePath);
            int c;
            String atom = "";
            boolean insideStringConst = false;
            int lineNo = 1;

            while( (c = inputStream.read())  != -1){ // will it work for the last read input??
                // read until the first separator then check if it's a reserved word or an operator
                char character = (char) c;

                if(insideStringConst) {
                    if(character == '"') // end of string const
                        insideStringConst = false;
                    atom += character;
                }
                else if (character == '"') {
                    insideStringConst = true;
                    atom += character;
                }
                else{ // todo check if separator or operator
                    int separatorCode = getSeparatorsCode((char) c);

                    if (separatorCode != -1){ // it is a separator
                        // check the previously formed atom, if it is a reserved word or an operator, add it to PIF with ST_pos = 0
                        int reservedWordCode = getReservedWordCode(atom);
                        if (reservedWordCode != -1){
                            PIF.add(new Pair<>(reservedWordCode, new Pair<>(-1, -1)));
                        }

                        else if(!atom.isEmpty()){
                            int operatorCode = getOperatorsCode(atom); // check if it is an operator
                            if(operatorCode != -1)
                                PIF.add(new Pair<>(operatorCode, new Pair<>(-1, -1)));

                            else {
                                // check if it is an identifier or a constant
                                Matcher matcher = identifierPattern.matcher(atom);
                                if(matcher.matches()){ // it is an identifier
                                    // check if it's in the ST and get its pos, otherwise add it
                                    Pair<Integer, Integer> atomPosST;
                                    if (!identifiersSymbolTable.hasSymbol(atom)) // doesn't exist we need to add it in the ST
                                        atomPosST = identifiersSymbolTable.addSymbol(atom);
                                    else atomPosST = identifiersSymbolTable.getSymbolPosition(atom);

                                    // add to PIF the identifier, code = 1 for ids
                                    PIF.add(new Pair<>(1, atomPosST)); // just the pos of the first bucket for ST
                                }
                                else{ // check if it is a constant
                                    matcher = integerConstantPattern.matcher(atom);
                                    Pair<Integer, Integer> atomPosST;

                                    if (matcher.matches()){ // it is an integer constant
                                        if (!constantsSymbolTable.hasSymbol(atom)) // doesn't exist we need to add it in the ST
                                            atomPosST = constantsSymbolTable.addSymbol(atom);
                                        else atomPosST = constantsSymbolTable.getSymbolPosition(atom);

                                        // add to PIF the identifier, code = 2 for const
                                        PIF.add(new Pair<>(2, atomPosST)); // just the pos of the first bucket for ST
                                    }
                                    else {
                                        matcher = stringConstantPattern.matcher(atom);
                                        if (matcher.matches()){ // it is a string constant
                                            if (!constantsSymbolTable.hasSymbol(atom)) // doesn't exist we need to add it in the ST
                                                atomPosST = constantsSymbolTable.addSymbol(atom);
                                            else atomPosST = constantsSymbolTable.getSymbolPosition(atom);

                                            // add to PIF the identifier, code = 2 for const
                                            PIF.add(new Pair<>(2, atomPosST)); // just the pos of the first bucket for ST

                                        }
                                        else lexicalErrors.append("lexical error: line ").append(lineNo).append(", token ").append(atom).append("\n"); // on which line is the atom?????
                                    }
                                }
                            }
                        }
                        if (character != ' ') // if it's not space, only then add it to the PIF
                            PIF.add(new Pair<>(separatorCode, new Pair<>(-1, -1)));
                        atom = "";
                    }
                    else if(whitespaceCharacters.indexOf(c) == -1)
                        atom += character;

                    if(character == '\n') lineNo++;
                }
            }

            if (!lexicalErrors.isEmpty())
                return "Lexically incorrect file: " + filePath + "\n" + lexicalErrors.toString();
            else {
                // todo write to file PIF and ST
                return "Lexically correct file: " + filePath;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(inputStream != null)
                inputStream.close();
        }
    }

    private void clear(){
        PIF.clear();
        identifiersSymbolTable.clear();
        constantsSymbolTable.clear();
    }
}
