import java.util.*;
import java.util.stream.Collectors;

public class TreeAStarSolver {

    static class Node implements Comparable<Node> {
        List<Integer> tree;
        int costSoFar;
        int estimatedTotalCost;
        String lastMove;
        Node parent;

        public Node(List<Integer> tree, int costSoFar, int estimatedTotalCost, String lastMove, Node parent) {
            this.tree = tree;
            this.costSoFar = costSoFar;
            this.estimatedTotalCost = estimatedTotalCost;
            this.lastMove = lastMove;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.estimatedTotalCost, other.estimatedTotalCost);
        }
    }

    public static boolean solve(TreeState initialState, List<Integer> targetTree) {
        System.out.println("🚀 A* Solver Started. Tree size: " + initialState.getTree().size());

        final int MAX_EXPLORED = 2_000_000;
        final int MAX_DEPTH = 100;
        final long MAX_SECONDS = 15;

        int explored = 0;
        long startTime = System.nanoTime();

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        List<Integer> initialTree = initialState.getTree();
        String initialKey = treeToKey(initialTree);

        Node root = new Node(initialTree, 0, heuristic(initialTree, targetTree), null, null);
        openSet.add(root);
        visited.add(initialKey);

        Node bestNode = root;

        while (!openSet.isEmpty()) {
            long elapsed = (System.nanoTime() - startTime) / 1_000_000_000;
            if (explored++ > MAX_EXPLORED || elapsed > MAX_SECONDS) {
                System.out.println("❌ A* skipped (limit reached: " + explored + " nodes, " + elapsed + "s).");
                printBestAttempt(bestNode);
                return false;
            }

            Node current = openSet.poll();
            if (current.tree.equals(targetTree)) {
                printPath(current);
                System.out.println("Final Tree: " + current.tree);
                return true;
            }

            if (current.costSoFar > MAX_DEPTH) continue;

            for (int i = 1; i < current.tree.size(); i++) {
                List<Integer> newTree = new ArrayList<>(current.tree);
                Collections.swap(newTree, i, (i - 1) / 2);
                String newKey = treeToKey(newTree);

                if (visited.add(newKey)) {
                    String move = "Swap index " + i + " (" + current.tree.get(i) + ") with parent → Tree: " + newTree;
                    int g = current.costSoFar + 1;
                    int h = heuristic(newTree, targetTree);

                    Node child = new Node(newTree, g, g + h, move, current);
                    openSet.add(child);

                    if (g > bestNode.costSoFar) bestNode = child;
                }
            }
        }

        System.out.println("❌ No solution found.");
        printBestAttempt(bestNode);
        return false;
    }

    private static int heuristic(List<Integer> current, List<Integer> target) {
        int cost = 0;
        Map<Integer, Integer> targetIndex = new HashMap<>();
        for (int i = 0; i < target.size(); i++) targetIndex.put(target.get(i), i);
        for (int i = 0; i < current.size(); i++) {
            int val = current.get(i);
            int targetPos = targetIndex.get(val);
            int depth = (int) (Math.log(i + 1) / Math.log(2));
            cost += Math.abs(i - targetPos) * depth;
        }
        return cost;
    }

    private static String treeToKey(List<Integer> tree) {
        return tree.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private static void printPath(Node node) {
        List<String> steps = new ArrayList<>();
        while (node != null && node.lastMove != null) {
            steps.add(node.lastMove);
            node = node.parent;
        }
        Collections.reverse(steps);
        System.out.println("✅ Found solution in " + steps.size() + " swaps.");
        for (String step : steps) System.out.println(step);
    }

    private static void printBestAttempt(Node best) {
        if (best == null) {
            System.out.println("No best attempt found.");
            return;
        }

        List<String> steps = new ArrayList<>();
        Node current = best;

        while (current != null && current.lastMove != null) {
            steps.add(current.lastMove);
            current = current.parent;
        }

        Collections.reverse(steps);
        System.out.println("🟡 Best attempt reached with " + steps.size() + " swaps:");
        for (String step : steps) {
            System.out.println(step);
        }
        System.out.println("Partial Tree (not sorted): " + best.tree);
    }
}
