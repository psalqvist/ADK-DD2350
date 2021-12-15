/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Main {
  // describes closest distances between w1 and w2
  static int[][] a = new int[50][50];

  public static List<String> readWordList(BufferedReader input) throws IOException {
    LinkedList<String> list = new LinkedList<String>();
    while (true) {
      String s = input.readLine();
      if (s.equals("#"))
        break;
      list.add(s);
    }
    return list;
  }

  public static void main(String args[]) throws IOException {
    // when length of word2=0, subtract from word1
    for (int i=0; i<a.length; i++) {
      a[i][0] = i;
    }

    // when length of word1=0, add to word1
    for (int j=0; j<a.length; j++){
      a[0][j] = j;
    }
    //    long t1 = System.currentTimeMillis();
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    // Säkrast att specificera att UTF-8 ska användas, för vissa system har annan
    // standardinställning för teckenkodningen.
    List<String> wordList = readWordList(stdin);
    String word;
    while ((word = stdin.readLine()) != null) {
      StringBuilder sb = new StringBuilder();
      ClosestWords closestWords = new ClosestWords(word, wordList, a);
      sb.append(word).append(" (").append(closestWords.getMinDistance()).append(")");
      for (String w : closestWords.getClosestWords())
        sb.append(" ").append(w);
      System.out.println(sb);
    }
    //    long tottime = (System.currentTimeMillis() - t1);
    //    System.out.println("CPU time: " + tottime + " ms");

  }
}


