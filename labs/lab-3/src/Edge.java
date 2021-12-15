public class Edge {
    int from, to, cap, resCap, flow;
    Edge backEdge;

    Edge(int from, int to, int cap) {
        this.from = from;
        this.to = to;
        this.flow = 0;
        this.cap = cap;
        this.resCap = cap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Edge edge = (Edge) o;
        if (from != edge.from) { return false; }
        if (to != edge.to) { return false; }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + from;
        result = prime * result + to;
        return result;
    }
}