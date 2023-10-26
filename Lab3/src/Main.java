import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try{
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

            lexicalAnalyzer.processFile("C:\\Facultate\\Semestrul5\\LFTC\\Formal-Languages-and-Compiler-Design\\Lab3\\src\\files\\p1.txt");
            System.out.println(lexicalAnalyzer.writePIF());
            System.out.println(lexicalAnalyzer.writeST());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
