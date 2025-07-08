package com.grupo5.algorithms.constructiveHeuristics;

import com.grupo5.algorithms.utils.Utils;

import java.util.*;

import static com.grupo5.algorithms.utils.Utils.calculatePathCost;

/**
 * Classe que implementa o algoritmo de Cheapest Insertion para o problema do caixeiro viajante.
 */
public class CheapestInsertion {

    /**
     * Executa o algoritmo Cheapest Insertion para gerar um tour com base em uma lista de cidades.
     *
     * @param cities Lista de cidades a serem visitadas.
     * @return Uma lista representando o tour calculado, começando e terminando na cidade inicial.
     */
    public static List<Utils.City> cheapestInsertion(List<Utils.City> cities) {
        if (cities.isEmpty()) return Collections.emptyList();

        /**
         * Seleciona a primeira cidade e a cidade mais distante dela
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
         * Conjunto de cidades que ainda não foram inseridas no tour
         */
        Set<Utils.City> unvisited = new HashSet<>(cities);
        unvisited.remove(start);
        unvisited.remove(farthest);

        /**
         * Enquanto houver cidades não visitadas, insere a que gera o menor aumento no custo
         */
        while (!unvisited.isEmpty()) {
            Utils.City bestCity = null;
            int bestInsertIndex = -1;
            double minIncrease = Double.POSITIVE_INFINITY;

            /**
             * Testa cada cidade não inserida para encontrar a melhor posição de inserção
             */
            for (Utils.City city : unvisited) {
                for (int i = 0; i < tour.size() - 1; i++) {
                    Utils.City current = tour.get(i);
                    Utils.City next = tour.get(i + 1);
                    double increase = current.distanceTo(city) + city.distanceTo(next) - current.distanceTo(next);
                    if (increase < minIncrease) {
                        minIncrease = increase;
                        bestCity = city;
                        bestInsertIndex = i + 1;
                    }
                }
            }

            /**
             * Insere a melhor cidade encontrada na posição ótima
             */
            tour.add(bestInsertIndex, bestCity);
            unvisited.remove(bestCity);
        }

        return tour;
    }

    /**
     * Método principal para execução do algoritmo com um ficheiro de entrada.
     *
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        /**
         * Usa o método utilitário para ler o ficheiro TSP
         */
        List<Utils.City> cities = Utils.readTSPFile("src/main/resources/a280.tsp");

        if (cities.isEmpty()) {
            System.out.println("Nenhuma cidade encontrada no ficheiro.");
            return;
        }

        /**
         * Executa o algoritmo e imprime o tour encontrado
         */
        List<Utils.City> tour = cheapestInsertion(cities);
        System.out.println("Tour encontrado:");
        for (Utils.City city : tour) {
            System.out.print(city + " ");
        }
        System.out.println();
        double cost = calculatePathCost(tour);
        System.out.println("Custo total do tour: " + cost);
    }
}
