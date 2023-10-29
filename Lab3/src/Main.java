import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try{
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

            System.out.println(lexicalAnalyzer.processFile("src\\files\\p1.txt"));
            System.out.println(lexicalAnalyzer.writePIF());
            System.out.println(lexicalAnalyzer.writeST());

            System.out.println(lexicalAnalyzer.processFile("src\\files\\p2.txt"));
            System.out.println(lexicalAnalyzer.writePIF());
            System.out.println(lexicalAnalyzer.writeST());

            System.out.println(lexicalAnalyzer.processFile("src\\files\\p3.txt"));
            System.out.println(lexicalAnalyzer.writePIF());
            System.out.println(lexicalAnalyzer.writeST());

//            System.out.println(lexicalAnalyzer.processFile("src\\files\\p1err.txt"));
//            System.out.println(lexicalAnalyzer.writePIF());
//            System.out.println(lexicalAnalyzer.writeST());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
