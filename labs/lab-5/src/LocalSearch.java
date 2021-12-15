import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalSearch {
    List<List<Integer>> roles = new ArrayList<>();
    List<List<Integer>> scenes = new ArrayList<>();
    List<List<Integer>> actors = new ArrayList<>();
    List<List<Integer>> result = new ArrayList<>();

    public static void main(String[] args) {
        LocalSearch ls = new LocalSearch();
        ls.init();
        ls.run(ls, 100);
    }

    void run(LocalSearch ls, int maxIterations) {
        int i = 0;
        int temp;
        int min = Integer.MAX_VALUE;
        while(i < maxIterations) {
            temp = ls.assignRoles();
            if(temp < min) {
                min = temp;
                result = new ArrayList(actors);
            }
            for(List<Integer> actor : actors) {
                actor.clear();
            }
            i++;
        }
        ls.printActors(result);
    }

    void init() {
        Kattio io = new Kattio(System.in, System.out);
        int n, s, k;
        n = io.getInt();
        s = io.getInt();
        k = io.getInt();
        List<Integer> role;
        List<Integer> scene;
        int numberOfActors;
        int numberOfScenes;

        for(int i=0; i<n; i++) {
            numberOfActors = io.getInt();
            role = new ArrayList<>();
            for(int j=0; j<numberOfActors; j++) {
                role.add(io.getInt());
            }
            roles.add(role);
        }

        for(int i=0; i<s; i++) {
            numberOfScenes = io.getInt();
            scene = new ArrayList<>();
            for(int j=0; j<numberOfScenes; j++) {
                scene.add(io.getInt());
            }
            scenes.add(scene);
        }

        for(int i=0; i<k; i++) {
            actors.add(new ArrayList<Integer>());
        }
    }

    /**
     * If r1 and r2 are in the same scene return false, otherwise return true.
     * @param r1
     * @param r2
     * @return
     */
    boolean rolesValid(int r1, int r2) {
        for(int i=0; i<scenes.size(); i++) {
            if(scenes.get(i).contains(r1) && scenes.get(i).contains(r2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * If actor a can play role r without breaking any rules, return true, otherwise false.
     * @param a
     * @param r
     * @return
     */
    boolean actorValid(int a, int r) {

        if(a==1 || a==2) { // if the actor is 1 or 2, check that all roles played by 1 and 2 isn't in same scene as r
            for(int i = 0; i < actors.get(0).size(); i++) {
                if(!rolesValid(actors.get(0).get(i), r)) {
                    return false;
                }
            }
            for(int i = 0; i < actors.get(1).size(); i++) {
                if(!rolesValid(actors.get(1).get(i), r)) {
                    return false;
                }
            }
        } else { // else, check that roles played by a isn't in the same scene as r
            for(int i = 0; i < actors.get(a-1).size(); i++) {
                if(!rolesValid(actors.get(a-1).get(i), r)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Finds valid roles for actor1 and actor2.
     * Then, assigns roles to remaining actors in a greedy manner.
     * If there are still unassigned roles, use super actors to assign these roles.
     */
    int assignRoles() {
        // extract the roles that actor1 and actor2 can play
        List<Integer> actor1 = new ArrayList<>();
        List<Integer> actor2 = new ArrayList<>();
        for(int i = 0; i < roles.size(); i++) {
            if(roles.get(i).contains(1)) {
                actor1.add(i+1);
            }
            if(roles.get(i).contains(2)) {
                actor2.add(i+1);
            }
        }
        Collections.shuffle(actor1);
        Collections.shuffle(actor2);
        // find valid roles for actor1 and actor2 to validate their special requirements as early as possible
        for(int i = 0; i < actor1.size(); i++) {
            for(int j = 0; j < actor2.size(); j++) {
                if(rolesValid(actor1.get(i), actor2.get(j))) {
                    // add role to actors, and clear the actors in roles
                    actors.get(0).add(actor1.get(i));
                    roles.get(actor1.get(i)-1).clear();
                    actors.get(1).add(actor2.get(j));
                    roles.get(actor2.get(j)-1).clear();
                    break;
                }
            }
            if(actors.get(0).size() != 0) { break; } // if we have found two valid roles for actor 1 and 2
        }

        // TODO: Remove instead of clear? Could make adding superactors and printing result a lot faster and easier
        // assign valid remaining roles to actors in a greedy manner
        int role;
        int actor = 0;
        for(int i = 0; i < roles.size(); i++) {
            if(!roles.get(i).isEmpty()) {
                role = i+1;
                for(int j = 0; j < roles.get(i).size(); j++) {
                    actor = (Integer) roles.get(i).get(j);
                    if(actorValid(actor, role)) {
                        actors.get(actor-1).add(role);
                        roles.get(i).clear();
                        break;
                    }
                }
            }
        }

        // add superactors if necessary
        List<Integer> superActor;
        int superActors = 0;
        for(int i = 0; i < roles.size(); i++) {
            if(!roles.get(i).isEmpty()) {
                superActor = new ArrayList<>();
                superActor.add(i+1);
                actors.add(superActor);
                superActors++;
            }
        }

        return (actor + superActors);
    }

    void printRoles() {
        for(int i=0; i<roles.size(); i++) {
            for(int j=0; j<roles.get(i).size(); j++) {
                System.out.print(roles.get(i).get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    void printScenes() {
        for(int i=0; i<scenes.size(); i++) {
            for(int j=0; j<scenes.get(i).size(); j++) {
                System.out.print(scenes.get(i).get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    void printActor(List<Integer> actor) {
        for(int i=0; i<actor.size(); i++) {
            System.out.print(actor.get(i) + " ");
        }
        System.out.println();
    }

    void printActors(List<List<Integer>> result) {
        int numberOfActors = 0;
        for(int i=0; i<result.size(); i++) {
            if(result.get(i).size() != 0) { numberOfActors++; }
        }
        System.out.println(numberOfActors);

        int actor;
        for(int i=0; i<result.size(); i++) {
            actor = i+1;
            if(result.get(i).size() != 0) {
                System.out.print(actor + " " + result.get(i).size() + " ");
                for(int j=0; j<result.get(i).size(); j++) {
                    System.out.print(result.get(i).get(j) + " ");
                }
                System.out.println();
            }

        }
    }
}



