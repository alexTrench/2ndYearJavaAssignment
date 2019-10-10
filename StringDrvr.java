/**
 * TimedListOpsStringDrvr.java
 *   Driver for TimedListOps<E> using String for E
 */
import java.util.*;
import java.io.*;
import java.util.Random;
public class StringDrvr {

    private static List<String> loadData(char mode, String path) throws IOException {
        List<String> data = null;
        // First checks what mode it is in (ArrayList or Linked List)
        if (mode == 'a') {
            System.out.println("ArrayList<String>");
            data = new ArrayList<String>();
        } else if (mode == 'l') {
            System.out.println("LinkedList<String>");
            data = new LinkedList<String>();
        } 
        // If the user enters a mode that isn't recognised
        else {
            System.out.println("List mode unrecognised");
            System.exit(0);
        }
        // Reads the text file which contains the strings and puts them into the list chosen
        Scanner input = new Scanner(new BufferedReader(new FileReader(path)));
        while (input.hasNextLine())
            data.add(input.nextLine());
        // This prints out the size of the list
        System.out.printf("%d items\n", data.size());
        input.close();
        return data;
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java StringDrvr {a|l} <file path>");
            return;
        }
        TimedListOps<String> app
        = new TimedListOps<String>(loadData(args[0].charAt(0), args[1]));
        System.out.println();
        System.out.print("Fetch (Sequencial Access): ");
        // This accesses 100 random strings from the list and
        // times how long it takes for each
        for (int i = 0; i < 100; i++) {
            app.fetch(app.dLen()-1000+5*i);
        }
        System.out.println();
        System.out.print("Insert:");
        // This times how long it takes to insert at a sequential place
        for (int i = 0; i < 100; i++) {
            app.insert(1000-5*i, "******* insert *******");
        }
        System.out.println();
        System.out.print("Delete:");
        // This times how long it takes to delete at a squenctial  place

        for (int i = 0; i < 100; i++) {
            app.delete(1000-5*i);
        }
        System.out.println();
        System.out.print("Fetch every 500: ");
        // This fetches every 250 items
        int m = 1;
        for (int i = 1; i < 100; i++){
            app.fetch(m);
            m = m + 250;
        }
        
        System.out.println();
        Random rand = new Random();
        
        System.out.print("Fetch (Random Access): ");
        // This accesses 100 random strings from the list and
        // times how long it takes for each
        for (int i = 0; i < 100; i++) {
            int n = rand.nextInt(150000)+1;
            app.fetch(app.dLen()-n);
        }
        
        
        System.out.println();
        System.out.print("Insert:");
        // This times how long it takes to insert at a random place
        for (int i = 0; i < 100; i++) {
            int n = rand.nextInt(150000)+1;
            app.insert(n, "******* insert *******");
        }
        
        System.out.println();
        System.out.print("Delete:");
        // This times how long it takes to delete at a random place

        for (int i = 0; i < 100; i++) {
            int n = rand.nextInt(150000)+1;
            app.delete(n);
        }
    } 
}
