import com.grupo5.algorithms.constructiveHeuristics.*;
import com.grupo5.algorithms.localAndSearchHeuristics.*;
import com.grupo5.algorithms.utils.Utils;
import com.grupo5.algorithms.utils.Utils.City;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    private static final String TSP_DIR = "src/main/resources";
    private static final String OUTPUT_FILE = "results.txt";

    @FunctionalInterface
    interface HeuristicRunner {
        List<City> run(List<City> cities);
    }

    public static void main(String[] args) throws IOException {
        List<String> tspFiles = getTSPFiles();
        PrintWriter fileWriter = new PrintWriter(new FileWriter(OUTPUT_FILE, true));

        Map<String, HeuristicRunner> constructiveHeuristics = Map.of(
                "NearestNeighbor", NearestNeighbor::nearestNeighborTour,
                "CheapestInsertion", CheapestInsertion::cheapestInsertion,
                "FarthestInsertion", FarthestInsertion::farthestInsertion,
                "RandomPathConstruction", RandomPathConstruction::randomPathTour,
                "MinimumSpanningTreeHeuristic", MinimumSpanningTreeHeuristic::mstHeuristic
        );

        Map<String, HeuristicRunner> localSearchHeuristics = Map.of(
                "Opt2", Opt2::twoOpt,
                "Opt3", Opt3::opt3,
                "OptOr", OptOr::orOpt,
                "OptK", cities -> OptK.optK(cities, 4),
                "LinKernighanHeuristic", LinKernighanHeuristic::linKernighan
        );

        for (String tsp : tspFiles) {
            List<City> cities = Utils.readTSPFile(TSP_DIR + "/" + tsp);
            if (cities.isEmpty()) continue;

            System.out.println("\n=== Problema: " + tsp + " ===");
            fileWriter.println("\n=== Problema: " + tsp + " ===");

            for (Map.Entry<String, HeuristicRunner> entry : constructiveHeuristics.entrySet()) {
                String methodName = entry.getKey();
                HeuristicRunner constructor = entry.getValue();

                List<City> initialSolution = constructor.run(new ArrayList<>(cities));
                double initialCost = Utils.calculatePathCost(initialSolution);

                System.out.printf("[%-30s] Solução Inicial: %.2f\n", methodName, initialCost);
                fileWriter.printf("[%-30s] Solução Inicial: %.2f\n", methodName, initialCost);

                for (Map.Entry<String, HeuristicRunner> searchEntry : localSearchHeuristics.entrySet()) {
                    String searchName = searchEntry.getKey();
                    HeuristicRunner improver = searchEntry.getValue();

                    long start = System.currentTimeMillis();
                    List<City> improvedSolution = improver.run(new ArrayList<>(initialSolution));
                    long duration = System.currentTimeMillis() - start;
                    double improvedCost = Utils.calculatePathCost(improvedSolution);

                    System.out.printf("\t-> %-25s | SE: %.2f | Desvio: %.2f%% | Tempo: %d ms\n",
                            searchName, improvedCost,
                            0.0, // % desvio from SO - will be filled later
                            duration);
                    fileWriter.printf("\t-> %-25s | SE: %.2f | Desvio: %.2f%% | Tempo: %d ms\n",
                            searchName, improvedCost,
                            0.0,
                            duration);
                }
            }
        }

        fileWriter.close();
    }

    private static List<String> getTSPFiles() throws IOException {
        List<String> tspFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(TSP_DIR), "*.tsp")) {
            for (Path entry : stream) {
                tspFiles.add(entry.getFileName().toString());
            }
        }
        Collections.sort(tspFiles);
        return tspFiles;
    }
}