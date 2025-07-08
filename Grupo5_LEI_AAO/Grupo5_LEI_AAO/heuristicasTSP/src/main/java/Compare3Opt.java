import com.grupo5.algorithms.localAndSearchHeuristics.Opt3;
import com.grupo5.algorithms.localAndSearchHeuristics.Opt3Best;
import com.grupo5.algorithms.utils.Utils;
import com.grupo5.algorithms.utils.Utils.City;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Compare3Opt {
    private static final String TSP_DIR = "src/main/resources";
    private static final String OUTPUT_FILE = "compare_3opt_results.txt";

    @FunctionalInterface
    interface HeuristicRunner {
        List<City> run(List<City> cities);
    }

    public static void main(String[] args) throws IOException {
        List<String> tspFiles = getTSPFiles();

        // Limitar a apenas 3 ficheiros
        if (tspFiles.size() > 6) {
            tspFiles = tspFiles.subList(0, 6);
        }

        PrintWriter fileWriter = new PrintWriter(new FileWriter(OUTPUT_FILE, false));

        Map<String, HeuristicRunner> versions = Map.of(
                "Opt3_FirstImprovement", Opt3::opt3,
                "Opt3_BestImprovement", Opt3Best::opt3BestImprovement
        );

        for (String tsp : tspFiles) {
            List<City> cities = Utils.readTSPFile(TSP_DIR + "/" + tsp);
            if (cities.isEmpty()) continue;

            // Criar solução inicial simples
            List<City> initialTour = new ArrayList<>(cities);
            initialTour.add(cities.get(0)); // fechar ciclo

            double initialCost = Utils.calculatePathCost(initialTour);

            System.out.println("\n=== " + tsp + " ===");
            fileWriter.println("\n=== " + tsp + " ===");
            System.out.printf("Custo inicial: %.2f\n", initialCost);
            fileWriter.printf("Custo inicial: %.2f\n", initialCost);

            for (Map.Entry<String, HeuristicRunner> entry : versions.entrySet()) {
                String label = entry.getKey();
                HeuristicRunner method = entry.getValue();

                long start = System.currentTimeMillis();
                List<City> improved = method.run(new ArrayList<>(initialTour));
                long duration = System.currentTimeMillis() - start;

                double improvedCost = Utils.calculatePathCost(improved);
                double deviation = 100.0 * (improvedCost - initialCost) / initialCost;

                System.out.printf("\t%-25s | SE: %.2f | Δ: %.2f%% | Tempo: %d ms\n",
                        label, improvedCost, deviation, duration);
                fileWriter.printf("\t%-25s | SE: %.2f | Δ: %.2f%% | Tempo: %d ms\n",
                        label, improvedCost, deviation, duration);
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
