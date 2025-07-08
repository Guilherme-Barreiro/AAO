package com.grupo5.algorithms.constructiveHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Implementa o algoritmo Nearest Neighbor para o problema do caixeiro viajante (TSP).
 */
public class NearestNeighbor {

    /**
     * Gera um tour aproximado usando o algoritmo Nearest Neighbor.
     *
     * @param cities Lista de cidades a serem visitadas.
     * @return Lista representando o tour, começando e terminando na cidade inicial.
     */
    public static List<Utils.City> nearestNeighborTour(List<Utils.City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();

        List<Utils.City> tour = new ArrayList<>();
        Set<Utils.City> unvisited = new HashSet<>(cities);

        /* Começa com a primeira cidade da lista */
        Utils.City current = cities.get(0);
        tour.add(current);
        unvisited.remove(current);

        /* Enquanto houver cidades não visitadas, seleciona a mais próxima */
        while (!unvisited.isEmpty()) {
            Utils.City nextCity = null;
            double minDistance = Double.POSITIVE_INFINITY;

            for (Utils.City candidate : unvisited) {
                double distance = current.distanceTo(candidate);
                if (distance < minDistance) {
                    minDistance = distance;
                    nextCity = candidate;
                }
            }

            /* Adiciona a cidade escolhida ao tour e atualiza a cidade atual */
            tour.add(nextCity);
            unvisited.remove(nextCity);
            current = nextCity;
        }

        /* Fecha o ciclo voltando para a cidade inicial */
        tour.add(tour.get(0));
        return tour;
    }

    /**
     * Executa o algoritmo com base num ficheiro de entrada.
     *
     * @param args Argumentos da linha de comandos (não utilizados).
     */
    public static void main(String[] args) {
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        List<Utils.City> tour = nearestNeighborTour(cities);
        System.out.println("Nearest Neighbor Tour:");
        for (Utils.City city : tour) {
            System.out.print(city + " ");
        }
        System.out.println();
        double cost = calculatePathCost(tour);
        System.out.println("Custo total do tour: " + cost);
    }
}
