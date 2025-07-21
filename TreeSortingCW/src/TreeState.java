import java.util.*;

public class TreeState {
    private List<Integer> tree;

    public TreeState(List<Integer> treeValues) {
        this.tree = new ArrayList<>(treeValues);
    }

    // Swap a child node with its parent
    public void swap(int childIndex) {
        if (childIndex == 0) return; // Can't swap root
        int parentIndex = (childIndex - 1) / 2;
        Collections.swap(tree, childIndex, parentIndex);
    }

    // Return true if current tree matches the target
    public boolean isGoalState(List<Integer> target) {
        return this.tree.equals(target);
    }

    // Return a deep copy of the tree
    public TreeState cloneState() {
        return new TreeState(new ArrayList<>(this.tree));
    }

    // Print tree
    public void printTree() {
        System.out.println(tree);
    }

    // Return the tree values
    public List<Integer> getTree() {
        return tree;
    }
}
