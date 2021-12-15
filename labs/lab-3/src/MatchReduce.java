/**
 * Step 1 out of 3
 * Reduce bipartite match to maximal flow
 * by using combine exec as a black box
 *
 * Run from terminal:
 *
 * ./../help-files/OSX/combine java MatchReduce \ ./../help-files/OSX/maxflow < [graphfile] > [matchfile]
 *
 *
 * @author Philip Salqvist
 * @version 2021-10
 */

public class MatchReduce {
    Kattio io;

	public static void main(String args[]) {
		new MatchReduce();
    }

	MatchReduce() {
		io = new Kattio(System.in, System.out);
		
		readBipartiteGraph();
	
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

		// Skriv ut antal hörn och kanter samt källa och sänka
		io.println(v);
		io.println(s + " " + t);
		io.println(nrOfEdges);
		int a = 1, b, c = 1;
		for (int i = 2; i <= x+1; i++) {
			b = i;
			// Kant från a till b med kapacitet c
			io.println(a + " " + b + " " + c);
		}
		for (int i = 0; i < e; ++i) {
			a = edges[i][0]+1;
			b = edges[i][1]+1;
			// Kant från a till b med kapacitet c
			io.println(a + " " + b + " " + c);
		}
		b = t;
		for (int i = x+2; i < t; i++) {
			a = i;
			// Kant från a till b med kapacitet c
			io.println(a + " " + b + " " + c);
		}

		// Var noggrann med att flusha utdata när flödesgrafen skrivits ut!
		io.flush();
		// Debugutskrift
		// System.err.println("Skickade iväg flödesgrafen");

		readMaxFlowWriteMatch(x, y);
	}

    void readMaxFlowWriteMatch(int x, int y) {
		// Läs in antal hörn, kanter, källa, sänka, och totalt flöde
		// (Antal hörn, källa och sänka borde vara samma som i grafen vi
		// skickade iväg)

		int v = io.getInt();
		int s = io.getInt();
		int t = io.getInt();
		int totflow = io.getInt();
		int e = io.getInt();
		int a, b, f;

		// Skriv bipartit matchning till stndout
		io.println(x + " " + y);
		io.println(totflow);
		for (int i = 0; i < e; ++i) {
			// Flöde f från a till b
			a = io.getInt();
			b = io.getInt();
			f = io.getInt();

			if(a!=s && b!=t) {
				// Kant mellan a och b ingår i vår matchningslösning. Minska med 1 då reduktionen ökade med 1.
				io.println((a-1) + " " + (b-1));
			}
		}
		io.flush();
    }
}

