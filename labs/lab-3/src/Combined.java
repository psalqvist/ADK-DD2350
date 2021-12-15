import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Step 3 out of 3
 * Reduce bipartite match to maximal flow
 * by making a function call to step 3
 *
 * Run from terminal (On OSX):
 *
 * ./../help-files/OSX/bipgen 5000 5000 10000 > graph.txt
 *
 * java Combined < graph.txt > match.txt
 *
 * Run from terminal (On other):
 *
 * ./../help-files/bipgen 5000 5000 10000 > graph.txt
 *  *
 *  * java Combined < graph.txt > match.txt
 *
 * ./../help-files/OSX/combine java MatchReduce \ ./../help-files/OSX/maxflow < [graphfile] > [matchfile]
 *
 *
 * @author Philip Salqvist
 * @version 2021-10
 */

public class Combined {
    Kattio io;

    public static void main(String args[]) {
        new Combined();
    }

    Combined() {
        io = new Kattio(System.in);

        readBipartiteGraph();

        // debugutskrift
        // System.err.println("Bipred avslutar\n");

        // Kom ihåg att stänga ner Kattio-klassen
        io.close();
    }

    void readBipartiteGraph() {
        // Läs antal hörn och kanter
        int x = io.getInt();
        int y = io.getInt();
        int e = io.getInt();

        int[][] edges = new int[e][2];
        // Läs in kanterna
        for (int i = 0; i < e; ++i) {
            edges[i][0] = io.getInt();
            edges[i][1] = io.getInt();

        }

        writeFlowGraph(x, y, e, edges);
    }

    void writeFlowGraph(int x, int y, int e, int[][] edges) {
        int v = x + y + 2, s = 1, t = v, nrOfEdges = e + x + y;

        StringBuilder sb = new StringBuilder();

        // Skriv ut antal hörn och kanter samt källa och sänka
        sb.append(v + "\n").append(s + " " + t + "\n").append(nrOfEdges + "\n");
        int a = 1, b, c = 1;
        for (int i = 2; i <= x+1; i++) {
            b = i;
            // Kant från a till b med kapacitet c
            sb.append(a + " " + b + " " + c + "\n");
        }
        for (int i = 0; i < e; ++i) {
            a = edges[i][0]+1;
            b = edges[i][1]+1;
            // Kant från a till b med kapacitet c
            sb.append(a + " " + b + " " + c + "\n");
        }
        b = t;
        for (int i = x+2; i < t; i++) {
            a = i;
            // Kant från a till b med kapacitet c
            sb.append(a + " " + b + " " + c + "\n");
        }

        String maxFlow = new FlowStep3(sb.toString()).getSolution();
        /*System.err.println(maxFlow);*/
        readMaxFlowWriteMatch(maxFlow, x, y);
    }


    void readMaxFlowWriteMatch(String maxFlow, int x, int y) {
        // Läs in antal hörn, kanter, källa, sänka, och totalt flöde
        // (Antal hörn, källa och sänka borde vara samma som i grafen vi
        // skickade iväg)

        InputStream is = new ByteArrayInputStream(maxFlow.getBytes());
        io = new Kattio(is);

        int v = io.getInt();
        int s = io.getInt();
        int t = io.getInt();
        int totflow = io.getInt();
        int e = io.getInt();
        int a, b, f;

        // Skriv bipartit matchning till stndout
        System.out.println(x + " " + y);
        System.out.println(totflow);
        for (int i = 0; i < e; ++i) {
            // Flöde f från a till b
            a = io.getInt();
            b = io.getInt();
            f = io.getInt();

            if(a!=s && b!=t) {
                // Kant mellan a och b ingår i vår matchningslösning. Minska med 1 då reduktionen ökade med 1.
                System.out.println((a-1) + " " + (b-1));
            }
        }
    }
}