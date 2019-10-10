/**
 * RndStrings.java
 *   Make a file of random alphnumeric strings, one per line.
 *   java RndStrings n m p 
 *     - make n random strings of length at least m and up to m+p-1
 */
import java.util.Random;

public class RndStrings {
  private static char[] alphabet =
  "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
  private static int ctr = 0;
  private static Random rnd = new Random();

  private static String rndStg(int ln) {
    char[] chars = new char[ln];
    if (ctr % 256 == 0)
      rnd.setSeed(ctr);
    for (int i=0; i<ln; i++)
      chars[i] = alphabet[rnd.nextInt(alphabet.length)];
    return String.format("%04x - %s", ctr++, new String(chars));    
  }

  public static void main(String[] args) {
    if (args.length < 3) {
      System.out.println("Usage: java RndStrings n m p");
      System.out.println("  to make n random strings, m <= length < m+p");
      return;
    }
    int num = Integer.parseInt(args[0]),
        min = Integer.parseInt(args[1]),
        max = Integer.parseInt(args[2]);
    for (int k = 0; k < num; k++)
      System.out.println(rndStg(min+rnd.nextInt(max)));
  }
}
