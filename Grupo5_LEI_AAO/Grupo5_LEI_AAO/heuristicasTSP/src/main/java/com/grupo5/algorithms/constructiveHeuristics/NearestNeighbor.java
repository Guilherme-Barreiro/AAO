package com.grupo5.algorithms.constructiveHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa o algoritmo heurístico "Nearest Neighbor" (vizinho mais próximo)
 * para o Problema do Caixeiro Viajante (TSP).
 *
 * Este algoritmo começa por uma cidade inicial e, em cada passo, escolhe a cidade
 * mais próxima ainda não visitada. No final, regressa à cidade inicial, formando
 * um ciclo completo.
 */
public class NearestNeighbor {

    /**
     * Executa o algoritmo Nearest Neighbor sobre uma lista de cidades.
     *
     * @param cities Lista de cidades a visitar.
     * @return Tour resultante representado como uma lista de cidades, com ciclo fechado.
     */
    public static List<Utils.City> nearestNeighborTour(List<Utils.City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();

        List<Utils.City> tour = new ArrayList<>();
        Set<Utils.City> unvisited = new HashSet<>(cities);

        // Começa com a primeira cidade da lista
        Utils.City current = cities.get(0);
        tour.add(current);
        unvisited.remove(current);

        // Enquanto existirem cidades não visitadas
        while (!unvisited.isEmpty()) {
            Utils.City nextCity = null;
            double minDistance = Double.POSITIVE_INFINITY;

            // Procura a cidade mais próxima da atual
            for (Utils.City candidate : unvisited) {
                double distance = current.distanceTo(candidate);
                if (distance < minDistance) {
                    minDistance = distance;
                    nextCity = candidate;
                }
            }

            // Adiciona a cidade encontrada ao tour e atualiza a cidade atual
            tour.add(nextCity);
            unvisited.remove(nextCity);
            current = nextCity;
        }

        // Fecha o ciclo, voltando à cidade inicial
        tour.add(tour.get(0));
        return tour;
    }

    /**
     * Ponto de entrada do programa. Lê cidades de um ficheiro TSP,
     * executa o algoritmo Nearest Neighbor e imprime o tour resultante.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        // Lê as cidades do ficheiro
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        // Executa a heurística
        List<Utils.City> tour = nearestNeighborTour(cities);

        // Mostra o tour e o custo total
        System.out.println("Nearest Neighbor Tour:");
        for (Utils.City city : tour) {
            System.out.print(city + " ");
        }
        System.out.println();

        double cost = calculatePathCost(tour);
        System.out.println("Custo total do tour: " + cost);
    }
}
