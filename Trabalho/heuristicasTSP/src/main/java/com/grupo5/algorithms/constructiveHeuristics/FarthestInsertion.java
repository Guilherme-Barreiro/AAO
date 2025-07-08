package com.grupo5.algorithms.constructiveHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Classe que implementa o algoritmo Farthest Insertion para o problema do caixeiro viajante.
 */
public class FarthestInsertion {

    /**
     * Executa o algoritmo Farthest Insertion com base numa lista de cidades.
     *
     * @param cities Lista de cidades a serem visitadas.
     * @return Uma lista representando o tour calculado.
     */
    public static List<Utils.City> farthestInsertion(List<Utils.City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();

        /**
         * Inicia com a primeira cidade e a cidade mais distante dela
         */
        Utils.City start = cities.get(0);
        Utils.City farthest = null;
        double maxDist = -1;
        for (Utils.City city : cities) {
            if (city == start) continue;
            double d = start.distanceTo(city);
            if (d > maxDist) {
                maxDist = d;
                farthest = city;
            }
        }

        /**
         * Cria o tour inicial: start -> farthest -> start (fechando o ciclo)
         */
        List<Utils.City> tour = new ArrayList<>();
        tour.add(start);
        tour.add(farthest);
        tour.add(start);

        /**
         * Conjunto de cidades que ainda não foram inseridas
         */
        Set<Utils.City> unvisited = new HashSet<>(cities);
        unvisited.remove(start);
        unvisited.remove(farthest);

        /**
         * Enquanto houver cidades não visitadas
         */
        while (!unvisited.isEmpty()) {
            Utils.City candidate = null;
            double candidateDistance = -1;

            /**
             * Para cada cidade não visitada, determina a distância mínima até alguma cidade no tour
             */
            for (Utils.City city : unvisited) {
                double minDistance = Double.POSITIVE_INFINITY;
                for (Utils.City tCity : tour) {
                    double d = city.distanceTo(tCity);
                    if (d < minDistance) {
                        minDistance = d;
                    }
                }

                /**
                 * Seleciona a cidade cuja distância mínima é a maior (mais afastada do tour)
                 */
                if (minDistance > candidateDistance) {
                    candidateDistance = minDistance;
                    candidate = city;
                }
            }

            /**
             * Para a cidade candidata, determina a melhor posição de inserção que minimize o aumento do tour
             */
            int bestInsertIndex = -1;
            double minIncrease = Double.POSITIVE_INFINITY;
            for (int i = 0; i < tour.size() - 1; i++) {
                Utils.City current = tour.get(i);
                Utils.City next = tour.get(i + 1);
                double increase = current.distanceTo(candidate) + candidate.distanceTo(next) - current.distanceTo(next);
                if (increase < minIncrease) {
                    minIncrease = increase;
                    bestInsertIndex = i + 1;
                }
            }

            /**
             * Insere a cidade candidata na melhor posição encontrada
             */
            tour.add(bestInsertIndex, candidate);
            unvisited.remove(candidate);
        }

        return tour;
    }

    /**
     * Método principal para execução do algoritmo com um ficheiro de entrada.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        List<Utils.City> tour = farthestInsertion(cities);
        System.out.println("Farthest Insertion Tour:");
        for (Utils.City city : tour) {
            System.out.print(city + " ");
        }
        System.out.println();
        double cost = calculatePathCost(tour);
        System.out.println("Custo total do tour: " + cost);
    }
}
