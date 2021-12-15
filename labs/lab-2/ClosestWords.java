/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.util.LinkedList;
import java.util.List;

public class ClosestWords {
  // store words with closest distance in closestWords
  LinkedList<String> closestWords = null;

  // last word to be put in the closestWord list
  String lastWord="";

  int closestDistance = -1;

  /**
   *
   * Finds the minimum editing distance between w1 and w2 by finding all distances
   * between all letter combinations between w1 and w2, entering these into matrix
   * a. The result can then be found at a[w1.length()][w1.length()].
   *
   * @param w1
   * @param w2
   * @return minimum editing distance between the words w1 and w2
   */
  int distance(String w1, String w2, int a[][]) {
    int w1Len = w1.length();
    int w2Len = w2.length();

    int same = same(w2, lastWord);

    int rep;
    int del;
    int add;
    for(int i=1; i<=w1Len; i++) {
      for(int j=1+same; j<=w2Len; j++) {
        rep = a[i-1][j-1] + (w1.charAt(i-1) == w2.charAt(j-1)? 0: 1);
        del = a[i-1][j] + 1;
        add = a[i][j-1] + 1;

        a[i][j] = min(rep, del, add);
      }
    }

    // save the most rescent word to be compared to w2
    lastWord = w2;

    return a[w1.length()][w2.length()];
  }

  public ClosestWords(String w, List<String> wordList, int a[][]) {
    for (String s : wordList) {
      int dist = distance(w, s, a);
      // System.out.println("d(" + w + "," + s + ")=" + dist);
      if (dist < closestDistance || closestDistance == -1) {
        closestDistance = dist;
        closestWords = new LinkedList<String>();
        closestWords.add(s);
      }
      else if (dist == closestDistance)
        closestWords.add(s);
    }
  }

  /**
   * Finds minimum of x, y, z
   * @param x
   * @param y
   * @param z
   * @return minimum of x, y, z
   */
  int min(int x, int y, int z){
    return Math.min(x, Math.min(y, z));
  }

  /**
   * Finds the number of first same letters in w1 and w2
   * @param w1
   * @param w2
   * @return the number of equal first letters in w1 and w2
   */
  int same(String w1, String w2) {
    int sameIndex = 0;
    int minLength = Math.min(w1.length(), w2.length());
    while(sameIndex < minLength) {
      if(w1.charAt(sameIndex) == w2.charAt(sameIndex)) {
        sameIndex++;
      } else {
        break;
      }
    }
    return sameIndex;
  }



  int getMinDistance() {
    return closestDistance;
  }

  List<String> getClosestWords() {
    return closestWords;
  }
}
