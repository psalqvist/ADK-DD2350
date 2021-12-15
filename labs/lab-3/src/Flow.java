import java.util.*;

/**
 * Step 2 out of 3
 *
 * Solves maximal flow. Reads from standard input and writes solution to standard output.
 *
 * Run from terminal:
 *
 * java Flow < ./../help-files/testfall/residualtest.indata > [output-file]
 *
 *
 * @author Philip Salqvist
 * @version 2021-10
 */

public class Flow {
    Kattio io;

    public static void main(String[] args) {
        new Flow();
    }

    Flow() {
        io = new Kattio(System.in, System.out);

        int v = io.getInt();
        int s = io.getInt();
        int t = io.getInt();
        int e = io.getInt();

        initGraph(v, e);

        // vertices are numbered 1 to v, so we need a size of v+1
        Edge parents[] = new Edge[v+1];

        // total flow is initially zero
        int totFlow = 0;

        // if there is a path in residual graph, continue
        while(bfs(v, s, t, parents)) {
            Edge currentEdge = parents[t];

            // get the min residual capacity from the path found in bfs
            int pathFlow = minResCap(currentEdge, parents, s);
            // update total flow with flow of the current path
            totFlow += pathFlow;
            while(true) {
                currentEdge.flow += pathFlow;
                currentEdge.backEdge.flow = -currentEdge.flow;
                currentEdge.resCap = currentEdge.cap - currentEdge.flow;
                currentEdge.backEdge.resCap = currentEdge.backEdge.cap - currentEdge.backEdge.flow;

                // if next vertex is the sink, we are done
                if(currentEdge.from == s) {
                    break;
                }

                // if not, we update current edge to it's parent and continue
                currentEdge = parents[currentEdge.from];
            }

        }

        StringBuilder sb = new StringBuilder();
        int posEdges = 0;
        for (Edge edge : graph) {
            if(edge.flow > 0) {
                sb.append(edge.from).append(" ").append(edge.to).append(" ").append(edge.flow).append("\n");
                posEdges++;
            }
        }
        System.out.println(v  + "\n" + s + " " + t + " " + totFlow + "\n" + posEdges);
        System.out.print(sb);

        io.close();
    }

    /**
     *
     * @param currentEdge
     * @param parents
     * @param s
     * @return minimum residual capacity in the path found in parents array
     */
    int minResCap(Edge currentEdge, Edge[] parents, int s) {
        if(currentEdge.from == s) {
            return currentEdge.resCap;
        }
        return Math.min(currentEdge.resCap, minResCap(parents[currentEdge.from], parents, s));
    }

    /**
     * Initialize graph as an Array of HashMaps.
     * Every index in the array corresponds to a vertex, which holds a HashMap containing neighbors.
     * @param v
     * @param e
     */
    // original graph edges
    Edge[] graph;
    // Residual graph representation as array of HashMaps
    Map<Integer, Edge>[] rGraph;
    void initGraph(int v, int e) {
        // Every index in the array is a vertex that holds a Map with neighbors
        rGraph = new Map[v + 1];;
        for(int i = 0; i <= v; i++) {
            rGraph[i] = new HashMap<>();
        }
        graph = new Edge[e];
        int from, to, cap;
        for(int i = 0; i < e; i++) {
            from = io.getInt();
            to = io.getInt();
            cap = io.getInt();

            Edge edge = rGraph[from].get(to);

            if(edge == null) { // if edge don't exist in residual graph
                Edge frontEdge = new Edge(from, to, cap);
                Edge backEdge = new Edge(to, from, 0);
                frontEdge.backEdge = backEdge;
                backEdge.backEdge = frontEdge;
                graph[i] = frontEdge;
                rGraph[from].put(to, frontEdge);
                rGraph[to].put(from, backEdge);
            } else { // if edge already exists, it's an inverse edge that needs to be updated and saved as original edge
                edge.resCap = cap;
                edge.cap = cap;
                graph[i] = edge;
            }
        }
    }

    /**
     *
     * @param v number of vertices in graph
     * @param s sink
     * @param t target
     * @param parents holds the minimum path from sink to target
     * @return true if there is a path, false if there is not a path
     */
    boolean bfs(int v, int s, int t, Edge[] parents) {
        boolean visited[] = new boolean [v+1];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(s);
        visited[s] = true;

        while(!queue.isEmpty()) {
            int current = queue.poll();
            for(Edge currentNeighbor : rGraph[current].values()) {
                if(!visited[currentNeighbor.to] && currentNeighbor.resCap > 0) {
                    // if sink is found
                    if(currentNeighbor.to == t) {
                        parents[t] = currentNeighbor;
                        return true;
                    }
                    queue.add(currentNeighbor.to);
                    parents[currentNeighbor.to] = currentNeighbor;
                    visited[currentNeighbor.to] = true;
                }
            }
        }
        return false;
    }
}

