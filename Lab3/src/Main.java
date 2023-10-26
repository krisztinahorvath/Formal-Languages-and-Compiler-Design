import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try{
            String programPath = "C:\\Facultate\\Semestrul5\\LFTC\\Formal-Languages-and-Compiler-Design\\Lab3\\src\\files\\p2.txt";

            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();


            System.out.println(lexicalAnalyzer.processFile(programPath));
            System.out.println(lexicalAnalyzer.writePIF());
            System.out.println(lexicalAnalyzer.writeST());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
