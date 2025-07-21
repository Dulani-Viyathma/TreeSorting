//20230369
import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("📁 Choose input mode:");
        System.out.println("1 - Process all .txt files from 'benchmarks/' folder");
        System.out.println("2 - Manually enter the path to a single .txt file");
        System.out.print("Enter your choice (1 or 2): ");
        String choice = scanner.nextLine();

        List<File> filesToProcess = new ArrayList<>();

        if (choice.equals("1")) {
            File folder = new File("benchmarks");
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

            if (files == null || files.length == 0) {
                System.out.println("❌ No .txt files found in 'benchmarks/' folder.");
                return;
            }

            Arrays.sort(files, Comparator
                    .comparingInt(Main::extractNumberFromFileName)
                    .thenComparing(File::getName));

            filesToProcess.addAll(Arrays.asList(files));

        } else if (choice.equals("2")) {
            System.out.print("Enter full or relative path to the .txt file: ");
            String path = scanner.nextLine();
            File file = new File(path);

            if (!file.exists() || !file.getName().toLowerCase().endsWith(".txt")) {
                System.out.println("❌ Invalid file path or not a .txt file.");
                return;
            }

            filesToProcess.add(file);
        } else {
            System.out.println("❌ Invalid choice. Please enter 1 or 2.");
            return;
        }

        for (File file : filesToProcess) {
            System.out.println("\n📄 File: " + file.getName());

            boolean solved = false;
            long startTime = System.nanoTime();

            try {
                List<Integer> treeValues = TreeParser.parseTreeFromFile(file.getPath());
                TreeState treeState = new TreeState(treeValues);

                System.out.print("Parsed Initial Tree: ");
                treeState.printTree();

                List<Integer> targetTree = new ArrayList<>(treeValues);
                Collections.sort(targetTree);
                System.out.println("Target BST Tree: " + targetTree);

                if (treeValues.size() <= 7) {
                    System.out.println("➡ Using BFS...");
                    solved = TreeSolver.solve(treeState, targetTree);
                } else {
                    System.out.println("➡ Using A*...");
                    solved = TreeAStarSolver.solve(treeState, targetTree);
                }



            } catch (OutOfMemoryError e) {
                System.err.println("❌ Skipped " + file.getName() + ": Ran out of memory.");
            } catch (Exception e) {
                System.err.println("❌ Error reading " + file.getName() + ": " + e.getMessage());
            }

            long endTime = System.nanoTime();
            double timeInSeconds = (endTime - startTime) / 1_000_000_000.0;


                System.out.printf("⏱ Time taken: %.5f seconds%n", timeInSeconds);

        }

        scanner.close();
    }

    public static int extractNumberFromFileName(File file) {
        String name = file.getName().replace(".txt", "");
        String[] parts = name.split("_");
        if (parts.length >= 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return Integer.MAX_VALUE;
            }
        }
        return Integer.MAX_VALUE;
    }
}
