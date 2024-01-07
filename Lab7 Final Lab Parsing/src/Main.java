import Models.LexicalAnalyzer;
import Models.Parser;
import Models.ParserOutput;
import Utils.ParseTreeNode;

import java.io.IOException;
import java.util.List;

import static Models.Parser.processProgramInternalForm;

public class Main {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("src\\resources\\g2.txt");
        parser.setFirstSets();
        System.out.println("FIRST");
        for(String key: parser.firstSets.keySet())
            System.out.println(key + "=" + parser.firstSets.get(key));

        parser.setFollowSets();
        System.out.println("\n\nFOLLOW");
        for(String key: parser.followSets.keySet())
            System.out.println(key + "=" + parser.followSets.get(key));

        System.out.println("\n\nPARSE TABLE:");
        parser.createParseTable();
        System.out.println(parser.getParseTable());

//        List<String> sequence = List.of("a", "*", "(", "a", "+", "a", ")");
//        List<String> sequence = readSequence();
        LexicalAnalyzer scanner = new LexicalAnalyzer();
        scanner.scanProgram("src\\resources\\p1.in", "src\\resources\\p1_ST.out", "src\\resources\\p1_PIF.out");
        List<String> sequence = processProgramInternalForm(scanner.getProgramInternalForm(), scanner);
        System.out.println(sequence);

        if (parser.parse(sequence, scanner))
            System.out.println("Sequence accepted!");
        else
            System.out.println("Sequence not accepted!");

        ParseTreeNode parseTreeRoot = parser.getParseTreeRoot();
        System.out.println("\n\nParsing tree: ");
        System.out.println(parseTreeRoot.toStringTree());

        // convert parsing tree to table
        ParserOutput parserOutput = new ParserOutput(parseTreeRoot);

        // display the resulting table
        parserOutput.displayTableRecords();
        parserOutput.writeToFile("src\\outputs\\out2.txt");
    }
}
