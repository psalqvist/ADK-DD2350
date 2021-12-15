import java.util.HashMap;

public class Reduce {
    public static void main(String[] args) {
        // O(v^2+v^2+e) = O(v^2+e) -> Polynomial time reduction

        final int ROLES_BASECASE = 3;
        final int SCENES_BASECASE = 2;
        final int ACTORS_BASECASE = 3;
        final int EDGES_MAX = 25000*2;
        Kattio io = new Kattio(System.in, System.out);

        // get number of vertices v, edges e and max use of colors m
        int v, e, m;
        v = io.getInt();
        e = io.getInt();
        m = io.getInt();

        // add vertices that are not isolated -> roles
        // store edges in edges array
        // O(e)
        HashMap<Integer, Integer> roles = new HashMap<>();
        int edges[] = new int[EDGES_MAX];
        int x, y;
        int z = 0;
        for(int i=0; i<e; i++) {
            x = io.getInt();
            y = io.getInt();
            roles.put(x, x);
            roles.put(y, y);

            edges[z] = x;
            edges[z+1] = y;
            z+=2;
        }

        // do not keep isolated vertices
        // adjust the values in map due to isolated vertices
        // O(v^2)
        int isolatedVertices = 0;
        for(int i=1; i<=v; i++) {
            if(!roles.containsKey(i)) { // isolated vertex found, decrease value of remaining vertices by 1
                isolatedVertices++;
                for(int j=i; j<=v; j++) {
                    if(roles.containsKey(j)) {
                        roles.put(j, roles.get(j) - 1);
                    }
                }
            }
        }
        v -= isolatedVertices;

        // add roles, scenes and actors from base case
        int n = v + ROLES_BASECASE;
        int s = e + SCENES_BASECASE;
        int k;
        // 1 <= m <= 1 073 741 824 and 1 <= v <= 300
        // next loop will be O(v*m)
        // but m >= v -> always yes instance. So if m > v, we can limit m to m = v.
        // this will mean that at worst case loop runs for O(v^2)
        if(m > v) { m = v; }
        k = m + ACTORS_BASECASE;

        StringBuilder sb = new StringBuilder();

        sb.append(n).append("\n").append(s).append("\n").append(k).append("\n");
        sb.append("1").append(" ").append("1").append("\n");
        sb.append("1").append(" ").append("2").append("\n");
        sb.append("1").append(" ").append("3").append("\n");

        // O(v^2)
        for(int i=0; i<v; i++) {
            sb.append(m + " ");
            for(int j=3; j<k; j++) {
                sb.append(j + " ");
            }
            sb.append("\n");
        }

        sb.append("2").append(" ").append("1").append(" ").append("3").append("\n");
        sb.append("2").append(" ").append("2").append(" ").append("3").append("\n");

        // O(e)
        int i=0;
        while(edges[i] != 0) {
            sb.append("2").append(" ").append(roles.get(edges[i]) + ROLES_BASECASE).append(" ").append(roles.get(edges[i+1]) + ROLES_BASECASE)
                    .append("\n");
            i+=2;
        }

        io.print(sb);

        io.flush();
        io.close();

    }
}
