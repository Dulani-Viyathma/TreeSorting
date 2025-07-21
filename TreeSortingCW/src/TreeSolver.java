import java.util.*;

public class TreeSolver {

    /**
     * Solves the tree sorting problem using Breadth-First Search (BFS).
     * Returns true if a solution is found, false otherwise.
     */
    public static boolean solve(TreeState initialState, List<Integer> targetState) {
        final int MAX_VISITED = 100_000;

        Queue<TreeState> queue = new LinkedList<>();
        Set<List<Integer>> visited = new HashSet<>();
        Map<List<Integer>, List<String>> paths = new HashMap<>();

        queue.add(initialState);
        visited.add(initialState.getTree());
        paths.put(initialState.getTree(), new ArrayList<>());

        // 🔎 Track best attempt in case of failure
        List<String> bestSwapSequence = new ArrayList<>();
        List<Integer> bestState = initialState.getTree();

        while (!queue.isEmpty()) {
            // ❗ Timeout or memory protection
            if (visited.size() > MAX_VISITED) {
                System.out.println("❌ BFS took too long or used too much memory. Skipping.");
                System.out.println("🟡 Best attempt reached with " + bestSwapSequence.size() + " swaps:");
                for (String step : bestSwapSequence) {
                    System.out.println(step);
                }
                System.out.println("Partial Tree (not sorted): " + bestState);
                return false;
            }

            TreeState current = queue.poll();
            List<Integer> currentTree = current.getTree();

            // ✅ Goal check
            if (current.isGoalState(targetState)) {
                List<String> swapSequence = paths.get(currentTree);
                System.out.println("✅ Found solution in " + swapSequence.size() + " swaps.");
                for (String step : swapSequence) {
                    System.out.println(step);
                }
                System.out.println("Final Tree: " + currentTree);
                return true;
            }

            // 🔄 Explore children by swapping each node with its parent
            for (int i = 1; i < currentTree.size(); i++) {
                TreeState newState = current.cloneState();
                newState.swap(i);
                List<Integer> newTree = newState.getTree();

                if (!visited.contains(newTree)) {
                    visited.add(new ArrayList<>(newTree));
                    queue.add(newState);

                    List<String> newPath = new ArrayList<>(paths.get(currentTree));
                    newPath.add("Swap index " + i + " (" + currentTree.get(i) + ") with parent → Tree: " + newTree);
                    paths.put(new ArrayList<>(newTree), newPath);

                    // 💡 Track best attempt so far
                    if (newPath.size() > bestSwapSequence.size()) {
                        bestSwapSequence = newPath;
                        bestState = newTree;
                    }
                }
            }
        }

        // 🔚 No solution found
        System.out.println("❌ No solution found.");
        return false;
    }
}
