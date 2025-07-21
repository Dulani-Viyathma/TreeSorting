import java.io.*;
import java.util.*;

public class TreeParser {

    // Reads a file and returns the tree as a list of integers
    public static List<Integer> parseTreeFromFile(String filePath) throws IOException {
        List<Integer> treeValues = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            for (String part : parts) {
                if (!part.isEmpty()) {
                    treeValues.add(Integer.parseInt(part));
                }
            }
        }

        reader.close();

        if (!isValidTree(treeValues)) {
            throw new IllegalArgumentException("Invalid tree: duplicate values or wrong node count.");
        }

        return treeValues;
    }

    // Check if tree is valid: unique values and complete binary tree shape
    private static boolean isValidTree(List<Integer> treeValues) {
        Set<Integer> unique = new HashSet<>(treeValues);

        if (unique.size() != treeValues.size()) {
            return false; // Contains duplicates
        }

        int n = treeValues.size();
        int h = (int) (Math.log(n + 1) / Math.log(2));
        return (Math.pow(2, h) - 1) == n;
    }
}
