package FA;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FA fa = new FA("src\\files\\identifiersFA.in");

        System.out.println("0. Exit");
        System.out.println("1. Print states");
        System.out.println("2. Print alphabet");
        System.out.println("3. Print transitions");
        System.out.println("4. Print initial state");
        System.out.println("5. Print final states");
        System.out.println("6. Verify sequence");
        while (true) {
            System.out.println("cmd: ");
            var option = new java.util.Scanner(System.in).nextInt();
            switch (option) {
                case 0:
                    break;
                case 1:
                    fa.printStates();
                    break;
                case 2:
                    fa.printAlphabet();
                    break;
                case 3:
                    fa.printTransitions();
                    break;
                case 4:
                    fa.printInitialState();
                    break;
                case 5:
                    fa.printFinalStates();
                    break;
                case 6:
                    System.out.println("sequence: ");
                    String sequence = new Scanner(System.in).nextLine();
                    if(fa.checkAccepted(sequence))
                        System.out.println("Accepted by the FA.");
                    else System.out.println("Not accepted by the FA.");
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
}